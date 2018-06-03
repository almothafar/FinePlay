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
import common.utils.DateTimes;
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

	private final EntityDao<OrganizationUnit> organizationUnitDao = new EntityDao<OrganizationUnit>() {
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

				final long companyId = createFormContent.getCompanyId();
				final Long organizationId = createFormContent.getOrganizationId();

				final Company company = manager.find(Company.class, companyId);
				Organization organization = company.getOrganization();

				if (Objects.nonNull(organizationId)) {

					if (Objects.isNull(organization)) {

						throw new PlayException(//
								messages.get(lang(), MessageKeys.CONSISTENT) + " " + messages.get(lang(), MessageKeys.ERROR), //
								messages.get(lang(), MessageKeys.SYSTEM_ERROR_X_NOTEXIST, messages.get(lang(), MessageKeys.ORGANIZATION)));
					}

					final LocalDateTime organizationUpdateServerDateTime = DateTimes.getServerDateTime(LocalDateTime.parse(createFormContent.getOrganizationUpdateDateTime()));
					if (!organization.getUpdateDateTime().isEqual(organizationUpdateServerDateTime)) {

						throw new PlayException(//
								messages.get(lang(), MessageKeys.CONSISTENT) + " " + messages.get(lang(), MessageKeys.ERROR), //
								messages.get(lang(), MessageKeys.SYSTEM_ERROR_STATE_NOTCONSISTENT));
					}
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

				final String name = createFormContent.getName();
				final String localName = createFormContent.getLocalName();

				final OrganizationUnit organizationUnit;
				try {

					organizationUnit = new OrganizationUnit();
					organizationUnit.setOrganization(organization);
					organizationUnitDao.create(manager, organizationUnit);

					final Map<Locale, OrganizationUnitName> names = new HashMap<>();
					names.put(Locale.US, new OrganizationUnitName(organizationUnit.getId(), Locale.US, name));
					if (!Locale.US.equals(lang().toLocale())) {

						if (localName != null && !localName.isEmpty()) {

							names.put(lang().toLocale(), new OrganizationUnitName(organizationUnit.getId(), lang().toLocale(), localName));
						}
					}
					organizationUnit.setNames(names);

					final Set<OrganizationUnit> organizationUnits = organization.getOrganizationUnits();
					organizationUnits.add(organizationUnit);
					manager.merge(organization);
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

				final long companyId = updateFormContent.getCompanyId();
				final Long organizationId = updateFormContent.getOrganizationId();

				final Company company = manager.find(Company.class, companyId);
				final Organization organization = company.getOrganization();

				if (Objects.nonNull(organizationId)) {

					if (Objects.isNull(organization)) {

						throw new PlayException(//
								messages.get(lang(), MessageKeys.CONSISTENT) + " " + messages.get(lang(), MessageKeys.ERROR), //
								messages.get(lang(), MessageKeys.SYSTEM_ERROR_X_NOTEXIST, messages.get(lang(), MessageKeys.ORGANIZATION)));
					}

					final LocalDateTime organizationUpdateServerDateTime = DateTimes.getServerDateTime(LocalDateTime.parse(updateFormContent.getOrganizationUpdateDateTime()));
					if (!organization.getUpdateDateTime().isEqual(organizationUpdateServerDateTime)) {

						throw new PlayException(//
								messages.get(lang(), MessageKeys.CONSISTENT) + " " + messages.get(lang(), MessageKeys.ERROR), //
								messages.get(lang(), MessageKeys.SYSTEM_ERROR_STATE_NOTCONSISTENT));
					}
				} else {

					throw new PlayException(//
							messages.get(lang(), MessageKeys.CONSISTENT) + " " + messages.get(lang(), MessageKeys.ERROR), //
							messages.get(lang(), MessageKeys.SYSTEM_ERROR_X_NOTEXIST, messages.get(lang(), MessageKeys.ORGANIZATION)));
				}

				final long id = updateFormContent.getId();
				final String name = updateFormContent.getName();
				final String localName = updateFormContent.getLocalName();
				final LocalDateTime updateDateTime = updateFormContent.getUpdateDateTime();

				final OrganizationUnit organizationUnit;
				try {

					try {

						organizationUnit = organizationUnitDao.read(manager, OrganizationUnit.class, (builder, query) -> {

							final LocalDateTime serverDateTime = DateTimes.getServerDateTime(updateDateTime);

							final Root<OrganizationUnit> root = query.from(OrganizationUnit.class);
							query.where(builder.and(//
									builder.equal(root.get(OrganizationUnit_.id), id)));
						});
					} catch (final NoResultException e) {

						throw new PlayException(//
								messages.get(lang(), MessageKeys.CONSISTENT) + " " + messages.get(lang(), MessageKeys.ERROR), //
								messages.get(lang(), MessageKeys.SYSTEM_ERROR_STATE_NOTCONSISTENT), e);
					}

					final Map<Locale, OrganizationUnitName> names = organizationUnit.getNames();
					final OrganizationUnitName organizationUnitName = names.get(Locale.US);
					organizationUnitName.setName(name);
					if (!Locale.US.equals(lang().toLocale())) {

						if (localName != null && !localName.isEmpty()) {

							final OrganizationUnitName localOrganizationUnitName;
							if(!names.containsKey(lang().toLocale())) {

								localOrganizationUnitName = new OrganizationUnitName();
								localOrganizationUnitName.setOrganizationUnit_Id(organizationUnit.getId());
								localOrganizationUnitName.setLocale(lang().toLocale());
								localOrganizationUnitName.setName(localName);
								names.put(lang().toLocale(), localOrganizationUnitName);
								manager.persist(localOrganizationUnitName);
							}else {

								localOrganizationUnitName = names.get(lang().toLocale());
								localOrganizationUnitName.setName(localName);
							}
						} else {

							names.remove(lang().toLocale());
						}
					}

					final Set<OrganizationUnit> organizationUnits = organization.getOrganizationUnits();
					organizationUnits.add(organizationUnit);
					manager.merge(organization);
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

				final long companyId = deleteFormContent.getCompanyId();
				final Long organizationId = deleteFormContent.getOrganizationId();

				final Company company = manager.find(Company.class, companyId);
				Organization organization = company.getOrganization();

				if (Objects.nonNull(organizationId)) {

					if (Objects.isNull(organization)) {

						throw new PlayException(//
								messages.get(lang(), MessageKeys.CONSISTENT) + " " + messages.get(lang(), MessageKeys.ERROR), //
								messages.get(lang(), MessageKeys.SYSTEM_ERROR_X_NOTEXIST, messages.get(lang(), MessageKeys.ORGANIZATION)));
					}

					final LocalDateTime organizationUpdateServerDateTime = DateTimes.getServerDateTime(LocalDateTime.parse(deleteFormContent.getOrganizationUpdateDateTime()));
					if (!organization.getUpdateDateTime().isEqual(organizationUpdateServerDateTime)) {

						throw new PlayException(//
								messages.get(lang(), MessageKeys.CONSISTENT) + " " + messages.get(lang(), MessageKeys.ERROR), //
								messages.get(lang(), MessageKeys.SYSTEM_ERROR_STATE_NOTCONSISTENT));
					}
				} else {

					throw new PlayException(//
							messages.get(lang(), MessageKeys.CONSISTENT) + " " + messages.get(lang(), MessageKeys.ERROR), //
							messages.get(lang(), MessageKeys.SYSTEM_ERROR_X_NOTEXIST, messages.get(lang(), MessageKeys.ORGANIZATION)));
				}

				final long id = deleteFormContent.getId();
				final LocalDateTime updateDateTime = deleteFormContent.getUpdateDateTime();

				final OrganizationUnit organizationUnit;
				try {

					try {

						organizationUnit = organizationUnitDao.read(manager, OrganizationUnit.class, (builder, query) -> {

							final LocalDateTime serverDateTime = DateTimes.getServerDateTime(updateDateTime);

							final Root<OrganizationUnit> root = query.from(OrganizationUnit.class);
							query.where(builder.and(//
									builder.equal(root.get(OrganizationUnit_.id), id)));
						});
					} catch (final NoResultException e) {

						throw new PlayException(//
								messages.get(lang(), MessageKeys.CONSISTENT) + " " + messages.get(lang(), MessageKeys.ERROR), //
								messages.get(lang(), MessageKeys.SYSTEM_ERROR_STATE_NOTCONSISTENT), e);
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
		editForm.allErrors().stream().forEach(error -> {

			final String property = error.key();

			final ArrayNode propertyErrorNode = mapper.createArrayNode();
			error.messages().forEach(message -> propertyErrorNode.add(messages.get(lang(), message)));

			errorsNode.set(property, propertyErrorNode);
		});
		result.set("errors", errorsNode);

		return badRequest(result);
	}
}
