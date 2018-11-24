package controllers.manage.company.organization.list;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import models.company.organization.OrganizationUnitName;
import models.manage.company.organization.list.UploadFormContent;
import models.manage.company.organization.list.UploadFormContent.Operation;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.api.PlayException;
import play.api.PlayException.ExceptionSource;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import javax.annotation.Nonnull;
import play.i18n.Messages;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

public class Upload extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private MessagesApi messagesApi;

	@Inject
	private JPAApi jpaApi;

	@Inject
	private FormFactory formFactory;

	private final EntityDao<OrganizationUnit> organizationUnitDao = new EntityDao<OrganizationUnit>() {
	};

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	@BodyParser.Of(value = BodyParser.MultipartFormData.class)
	@RequireCSRFCheck
	public CompletionStage<Result> upload(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final Result result = jpaApi.withTransaction(manager -> {

			final MultipartFormData<File> multipartFormData = request.body().asMultipartFormData();
			final Form<UploadFormContent> uploadForm = formFactory.form(UploadFormContent.class).bindFromRequestData(lang, request.attrs(), multipartFormData.asFormUrlEncoded());

			if (!uploadForm.hasErrors()) {

				final UploadFormContent uploadFormContent = uploadForm.get();

				final long companyId = uploadFormContent.getCompanyId();
				final Long organizationId = uploadFormContent.getOrganizationId();

				final Company company = manager.find(Company.class, companyId);
				Organization organization = company.getOrganization();

				if (Objects.nonNull(organizationId)) {

					if (Objects.isNull(organization)) {

						throw new PlayException(//
								messages.at(MessageKeys.CONSISTENT) + " " + messages.at(MessageKeys.ERROR), //
								messages.at(MessageKeys.SYSTEM_ERROR_X_NOTEXIST, messages.at(MessageKeys.ORGANIZATION)));
					}
				} else {

					if (!Objects.isNull(organization)) {

						throw new PlayException(//
								messages.at(MessageKeys.CONSISTENT) + " " + messages.at(MessageKeys.ERROR), //
								messages.at(MessageKeys.SYSTEM_ERROR_X_EXIST, messages.at(MessageKeys.ORGANIZATION)));
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

						throw new IllegalStateException(messages.at(MessageKeys.ERROR_PATH_EMPTY));
					}
					final Path uploadPath = uploadFilePart.getFile().toPath();

					final String csv = Files.readAllLines(uploadPath, StandardCharsets.UTF_8).stream().collect(Collectors.joining(CsvPreference.STANDARD_PREFERENCE.getEndOfLineSymbols()));
					final List<OrganizationUnit> uploadOrganizationUnits = CSVs.toBeans(OrganizationUnit.getHeaders(), OrganizationUnit.getReadCellProcessors(), csv, OrganizationUnit.class);
					uploadOrganizationUnits.stream().forEach(organizationUnit -> organizationUnit.afterRead(messages));

					final UploadProcess process = createUploadProcess(operation);
					process.validate(uploadOrganizationUnits, lang, messages);
					process.execute(manager, organization, uploadOrganizationUnits, messages);
				} catch (final ExceptionSource e) {

					manager.getTransaction().setRollbackOnly();

					final Form<UploadFormContent> failureUploadForm = formFactory.form(UploadFormContent.class).fill(uploadFormContent);
					failureUploadForm.withGlobalError(messages.at(MessageKeys.SYSTEM_ERROR_X__CASE__DATA_ILLEGAL, e.line()) + ": " + e.getLocalizedMessage());
					return failureUpload(failureUploadForm, request, lang, messages);
				} catch (final Exception e) {

					manager.getTransaction().setRollbackOnly();

					final Form<UploadFormContent> failureUploadForm = formFactory.form(UploadFormContent.class).fill(uploadFormContent);
					failureUploadForm.withGlobalError(e.getLocalizedMessage());
					return failureUpload(failureUploadForm, request, lang, messages);
				}

				final ObjectMapper mapper = new ObjectMapper();
				final ObjectNode response = mapper.createObjectNode();
				response.put("status", "success");

				return ok(response);
			} else {

				return failureUpload(uploadForm, request, lang, messages);
			}
		});

		return CompletableFuture.supplyAsync(() -> {

			return result;
		});
	}

	private Result failureUpload(final Form<UploadFormContent> uploadForm, final Request request, final Lang lang, final Messages messages) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();
		result.put("status", "error");

		final ArrayNode globalErrorsNode = mapper.createArrayNode();
		uploadForm.globalErrors().forEach(validationError -> {

			globalErrorsNode.add(messages.at(validationError.message()));
		});
		result.set("globalErrors", globalErrorsNode);

		final ObjectNode errorsNode = mapper.createObjectNode();
		uploadForm.errors().stream().forEach(error -> {

			final String property = error.key();

			final ArrayNode propertyErrorNode = mapper.createArrayNode();
			error.messages().forEach(message -> propertyErrorNode.add(messages.at(message)));

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

		Class<?>[] getGroups();

		default void validate(final List<OrganizationUnit> uploadCompanies, final Lang lang, final Messages messages) {

			uploadCompanies.stream().forEach(organizationUnit -> {

				final Set<ConstraintViolation<OrganizationUnit>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(organizationUnit, getGroups());
				if (1 <= violations.size()) {

					final StringBuilder builder = new StringBuilder();
					builder.append(organizationUnit.getNames().getOrDefault(lang.toLocale(), organizationUnit.getNames().get(Locale.US))).append(" - ");
					violations.stream().forEach(violation -> {

						builder.append(violation.getPropertyPath().toString()).append("<br>").append(messages.at(violation.getMessage()));
					});
					throw new IllegalStateException(builder.toString());
				}
			});
		}

		void execute(final EntityManager manager, final Organization organization, final List<OrganizationUnit> uploadCompanies, final Messages messages);
	}

	private class CreateUploadProcess implements UploadProcess {

		private final Class<?>[] groups = new Class<?>[] { Create.class };

		@Override
		public Class<?>[] getGroups() {

			return groups;
		}

		@Override
		public void execute(final EntityManager manager, final Organization organization, final List<OrganizationUnit> uploadOrganizationUnits, final Messages messages) {

			for (int i = 0; i < uploadOrganizationUnits.size(); i++) {

				final OrganizationUnit uploadOrganizationUnit = uploadOrganizationUnits.get(i);
				final Map<Locale, OrganizationUnitName> names = uploadOrganizationUnit.getNames();

				try {

					uploadOrganizationUnit.setOrganization(organization);
					uploadOrganizationUnit.setId(0);
					uploadOrganizationUnit.setNames(Collections.emptyMap());
					organizationUnitDao.create(manager, uploadOrganizationUnit);
					names.forEach((locale, name) -> name.setOrganizationUnit_Id(uploadOrganizationUnit.getId()));
					uploadOrganizationUnit.setNames(names);
				} catch (final EntityExistsException e) {

					throw e;
				}
			}
		}
	}

	private class UpdateUploadProcess implements UploadProcess {

		private final Class<?>[] groups = new Class<?>[] { Update.class };

		@Override
		public Class<?>[] getGroups() {

			return groups;
		}

		@Override
		public void execute(final EntityManager manager, final Organization organization, final List<OrganizationUnit> uploadCompanies, final Messages messages) {

			final Map<Long, OrganizationUnit> idToUnitMap = organization.getIdToUnitMap();
			for (int i = 0; i < uploadCompanies.size(); i++) {

				final OrganizationUnit uploadOrganizationUnit = uploadCompanies.get(i);
				final Map<Locale, OrganizationUnitName> names = uploadOrganizationUnit.getNames();

				final OrganizationUnit storedOrganizationUnit;
				try {

					storedOrganizationUnit = idToUnitMap.get(uploadOrganizationUnit.getId());
					final Map<Locale, OrganizationUnitName> storedNames = storedOrganizationUnit.getNames();
					names.forEach((locale, name) -> {

						final OrganizationUnitName storedName = storedNames.get(locale);
						storedName.setName(name.getName());
					});
				} catch (final Exception e) {

					throw e;
				}

				final boolean isExist = Objects.nonNull(storedOrganizationUnit);
				if (!isExist) {

					throw new PlayException(//
							messages.at(MessageKeys.CONSISTENT) + " " + messages.at(MessageKeys.ERROR), //
							messages.at(MessageKeys.SYSTEM_ERROR_X_NOTEXIST, messages.at(MessageKeys.ORGANIZATIONUNIT)) + ": " + uploadOrganizationUnit.getId());
				}
			}

			manager.merge(organization);
		}
	}
}
