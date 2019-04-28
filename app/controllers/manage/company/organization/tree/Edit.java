package controllers.manage.company.organization.tree;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import common.data.validation.groups.Update;
import common.system.MessageKeys;
import models.base.EntityDao;
import models.company.Company;
import models.company.organization.Organization;
import models.company.organization.OrganizationUnit;
import models.manage.company.organization.tree.EditFormContent;
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

	private final EntityDao<Organization> organizationDao = new EntityDao<Organization>() {
	};

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
				final long organizationId = updateFormContent.getOrganizationId();

				final Company company = manager.find(Company.class, companyId);
				final Organization organization = company.getOrganization();

				if (Objects.nonNull(organizationId)) {

					if (Objects.isNull(organization)) {

						throw new PlayException(//
								messages.at(MessageKeys.CONSISTENT) + " " + messages.at(MessageKeys.ERROR), //
								messages.at(MessageKeys.SYSTEM_ERROR_X_NOTEXIST, messages.at(MessageKeys.ORGANIZATION)));
					}

					final long organizationVersion = updateFormContent.getOrganizationVersion().longValue();
					if (organization.getVersion() != organizationVersion) {

						throw new PlayException(//
								messages.at(MessageKeys.CONSISTENT) + " " + messages.at(MessageKeys.ERROR), //
								messages.at(MessageKeys.CONSISTENT) + " " + messages.at(MessageKeys.ERROR));
					}
				} else {

					throw new PlayException(//
							messages.at(MessageKeys.CONSISTENT) + " " + messages.at(MessageKeys.ERROR), //
							messages.at(MessageKeys.SYSTEM_ERROR_X_NOTEXIST, messages.at(MessageKeys.ORGANIZATION)));
				}

				final String unitTreeJSON = updateFormContent.getUnitTreeJSON();

				try {

					final Map<Long, OrganizationUnit> idToUnitMap = organization.getIdToUnitMap();
					final Set<OrganizationUnit> sss = organization.getOrganizationUnits();
					for (final OrganizationUnit organizationUnit : sss) {
						System.out.println(organizationUnit.toString());
					}
					final ObjectMapper mapper = new ObjectMapper();
					final ArrayNode arrayNode = mapper.readValue(unitTreeJSON, ArrayNode.class);
					updateTree(idToUnitMap, null, arrayNode);

					organization.setOrganizationUnits(new HashSet<>(idToUnitMap.values()));
					organizationDao.update(manager, organization);
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

	private static void updateTree(@Nonnull final Map<Long, OrganizationUnit> idToUnitMap, final Long parentId, @Nonnull final ArrayNode arrayNode) {

		for (int i = 0; i < arrayNode.size(); i++) {

			final JsonNode node = arrayNode.get(i);

			final long id = node.get("id").asLong();

			final OrganizationUnit parentUnit = idToUnitMap.get(parentId);
			final OrganizationUnit unit = idToUnitMap.get(id);
			unit.setParent(parentUnit);
			unit.setSortOrder(Long.valueOf(i));

			if (node.has("children")) {

				final ArrayNode childArrayNodes = (ArrayNode) node.get("children");
				updateTree(idToUnitMap, id, childArrayNodes);
			}
		}
	}

	private Result failureEdit(@Nonnull final Form<EditFormContent> editForm, final Request request, final Lang lang, final Messages messages) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();
		result.put("status", "error");

		final ArrayNode globalErrorsNode = mapper.createArrayNode();
		editForm.globalErrors().forEach(validationError -> {

			globalErrorsNode.add(messages.at(validationError.message()));
		});
		result.set("globalErrors", globalErrorsNode);

		return badRequest(result);
	}
}
