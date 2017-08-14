package apis.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.mvc.Controller;
import models.base.EntityDao;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

public class Company extends Controller {

	@Inject
	private JPAApi jpaApi;

	private final EntityDao<models.company.Company> companyDao = new EntityDao<models.company.Company>() {
	};

	@Transactional(readOnly = true)
	@PermissionsAllowed(value = {Permission.MANAGE})
	@Authenticated(common.core.Authenticator.class)
	public Result companies(@Nonnull final String name, final int pageIndex, final int pageSize) {

		final Result result = jpaApi.withTransaction(manager -> {

			final String companyName = name;

			final long totalCount = this.count(manager, companyName);

			final int pageStart = pageIndex * pageSize;

			final List<models.company.Company> companies = this.readList(manager, companyName, pageStart, pageSize);

			return createResult(totalCount, companies);
		});

		return result;
	}

	private long count(@Nonnull final EntityManager manager, @Nonnull final String name) {

		final int maxResult = 1000;

		return companyDao.count(manager, models.company.Company.class, (builder, query) -> {

			final Root<models.company.Company> root = query.from(models.company.Company.class);

			query.select(builder.count(root));

			final List<Predicate> predicates = new ArrayList<>();

			if (Objects.nonNull(name) && !name.isEmpty()) {

				final MapJoin<String, Locale, String> namesJoin = root.joinMap(models.company.Company.NAMES);
				predicates.add(namesJoin.in(readNameList(manager, name)));
			}

			query.where(predicates.toArray(new Predicate[0])).distinct(true);
		}, parameters -> {

			parameters.setFirstResult(0).setMaxResults(maxResult);
		});
	}

	private List<models.company.Company> readList(@Nonnull final EntityManager manager, @Nonnull final String name, final int pageStart, final int pageSize) {

		return companyDao.readList(manager, models.company.Company.class, (builder, query) -> {

			final Root<models.company.Company> root = query.from(models.company.Company.class);

			final List<Predicate> predicates = new ArrayList<>();

			if (Objects.nonNull(name) && !name.isEmpty()) {

				final MapJoin<String, Locale, String> namesJoin = root.joinMap(models.company.Company.NAMES);
				predicates.add(namesJoin.in(readNameList(manager, name)));
			}

			query.where(predicates.toArray(new Predicate[0])).distinct(true);
		}, parameters -> {

			parameters.setFirstResult(pageStart).setMaxResults(pageSize);
		});
	}

	private List<String> readNameList(@Nonnull final EntityManager manager, @Nonnull final String name) {

		final Query nativeQuery = manager.createNativeQuery("SELECT NAME FROM COMPANY_NAMES WHERE NAME LIKE ?");
		nativeQuery.setParameter(1, "%" + name + "%");

		@SuppressWarnings("unchecked")
		final List<String> list = nativeQuery.getResultList();
		return list;
	}

	private Result createResult(final long totalCount, @Nonnull final List<models.company.Company> companies) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		result.put("totalCount", totalCount);

		final ArrayNode companiesNode = result.putArray("companies");
		companies.forEach(company -> {

			final ObjectNode companyNode = companiesNode.addObject();

			companyNode.put("id", company.getId());

			final ObjectNode namesNode = mapper.createObjectNode();
			company.getNames().forEach((locale, name) -> {

				namesNode.put(locale.toLanguageTag(), name);
			});
			companyNode.set("names", namesNode);
		});

		return ok(result);
	}
}
