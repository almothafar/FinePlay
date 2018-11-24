package apis.company.organization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.base.EntityDao;
import models.company.organization.OrganizationUnit_;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.db.jpa.JPAApi;
import play.mvc.Controller;
import play.mvc.Result;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

public class Organization extends Controller {

	@Inject
	private MessagesApi messagesApi;

	@Inject
	private JPAApi jpaApi;

	private final EntityDao<models.company.organization.OrganizationUnit> organizationUnitDao = new EntityDao<models.company.organization.OrganizationUnit>() {
	};

	@PermissionsAllowed(value = { Permission.MANAGE })
	@Authenticated(common.core.Authenticator.class)
	public Result organizationUnits(@Nonnull final Request request, final long companyId, @Nonnull final String name, final int pageIndex, final int pageSize) {

		final Messages messages = messagesApi.preferred(request);
		messages.lang();

		final Result result = jpaApi.withTransaction(manager -> {

			final String organizationUnitName = name;

			final long totalCount = this.count(manager, companyId, organizationUnitName);

			if (0 == totalCount) {

				return createResult(totalCount, Collections.emptyList());
			}

			final int pageStart = pageIndex * pageSize;

			final List<models.company.organization.OrganizationUnit> organizationUnits = this.readList(manager, companyId, organizationUnitName, pageStart, pageSize);

			return createResult(totalCount, organizationUnits);
		});

		return result;
	}

	private long count(@Nonnull final EntityManager manager, final long companyId, @Nonnull final String name) {

		final models.company.Company company = manager.find(models.company.Company.class, companyId);
		if (Objects.isNull(company)) {

			return 0;
		}

		final models.company.organization.Organization organization = company.getOrganization();
		if (Objects.isNull(organization)) {

			return 0;
		}

		final int maxResult = 1000;

		return organizationUnitDao.count(manager, models.company.organization.OrganizationUnit.class, (builder, query) -> {

			final Root<models.company.organization.OrganizationUnit> root = query.from(models.company.organization.OrganizationUnit.class);

			query.select(builder.count(root));

			final List<Predicate> predicates = new ArrayList<>();

			predicates.add(builder.equal(root.get(OrganizationUnit_.organization), organization));

			if (Objects.nonNull(name) && !name.isEmpty()) {

				final Join<models.company.organization.OrganizationUnit, models.company.organization.OrganizationUnitName> namesJoin = root.join(models.company.organization.OrganizationUnit_.names);
				predicates.add(builder.like(namesJoin.get(models.company.organization.OrganizationUnitName_.name), "%" + name + "%"));
			}

			query.where(predicates.toArray(new Predicate[0])).distinct(true);
		}, parameters -> {

			parameters.setFirstResult(0).setMaxResults(maxResult);
		});
	}

	private List<models.company.organization.OrganizationUnit> readList(@Nonnull final EntityManager manager, final long companyId, @Nonnull final String name, final int pageStart, final int pageSize) {

		return organizationUnitDao.readList(manager, models.company.organization.OrganizationUnit.class, (builder, query) -> {

			final Root<models.company.organization.OrganizationUnit> root = query.from(models.company.organization.OrganizationUnit.class);

			final List<Predicate> predicates = new ArrayList<>();

			if (Objects.nonNull(name) && !name.isEmpty()) {

				final Join<models.company.organization.OrganizationUnit, models.company.organization.OrganizationUnitName> namesJoin = root.join(models.company.organization.OrganizationUnit_.names);
				predicates.add(builder.like(namesJoin.get(models.company.organization.OrganizationUnitName_.name), "%" + name + "%"));
			}

			query.where(predicates.toArray(new Predicate[0])).distinct(true);
		}, parameters -> {

			parameters.setFirstResult(pageStart).setMaxResults(pageSize);
		});
	}

	private Result createResult(final long totalCount, @Nonnull final List<models.company.organization.OrganizationUnit> organizationUnits) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		result.put("totalCount", totalCount);

		final ArrayNode organizationUnitsNode = result.putArray("organizationUnits");
		organizationUnits.forEach(organizationUnit -> {

			final ObjectNode organizationUnitNode = organizationUnitsNode.addObject();

			organizationUnitNode.put("id", organizationUnit.getId());

			final ObjectNode namesNode = mapper.createObjectNode();
			organizationUnit.getNames().forEach((locale, name) -> {

				namesNode.put(locale.toLanguageTag(), name.getName());
			});
			organizationUnitNode.set("names", namesNode);
		});

		return ok(result);
	}
}
