package controllers.manage.company;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import common.data.validation.groups.Create;
import common.data.validation.groups.Delete;
import common.data.validation.groups.Update;
import common.system.MessageKeys;
import common.utils.DateTimes;
import models.base.EntityDao;
import models.company.Company;
import models.company.Company_;
import models.company.CompanyName;
import models.manage.company.EditFormContent;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.api.PlayException;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.MessagesApi;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

public class Edit extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private MessagesApi messages;

	@Inject
	private JPAApi jpaApi;

	@Inject
	private FormFactory formFactory;

	private final EntityDao<Company> companyDao = new EntityDao<Company>() {
	};

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	@Transactional()
	@RequireCSRFCheck
	public CompletionStage<Result> create() {

		final Result result = jpaApi.withTransaction(manager -> {

			final Form<EditFormContent> createForm = formFactory.form(EditFormContent.class, Create.class).bindFromRequest();

			if (!createForm.hasErrors()) {

				final EditFormContent createFormContent = createForm.get();

				final String name = createFormContent.getName();
				final String localName = createFormContent.getLocalName();

				final Company company;
				try {

					company = new Company();
					companyDao.create(manager, company);

					final Map<Locale, CompanyName> names = new HashMap<>();
					names.put(Locale.US, new CompanyName(company.getId(), Locale.US, name));
					if (!Locale.US.equals(lang().toLocale())) {

						if (localName != null && !localName.isEmpty()) {

							names.put(lang().toLocale(), new CompanyName(company.getId(), lang().toLocale(), localName));
						}
					}
					company.setNames(names);
				} catch (final Exception e) {

					final Form<EditFormContent> failureUpdateForm = formFactory.form(EditFormContent.class).fill(createFormContent);
					failureUpdateForm.withGlobalError(e.getLocalizedMessage());
					return failureEdit(failureUpdateForm);
				}

				final ObjectMapper mapper = new ObjectMapper();
				final ObjectNode response = mapper.createObjectNode();
				response.put("status", "success");

				return ok(response);
			} else {

				return failureEdit(createForm);
			}
		});

		return CompletableFuture.supplyAsync(() -> {

			return result;
		});
	}

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	@Transactional()
	@RequireCSRFCheck
	public CompletionStage<Result> update() {

		final Result result = jpaApi.withTransaction(manager -> {

			final Form<EditFormContent> updateForm = formFactory.form(EditFormContent.class, Update.class).bindFromRequest();

			if (!updateForm.hasErrors()) {

				final EditFormContent updateFormContent = updateForm.get();

				final long id = updateFormContent.getId();
				final String name = updateFormContent.getName();
				final String localName = updateFormContent.getLocalName();
				final LocalDateTime updateDateTime = updateFormContent.getUpdateDateTime();

				final Company company;
				try {

					try {

						company = companyDao.read(manager, Company.class, (builder, query) -> {

							final LocalDateTime serverDateTime = DateTimes.getServerDateTime(updateDateTime);

							final Root<Company> root = query.from(Company.class);
							query.where(builder.and(//
									builder.equal(root.get(Company_.id), id)));
						});
					} catch (final NoResultException e) {

						throw new PlayException(//
								messages.get(lang(), MessageKeys.CONSISTENT) + " " + messages.get(lang(), MessageKeys.ERROR), //
								messages.get(lang(), MessageKeys.SYSTEM_ERROR_STATE_NOTCONSISTENT), //
								e);
					}

					final Map<Locale, CompanyName> names = company.getNames();
					final CompanyName companyName = names.get(Locale.US);
					companyName.setName(name);
					if (!Locale.US.equals(lang().toLocale())) {

						if (localName != null && !localName.isEmpty()) {

							final CompanyName localCompanyName;
							if(!names.containsKey(lang().toLocale())) {

								localCompanyName = new CompanyName();
								localCompanyName.setCompany_Id(company.getId());
								localCompanyName.setLocale(lang().toLocale());
								localCompanyName.setName(localName);
								names.put(lang().toLocale(), localCompanyName);
								manager.persist(localCompanyName);
							}else {

								localCompanyName = names.get(lang().toLocale());
								localCompanyName.setName(localName);
							}
						} else {

							names.remove(lang().toLocale());
						}
					}

					companyDao.update(manager, company);
				} catch (final Exception e) {

					final Form<EditFormContent> failureUpdateForm = formFactory.form(EditFormContent.class).fill(updateFormContent);
					failureUpdateForm.withGlobalError(e.getLocalizedMessage());
					return failureEdit(failureUpdateForm);
				}

				final ObjectMapper mapper = new ObjectMapper();
				final ObjectNode response = mapper.createObjectNode();
				response.put("status", "success");

				return ok(response);
			} else {

				return failureEdit(updateForm);
			}
		});

		return CompletableFuture.supplyAsync(() -> {

			return result;
		});
	}

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	@Transactional()
	@RequireCSRFCheck
	public CompletionStage<Result> delete() {

		final Result result = jpaApi.withTransaction(manager -> {

			final Form<EditFormContent> deleteForm = formFactory.form(EditFormContent.class, Delete.class).bindFromRequest();

			if (!deleteForm.hasErrors()) {

				final EditFormContent deleteFormContent = deleteForm.get();

				final long id = deleteFormContent.getId();
				final LocalDateTime updateDateTime = deleteFormContent.getUpdateDateTime();

				final Company company;
				try {

					try {

						company = companyDao.read(manager, Company.class, (builder, query) -> {

							final LocalDateTime serverDateTime = DateTimes.getServerDateTime(updateDateTime);

							final Root<Company> root = query.from(Company.class);
							query.where(builder.and(//
									builder.equal(root.get(Company_.id), id)));
						});
					} catch (final NoResultException e) {

						throw new PlayException(//
								messages.get(lang(), MessageKeys.CONSISTENT) + " " + messages.get(lang(), MessageKeys.ERROR), //
								messages.get(lang(), MessageKeys.SYSTEM_ERROR_STATE_NOTCONSISTENT), //
								e);
					}

					companyDao.delete(manager, company);
				} catch (final Exception e) {

					final Form<EditFormContent> failureDeleteForm = formFactory.form(EditFormContent.class).fill(deleteFormContent);
					failureDeleteForm.withGlobalError(e.getLocalizedMessage());
					return failureEdit(failureDeleteForm);
				}

				final ObjectMapper mapper = new ObjectMapper();
				final ObjectNode response = mapper.createObjectNode();
				response.put("status", "success");

				return ok(response);
			} else {

				return failureEdit(deleteForm);
			}
		});

		return CompletableFuture.supplyAsync(() -> {

			return result;
		});
	}

	private Result failureEdit(final Form<EditFormContent> editForm) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();
		result.put("status", "error");

		final ArrayNode globalErrorsNode = mapper.createArrayNode();
		editForm.globalErrors().forEach(validationError -> {

			globalErrorsNode.add(messages.get(lang(), validationError.message()));
		});
		result.set("globalErrors", globalErrorsNode);

		final ObjectNode errorsNode = mapper.createObjectNode();
		editForm.errors().stream().forEach(error -> {

			final String property = error.key();

			final ArrayNode propertyErrorNode = mapper.createArrayNode();
			error.messages().forEach(message -> propertyErrorNode.add(messages.get(lang(), message)));

			errorsNode.set(property, propertyErrorNode);
		});
		result.set("errors", errorsNode);

		return badRequest(result);
	}
}
