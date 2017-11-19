package controllers.manage.company.organization.list;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.prefs.CsvPreference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import common.data.validation.groups.Create;
import common.data.validation.groups.Update;
import common.system.MessageKeys;
import common.utils.CSVs;
import models.base.EntityDao;
import models.company.Company;
import models.company.organization.Organization;
import models.company.organization.OrganizationUnit;
import models.manage.company.organization.list.UploadFormContent;
import models.manage.company.organization.list.UploadFormContent.Operation;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.api.PlayException;
import play.api.PlayException.ExceptionSource;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.MessagesApi;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

public class Upload extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(Upload.class);

	@Inject
	private MessagesApi messages;

	@Inject
	private JPAApi jpaApi;

	@Inject
	private FormFactory formFactory;

	private final EntityDao<OrganizationUnit> organizationUnitDao = new EntityDao<OrganizationUnit>() {
	};

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	@BodyParser.Of(value = BodyParser.MultipartFormData.class)
	@Transactional()
	@RequireCSRFCheck
	public CompletionStage<Result> upload() {

		final Result result = jpaApi.withTransaction(manager -> {

			final MultipartFormData<File> multipartFormData = request().body().asMultipartFormData();
			final Form<UploadFormContent> uploadForm = formFactory.form(UploadFormContent.class).bindFromRequest(multipartFormData.asFormUrlEncoded());

			if (!uploadForm.hasErrors()) {

				final UploadFormContent uploadFormContent = uploadForm.get();

				final long companyId = uploadFormContent.getCompanyId();
				final Long organizationId = uploadFormContent.getOrganizationId();

				final Company company = manager.find(Company.class, companyId);
				Organization organization = company.getOrganization();

				if (Objects.nonNull(organizationId)) {

					if (Objects.isNull(organization)) {

						throw new PlayException(//
								messages.get(lang(), MessageKeys.CONSISTENT) + " " + messages.get(lang(), MessageKeys.ERROR), //
								messages.get(lang(), MessageKeys.SYSTEM_ERROR_X_NOTEXIST, messages.get(lang(), MessageKeys.ORGANIZATION)));
					}
					//
					// final LocalDateTime organizationUpdateServerDateTime =
					// DateTimes.getServerDateTime(LocalDateTime.parse(uploadFormContent.getOrganizationUpdateDateTime()));
					// if
					// (!organization.getUpdateDateTime().isEqual(organizationUpdateServerDateTime))
					// {
					//
					// throw new PlayException(messages.get(lang(),
					// MessageKeys.SYSTEM_ERROR_STATE_NOTCONSISTENT),
					// messages.get(lang(), MessageKeys.CONSISTENT) + " " +
					// messages.get(lang(), MessageKeys.ERROR));
					// }
				} else {

					if (!Objects.isNull(organization)) {

						throw new PlayException(//
								messages.get(lang(), MessageKeys.CONSISTENT) + " " + messages.get(lang(), MessageKeys.ERROR), //
								messages.get(lang(), MessageKeys.SYSTEM_ERROR_X_EXIST, messages.get(lang(), MessageKeys.ORGANIZATION)));
					}

					organization = new Organization();
					organization.setCompany(company);
					company.setOrganization(organization);
					manager.persist(company);
				}

				final Operation operation = uploadFormContent.getOperation();

				try {

					final FilePart<File> uploadFilePart = multipartFormData.getFile(OrganizationUnit.UPLOADFILE);
					if (uploadFilePart.getFilename().isEmpty()) {

						throw new IllegalStateException(messages.get(lang(), MessageKeys.ERROR_PATH_EMPTY));
					}
					final Path uploadPath = uploadFilePart.getFile().toPath();

					final String csv = Files.readAllLines(uploadPath, StandardCharsets.UTF_8).stream().collect(Collectors.joining(CsvPreference.STANDARD_PREFERENCE.getEndOfLineSymbols()));
					final List<OrganizationUnit> uploadOrganizationUnits = CSVs.toBeans(OrganizationUnit.getHeaders(), OrganizationUnit.getReadCellProcessors(), csv, OrganizationUnit.class);
					uploadOrganizationUnits.stream().forEach(organizationUnit -> organizationUnit.afterRead());

					final UploadProcess process = createUploadProcess(operation);
					process.validate(uploadOrganizationUnits);
					process.execute(manager, organization, uploadOrganizationUnits);
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
		case CREATE:

			return new CreateUploadProcess();
		case UPDATE:

			return new UpdateUploadProcess();
		default:

			throw new IllegalArgumentException("Operation: " + operation);
		}
	}

	interface UploadProcess {

		MessagesApi getMessages();

		Class<?>[] getGroups();

		default void validate(final List<OrganizationUnit> uploadCompanies) {

			uploadCompanies.stream().forEach(organizationUnit -> {

				final Set<ConstraintViolation<OrganizationUnit>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(organizationUnit, getGroups());
				if (1 <= violations.size()) {

					final StringBuilder builder = new StringBuilder();
					builder.append(organizationUnit.getNames().getOrDefault(lang().toLocale(), organizationUnit.getNames().get(Locale.US))).append(" - ");
					violations.stream().forEach(violation -> {

						builder.append(violation.getPropertyPath().toString()).append("<br>").append(getMessages().get(lang(), violation.getMessage()));
					});
					throw new IllegalStateException(builder.toString());
				}
			});
		}

		void execute(final EntityManager manager, final Organization organization, final List<OrganizationUnit> uploadCompanies);
	}

	private class CreateUploadProcess implements UploadProcess {

		private final Class<?>[] groups = new Class<?>[] { Create.class };

		@Override
		public MessagesApi getMessages() {

			return messages;
		}

		@Override
		public Class<?>[] getGroups() {

			return groups;
		}

		@Override
		public void execute(final EntityManager manager, final Organization organization, final List<OrganizationUnit> uploadCompanies) {

			final Set<OrganizationUnit> organizationUnits = organization.getOrganizationUnits();
			for (int i = 0; i < uploadCompanies.size(); i++) {

				final OrganizationUnit uploadOrganizationUnit = uploadCompanies.get(i);

				uploadOrganizationUnit.setId(0);
				if (organization.getId() != uploadOrganizationUnit.getOrganizationId()) {

					throw new PlayException(//
							messages.get(lang(), MessageKeys.PROCESS) + " " + messages.get(lang(), MessageKeys.ERROR), //
							messages.get(lang(), MessageKeys.SYSTEM_ERROR_X__DATA_ILLEGAL, uploadOrganizationUnit.getName()));
				}
				uploadOrganizationUnit.setOrganization(organization);
				uploadOrganizationUnit.setUpdateDateTime(null);

				organizationUnits.add(uploadOrganizationUnit);
			}

			manager.merge(organization);
		}
	}

	private class UpdateUploadProcess implements UploadProcess {

		private final Class<?>[] groups = new Class<?>[] { Update.class };

		@Override
		public MessagesApi getMessages() {

			return messages;
		}

		@Override
		public Class<?>[] getGroups() {

			return groups;
		}

		@Override
		public void execute(final EntityManager manager, final Organization organization, final List<OrganizationUnit> uploadCompanies) {

			final Map<Long, OrganizationUnit> idToUnitMap = organization.getIdToUnitMap();
			for (int i = 0; i < uploadCompanies.size(); i++) {

				final OrganizationUnit uploadOrganizationUnit = uploadCompanies.get(i);

				final OrganizationUnit storedOrganizationUnit;
				try {

					storedOrganizationUnit = idToUnitMap.get(uploadOrganizationUnit.getId());
				} catch (final Exception e) {

					throw e;
				}

				final boolean isExist = Objects.nonNull(storedOrganizationUnit);
				if (!isExist) {

					throw new PlayException(//
							messages.get(lang(), MessageKeys.CONSISTENT) + " " + messages.get(lang(), MessageKeys.ERROR), //
							messages.get(lang(), MessageKeys.SYSTEM_ERROR_X_NOTEXIST, messages.get(lang(), MessageKeys.ORGANIZATIONUNIT)) + ": " + uploadOrganizationUnit.getId());
				}

				storedOrganizationUnit.setNames(uploadOrganizationUnit.getNames());
			}

			manager.merge(organization);
		}
	}
}
