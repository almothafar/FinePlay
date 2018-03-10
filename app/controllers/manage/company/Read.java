package controllers.manage.company;

import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.utils.Binaries;
import common.utils.CSVs;
import models.base.EntityDao;
import models.company.Company;
import models.manage.company.ReadFormContent;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

public class Read extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private JPAApi jpaApi;

	@Inject
	private FormFactory formFactory;

	private final EntityDao<Company> companyDao = new EntityDao<Company>() {
	};

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	@Transactional()
	public Result index() {

		final ReadFormContent readFormContent = new ReadFormContent();
		readFormContent.setMaxResult(String.valueOf(1000));

		final List<Company> companies = readList(jpaApi.em(), readFormContent, 0);

		final Form<ReadFormContent> readForm = formFactory.form(ReadFormContent.class).fill(readFormContent);

		return ok(views.html.manage.company.index.render(readForm, companies));
	}

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	@Transactional()
	@RequireCSRFCheck
	public Result read() {

		final Form<ReadFormContent> readForm = formFactory.form(ReadFormContent.class).bindFromRequest();
		if (!readForm.hasErrors()) {

			final ReadFormContent readFormContent = readForm.get();

			final List<Company> companies = readList(jpaApi.em(), readFormContent, 0);

			return ok(views.html.manage.company.index.render(readForm, Collections.unmodifiableList(companies)));
		} else {

			return failureRead(readForm);
		}
	}

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	@Transactional()
	@RequireCSRFCheck
	public Result download() {

		final Result result = jpaApi.withTransaction(manager -> {

			final Form<ReadFormContent> downloadForm = formFactory.form(ReadFormContent.class).bindFromRequest();

			if (!downloadForm.hasErrors()) {

				final ReadFormContent downloadFormContent = downloadForm.get();
				final List<Company> downloadCompanies = readList(manager, downloadFormContent, 0);
				downloadCompanies.stream().forEach(company -> company.beforeWrite());
				final String csv = CSVs.toCSV(Company.getHeaders(), Company.getWriteCellProcessors(), downloadCompanies);

				response().setHeader(Http.HeaderNames.CONTENT_DISPOSITION, "attachment;filename=companies.csv");
				return ok(Binaries.concat(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }, csv.getBytes(StandardCharsets.UTF_8))).as(Http.MimeTypes.BINARY);
			} else {

				return failureRead(downloadForm);
			}
		});

		return result;
	}

	private List<Company> readList(final EntityManager manager, final ReadFormContent readFormContent, int startPosition) {

		final String name = readFormContent.getName();
		final int maxResult = Integer.parseInt(readFormContent.getMaxResult());

		return companyDao.readList(manager, Company.class, (builder, query) -> {

			final Root<Company> root = query.from(Company.class);

			final List<Predicate> predicates = new ArrayList<>();

			if (Objects.nonNull(name) && !name.isEmpty()) {

				final MapJoin<String, Locale, String> namesJoin = root.joinMap(Company.NAMES);
				predicates.add(namesJoin.in(readNameList(manager, name)));
			}

			query.where(predicates.toArray(new Predicate[0])).distinct(true);
		}, parameters -> {

			parameters.setFirstResult(startPosition).setMaxResults(maxResult);
		});
	}

	private List<String> readNameList(final EntityManager manager, final String name) {

		// Relation mapping is better.
		final Query nativeQuery = manager.createNativeQuery("SELECT NAME FROM COMPANY_NAMES WHERE NAME LIKE ?");
		nativeQuery.setParameter(1, "%" + name + "%");

		@SuppressWarnings("unchecked")
		final List<String> list = nativeQuery.getResultList();
		return list;
	}

	private Result failureRead(final Form<ReadFormContent> searchForm) {

		final List<Company> companies = Collections.emptyList();
		return badRequest(views.html.manage.company.index.render(searchForm, companies));
	}
}
