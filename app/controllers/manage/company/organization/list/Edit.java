package controllers.manage.company.organization.list;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
import models.base.EntityDao;
import models.company.Company;
import models.company.organization.Organization;
import models.company.organization.OrganizationUnit;
import models.company.organization.OrganizationUnitName;
import models.company.organization.OrganizationUnit_;
import models.manage.company.organization.list.EditFormContent;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.api.PlayException;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import javax.annotation.Nonnull;
import play.i18n.Messages;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

public class Edit extends Controller {

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
	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	@RequireCSRFCheck
	public CompletionStage<Result> create(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final Result result = jpaApi.withTransaction(manager -> {

			final Form<EditFormContent> createForm = formFactory.form(EditFormContent.class, Create.class).bindFromRequest(request);

			if (!createForm.hasErrors()) {

				final EditFormContent createFormContent = createForm.get();

				final long companyId = createFormContent.getCompanyId();
				final Long organizationId = createFormContent.getOrganizationId();

				final Company company = manager.find(Company.class, companyId);
				Organization organization = company.getOrganization();

				if (Objects.nonNull(organizationId)) {

					if (Objects.isNull(organization)) {

						throw new PlayException(//
								messages.at(MessageKeys.CONSISTENT) + " " + messages.at(MessageKeys.ERROR), //
								messages.at(MessageKeys.SYSTEM_ERROR_X_NOTEXIST, messages.at(MessageKeys.ORGANIZATION)));
					}

					final long organizationVersion = Long.valueOf(createFormContent.getOrganizationVersion()).longValue();
					if (organization.getVersion() != organizationVersion) {

						throw new PlayException(//
								messages.at(MessageKeys.CONSISTENT) + " " + messages.at(MessageKeys.ERROR), //
								messages.at(MessageKeys.SYSTEM_ERROR_STATE_NOTCONSISTENT));
					}
				} else {

					if (!Objects.isNull(organization)) {

						throw new PlayException(//
								messages.at(MessageKeys.CONSISTENT) + " " + messages.at(MessageKeys.ERROR), //
								messages.at(MessageKeys.SYSTEM_ERROR_X_EXIST, messages.at(MessageKeys.ORGANIZATION)));
					}

					organization = new Organization();
					organization.setCompany(company);
					organization.setUpdateDateTime(LocalDateTime.now());
					company.setOrganization(organization);
					manager.persist(company);
				}

				final String name = createFormContent.getName();
				final String localName = createFormContent.getLocalName();

				final OrganizationUnit organizationUnit;
				try {

					organizationUnit = new OrganizationUnit();
					organizationUnit.setOrganization(organization);
					organizationUnit.setUpdateDateTime(LocalDateTime.now());
					organizationUnitDao.create(manager, organizationUnit);

					final Map<Locale, OrganizationUnitName> names = new HashMap<>();
					names.put(Locale.US, new OrganizationUnitName(organizationUnit.getId(), Locale.US, name));
					if (!Locale.US.equals(lang.toLocale())) {

						if (localName != null && !localName.isEmpty()) {

							names.put(lang.toLocale(), new OrganizationUnitName(organizationUnit.getId(), lang.toLocale(), localName));
						}
					}
					organizationUnit.setNames(names);

					final Set<OrganizationUnit> organizationUnits = organization.getOrganizationUnits();
					organizationUnits.add(organizationUnit);
					manager.merge(organization);
				} catch (final Exception e) {

					final Form<EditFormContent> failureUpdateForm = formFactory//
							.form(EditFormContent.class)//
							.fill(createFormContent)//
							.withGlobalError(e.getLocalizedMessage());
					return failureEdit(failureUpdateForm, request, lang, messages);
				}

				final ObjectMapper mapper = new ObjectMapper();
				final ObjectNode response = mapper.createObjectNode();
				response.put("status", "success");

				return ok(response);
			} else {

				return failureEdit(createForm, request, lang, messages);
			}
		});

		return CompletableFuture.supplyAsync(() -> {

			return result;
		});
	}

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	@RequireCSRFCheck
	public CompletionStage<Result> update(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final Result result = jpaApi.withTransaction(manager -> {

			final Form<EditFormContent> updateForm = formFactory.form(EditFormContent.class, Update.class).bindFromRequest(request);

			if (!updateForm.hasErrors()) {

				final EditFormContent updateFormContent = updateForm.get();

				final long companyId = updateFormContent.getCompanyId();
				final Long organizationId = updateFormContent.getOrganizationId();

				final Company company = manager.find(Company.class, companyId);
				final Organization organization = company.getOrganization();

				if (Objects.nonNull(organizationId)) {

					if (Objects.isNull(organization)) {

						throw new PlayException(//
								messages.at(MessageKeys.CONSISTENT) + " " + messages.at(MessageKeys.ERROR), //
								messages.at(MessageKeys.SYSTEM_ERROR_X_NOTEXIST, messages.at(MessageKeys.ORGANIZATION)));
					}

					final long organizationVersion = Long.valueOf(updateFormContent.getOrganizationVersion()).longValue();
					if (organization.getVersion() != organizationVersion) {

						throw new PlayException(//
								messages.at(MessageKeys.CONSISTENT) + " " + messages.at(MessageKeys.ERROR), //
								messages.at(MessageKeys.SYSTEM_ERROR_STATE_NOTCONSISTENT));
					}
				} else {

					throw new PlayException(//
							messages.at(MessageKeys.CONSISTENT) + " " + messages.at(MessageKeys.ERROR), //
							messages.at(MessageKeys.SYSTEM_ERROR_X_NOTEXIST, messages.at(MessageKeys.ORGANIZATION)));
				}

				final long id = updateFormContent.getId();
				final String name = updateFormContent.getName();
				final String localName = updateFormContent.getLocalName();
				final Long version = updateFormContent.getVersion();

				final OrganizationUnit organizationUnit;
				try {

					try {

						organizationUnit = organizationUnitDao.read(manager, OrganizationUnit.class, (builder, query) -> {

							final Root<OrganizationUnit> root = query.from(OrganizationUnit.class);
							query.where(builder.and(//
									builder.equal(root.get(OrganizationUnit_.id), id), //
									builder.equal(root.get(OrganizationUnit_.version), version)));
						});
					} catch (final NoResultException e) {

						throw new PlayException(//
								messages.at(MessageKeys.CONSISTENT) + " " + messages.at(MessageKeys.ERROR), //
								messages.at(MessageKeys.SYSTEM_ERROR_STATE_NOTCONSISTENT), e);
					}

					final Map<Locale, OrganizationUnitName> names = organizationUnit.getNames();
					final OrganizationUnitName organizationUnitName = names.get(Locale.US);
					organizationUnitName.setName(name);
					if (!Locale.US.equals(lang.toLocale())) {

						if (localName != null && !localName.isEmpty()) {

							final OrganizationUnitName localOrganizationUnitName;
							if (!names.containsKey(lang.toLocale())) {

								localOrganizationUnitName = new OrganizationUnitName();
								localOrganizationUnitName.setOrganizationUnit_Id(organizationUnit.getId());
								localOrganizationUnitName.setLocale(lang.toLocale());
								localOrganizationUnitName.setName(localName);
								names.put(lang.toLocale(), localOrganizationUnitName);
								manager.persist(localOrganizationUnitName);
							} else {

								localOrganizationUnitName = names.get(lang.toLocale());
								localOrganizationUnitName.setName(localName);
							}
						} else {

							names.remove(lang.toLocale());
						}
					}
					organizationUnit.setUpdateDateTime(LocalDateTime.now());

					final Set<OrganizationUnit> organizationUnits = organization.getOrganizationUnits();
					organizationUnits.add(organizationUnit);
					manager.merge(organization);
				} catch (final Exception e) {

					final Form<EditFormContent> failureUpdateForm = formFactory//
							.form(EditFormContent.class)//
							.fill(updateFormContent)//
							.withGlobalError(e.getLocalizedMessage());
					return failureEdit(failureUpdateForm, request, lang, messages);
				}

				final ObjectMapper mapper = new ObjectMapper();
				final ObjectNode response = mapper.createObjectNode();
				response.put("status", "success");

				return ok(response);
			} else {

				return failureEdit(updateForm, request, lang, messages);
			}
		});

		return CompletableFuture.supplyAsync(() -> {

			return result;
		});
	}

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	@RequireCSRFCheck
	public CompletionStage<Result> delete(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final Result result = jpaApi.withTransaction(manager -> {

			final Form<EditFormContent> deleteForm = formFactory.form(EditFormContent.class, Delete.class).bindFromRequest(request);

			if (!deleteForm.hasErrors()) {

				final EditFormContent deleteFormContent = deleteForm.get();

				final long companyId = deleteFormContent.getCompanyId();
				final Long organizationId = deleteFormContent.getOrganizationId();

				final Company company = manager.find(Company.class, companyId);
				Organization organization = company.getOrganization();

				if (Objects.nonNull(organizationId)) {

					if (Objects.isNull(organization)) {

						throw new PlayException(//
								messages.at(MessageKeys.CONSISTENT) + " " + messages.at(MessageKeys.ERROR), //
								messages.at(MessageKeys.SYSTEM_ERROR_X_NOTEXIST, messages.at(MessageKeys.ORGANIZATION)));
					}

					final long organizationVersion = Long.valueOf(deleteFormContent.getOrganizationVersion()).longValue();
					if (organization.getVersion() != organizationVersion) {

						throw new PlayException(//
								messages.at(MessageKeys.CONSISTENT) + " " + messages.at(MessageKeys.ERROR), //
								messages.at(MessageKeys.SYSTEM_ERROR_STATE_NOTCONSISTENT));
					}
				} else {

					throw new PlayException(//
							messages.at(MessageKeys.CONSISTENT) + " " + messages.at(MessageKeys.ERROR), //
							messages.at(MessageKeys.SYSTEM_ERROR_X_NOTEXIST, messages.at(MessageKeys.ORGANIZATION)));
				}

				final long id = deleteFormContent.getId();
				final Long version = deleteFormContent.getVersion();

				final OrganizationUnit organizationUnit;
				try {

					try {

						organizationUnit = organizationUnitDao.read(manager, OrganizationUnit.class, (builder, query) -> {

							final Root<OrganizationUnit> root = query.from(OrganizationUnit.class);
							query.where(builder.and(//
									builder.equal(root.get(OrganizationUnit_.id), id), //
									builder.equal(root.get(OrganizationUnit_.version), version)));
						});
					} catch (final NoResultException e) {

						throw new PlayException(//
								messages.at(MessageKeys.CONSISTENT) + " " + messages.at(MessageKeys.ERROR), //
								messages.at(MessageKeys.SYSTEM_ERROR_STATE_NOTCONSISTENT), e);
					}

					organization = manager.find(Organization.class, organizationId);
					final Set<OrganizationUnit> organizationUnits = organization.getOrganizationUnits();
					organizationUnits.remove(organizationUnit);
					manager.merge(organization);

					organizationUnit.getChildren().stream().forEach(childUnit -> {

						childUnit.setParent(null);
						childUnit.setSortOrder(null);
						manager.merge(childUnit);
					});

					manager.remove(organizationUnit);
					manager.flush();
					manager.clear();
				} catch (final Exception e) {

					final Form<EditFormContent> failureDeleteForm = formFactory//
							.form(EditFormContent.class)//
							.fill(deleteFormContent)//
							.withGlobalError(e.getLocalizedMessage());
					return failureEdit(failureDeleteForm, request, lang, messages);
				}

				final ObjectMapper mapper = new ObjectMapper();
				final ObjectNode response = mapper.createObjectNode();
				response.put("status", "success");

				return ok(response);
			} else {

				return failureEdit(deleteForm, request, lang, messages);
			}
		});

		return CompletableFuture.supplyAsync(() -> {

			return result;
		});
	}

	private Result failureEdit(final Form<EditFormContent> editForm, final Request request, final Lang lang, final Messages messages) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();
		result.put("status", "error");

		final ArrayNode globalErrorsNode = mapper.createArrayNode();
		editForm.globalErrors().forEach(validationError -> {

			globalErrorsNode.add(messages.at(validationError.message()));
		});
		result.set("globalErrors", globalErrorsNode);

		final ObjectNode errorsNode = mapper.createObjectNode();
		editForm.errors().stream().forEach(error -> {

			final String property = error.key();

			final ArrayNode propertyErrorNode = mapper.createArrayNode();
			error.messages().forEach(message -> propertyErrorNode.add(messages.at(message)));

			errorsNode.set(property, propertyErrorNode);
		});
		result.set("errors", errorsNode);

		return badRequest(result);
	}
}
