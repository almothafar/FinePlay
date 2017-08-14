package controllers.manage.company;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.supercsv.prefs.CsvPreference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import common.data.validation.groups.Create;
import common.data.validation.groups.Update;
import common.system.MessageKeys;
import common.utils.CSVs;
import play.mvc.Controller;
import models.base.EntityDao;
import models.company.Company;
import models.manage.company.UploadFormContent;
import models.manage.company.UploadFormContent.Operation;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.Logger;
import play.Logger.ALogger;
import play.api.PlayException;
import play.api.PlayException.ExceptionSource;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.MessagesApi;
import play.mvc.BodyParser;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

public class Upload extends Controller {

	private static final ALogger LOGGER = Logger.of(Upload.class);

	@Inject
	private MessagesApi messages;

	@Inject
	private JPAApi jpaApi;

	@Inject
	private FormFactory formFactory;

	private final EntityDao<Company> companyDao = new EntityDao<Company>() {
	};

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = {Permission.MANAGE})
	@BodyParser.Of(value = BodyParser.MultipartFormData.class)
	@Transactional()
	@RequireCSRFCheck
	public CompletionStage<Result> upload() {

		final Result result = jpaApi.withTransaction(manager -> {

			final MultipartFormData<File> multipartFormData = request().body().asMultipartFormData();
			final Form<UploadFormContent> uploadForm = formFactory.form(UploadFormContent.class).bindFromRequest(multipartFormData.asFormUrlEncoded());

			if (!uploadForm.hasErrors()) {

				final UploadFormContent uploadFormContent = uploadForm.get();

				final Operation operation = uploadFormContent.getOperation();

				try {

					final FilePart<File> uploadFilePart = multipartFormData.getFile(Company.UPLOADFILE);
					if (uploadFilePart.getFilename().isEmpty()) {

						throw new IllegalStateException(messages.get(lang(), MessageKeys.ERROR_PATH_EMPTY));
					}
					final Path uploadPath = uploadFilePart.getFile().toPath();

					final String csv = Files.readAllLines(uploadPath, StandardCharsets.UTF_8).stream().collect(Collectors.joining(CsvPreference.STANDARD_PREFERENCE.getEndOfLineSymbols()));
					final List<Company> uploadCompanies = CSVs.toBeans(Company.getHeaders(), Company.getReadCellProcessors(), csv, Company.class);
					uploadCompanies.stream().forEach(company -> company.afterRead());

					final UploadProcess process = createUploadProcess(operation);
					process.validate(uploadCompanies);
					process.execute(manager, uploadCompanies);
				} catch (final ExceptionSource e) {

					manager.getTransaction().setRollbackOnly();

					final Form<UploadFormContent> failureUploadForm = formFactory.form(UploadFormContent.class).fill(uploadFormContent);
					failureUploadForm.withGlobalError(messages.get(lang(), MessageKeys.SYSTEM_ERROR_X__CASE__DATA_ILLEGAL, e.line()) + ": " + e.getLocalizedMessage());
					return failureUpload(failureUploadForm);
				} catch (final Exception e) {

					manager.getTransaction().setRollbackOnly();

					final Form<UploadFormContent> failureUploadForm = formFactory.form(UploadFormContent.class).fill(uploadFormContent);
					failureUploadForm.withGlobalError(e.getLocalizedMessage());
					return failureUpload(failureUploadForm);
				}

				final ObjectMapper mapper = new ObjectMapper();
				final ObjectNode response = mapper.createObjectNode();
				response.put("status", "success");

				return ok(response);
			} else {

				return failureUpload(uploadForm);
			}
		});

		return CompletableFuture.supplyAsync(() -> {

			return result;
		});

	}

	private Result failureUpload(final Form<UploadFormContent> uploadForm) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();
		result.put("status", "error");

		final ArrayNode globalErrorsNode = mapper.createArrayNode();
		uploadForm.globalErrors().forEach(validationError -> {

			globalErrorsNode.add(messages.get(lang(), validationError.message()));
		});
		result.set("globalErrors", globalErrorsNode);

		final ObjectNode errorsNode = mapper.createObjectNode();
		uploadForm.allErrors().stream().forEach(error -> {

			final String property = error.key();

			final ArrayNode propertyErrorNode = mapper.createArrayNode();
			error.messages().forEach(message -> propertyErrorNode.add(messages.get(lang(), message)));

			errorsNode.set(property, propertyErrorNode);
		});
		result.set("errors", errorsNode);

		return badRequest(result);
	}

	private UploadProcess createUploadProcess(final Operation operation) {

		switch (operation) {
			case CREATE :

				return new CreateUploadProcess();
			case UPDATE :

				return new UpdateUploadProcess();
			default :

				throw new IllegalArgumentException("Operation: " + operation);
		}
	}

	interface UploadProcess {

		MessagesApi getMessages();

		Class<?>[] getGroups();

		default void validate(final List<Company> uploadCompanies) {

			uploadCompanies.stream().forEach(company -> {

				final Set<ConstraintViolation<Company>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(company, getGroups());
				if (1 <= violations.size()) {

					final StringBuilder builder = new StringBuilder();
					builder.append(company.getNames().getOrDefault(lang().toLocale(), company.getNames().get(Locale.US))).append(" - ");
					violations.stream().forEach(violation -> {

						builder.append(violation.getPropertyPath().toString()).append("<br>").append(getMessages().get(lang(), violation.getMessage()));
					});
					throw new IllegalStateException(builder.toString());
				}
			});
		}

		void execute(final EntityManager manager, final List<Company> uploadCompanies);
	}

	private class CreateUploadProcess implements UploadProcess {

		private final Class<?>[] groups = new Class<?>[]{Create.class};

		@Override
		public MessagesApi getMessages() {

			return messages;
		}

		@Override
		public Class<?>[] getGroups() {

			return groups;
		}

		@Override
		public void execute(final EntityManager manager, final List<Company> uploadCompanies) {

			for (int i = 0; i < uploadCompanies.size(); i++) {

				final Company uploadCompany = uploadCompanies.get(i);

				try {

					companyDao.create(manager, uploadCompany);
				} catch (final EntityExistsException e) {

					throw e;
				}
			}
		}
	}

	private class UpdateUploadProcess implements UploadProcess {

		private final Class<?>[] groups = new Class<?>[]{Update.class};

		@Override
		public MessagesApi getMessages() {

			return messages;
		}

		@Override
		public Class<?>[] getGroups() {

			return groups;
		}

		@Override
		public void execute(final EntityManager manager, final List<Company> uploadCompanies) {

			for (int i = 0; i < uploadCompanies.size(); i++) {

				final Company uploadCompany = uploadCompanies.get(i);

				final Company storedCompany;
				try {

					storedCompany = manager.find(Company.class, uploadCompany.getId());
				} catch (final Exception e) {

					throw e;
				}

				final boolean isExist = Objects.nonNull(storedCompany);
				if (!isExist) {

					throw new PlayException(//
							messages.get(lang(), MessageKeys.CONSISTENT) + " " + messages.get(lang(), MessageKeys.ERROR), //
							messages.get(lang(), MessageKeys.SYSTEM_ERROR_X_NOTEXIST, messages.get(lang(), MessageKeys.COMPANY)) + ": " + uploadCompany.getId());
				}

				try {

					companyDao.update(manager, storedCompany);
				} catch (final IllegalArgumentException e) {

					throw e;
				}
			}
		}
	}
}
