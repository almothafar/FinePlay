package controllers.manage.company.organization.list;

import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.utils.Binaries;
import common.utils.CSVs;
import common.utils.DateTimes;
import models.base.EntityDao;
import models.company.Company;
import models.company.organization.Organization;
import models.company.organization.OrganizationUnit;
import models.company.organization.OrganizationUnit_;
import models.manage.company.organization.list.ReadFormContent;
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

	private final EntityDao<OrganizationUnit> organizationUnitDao = new EntityDao<OrganizationUnit>() {
	};

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	@Transactional()
	public Result index(final long companyId) {

		final String companyName = readCompanyName(jpaApi.em(), companyId);

		final ReadFormContent readFormContent = new ReadFormContent();
		readFormContent.setCompanyId(companyId);
		readFormContent.setMaxResult(String.valueOf(1000));

		final List<OrganizationUnit> organizationUnits = readList(jpaApi.em(), readFormContent, 0);

		final Form<ReadFormContent> readForm = formFactory.form(ReadFormContent.class).fill(readFormContent);

		return ok(views.html.manage.company.organization.list.index.render(readForm, companyName, organizationUnits));
	}

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	@Transactional()
	@RequireCSRFCheck
	public Result read() {

		final Form<ReadFormContent> readForm = formFactory.form(ReadFormContent.class).bindFromRequest();
		final long companyId = readForm.value().get().getCompanyId();

		if (!readForm.hasErrors()) {

			final ReadFormContent readFormContent = readForm.get();

			final String companyName = readCompanyName(jpaApi.em(), companyId);

			final List<OrganizationUnit> organizationUnits = readList(jpaApi.em(), readFormContent, 0);

			return ok(views.html.manage.company.organization.list.index.render(readForm, companyName, Collections.unmodifiableList(organizationUnits)));
		} else {

			return failureRead(readForm, companyId);
		}
	}

	private String readCompanyName(final EntityManager manager, final long companyId) {

		final Company company = manager.find(Company.class, companyId);
		final String companyName = company.getNames().getOrDefault(lang().toLocale(), company.getNames().get(Locale.US));

		return companyName;
	}

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	@Transactional()
	@RequireCSRFCheck
	public Result download() {

		final Result result = jpaApi.withTransaction(manager -> {

			final Form<ReadFormContent> downloadForm = formFactory.form(ReadFormContent.class).bindFromRequest();
			final long companyId = downloadForm.value().get().getCompanyId();

			if (!downloadForm.hasErrors()) {

				final ReadFormContent downloadFormContent = downloadForm.get();
				final List<OrganizationUnit> downloadCompanies = readList(manager, downloadFormContent, 0);
				downloadCompanies.stream().forEach(organizationUnit -> organizationUnit.beforeWrite());
				final String csv = CSVs.toCSV(OrganizationUnit.getHeaders(), OrganizationUnit.getWriteCellProcessors(), downloadCompanies);

				response().setHeader(Http.HeaderNames.CONTENT_DISPOSITION, "attachment;filename=organizationUnits.csv");
				return ok(Binaries.concat(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }, csv.getBytes(StandardCharsets.UTF_8))).as(Http.MimeTypes.BINARY);
			} else {

				return failureRead(downloadForm, companyId);
			}
		});

		return result;
	}

	private List<OrganizationUnit> readList(final EntityManager manager, final ReadFormContent readFormContent, int startPosition) {

		final long companyId = readFormContent.getCompanyId();

		final Company company = manager.find(Company.class, companyId);
		final Organization organization = company.getOrganization();

		if (Objects.isNull(organization)) {

			return Collections.emptyList();
		}

		readFormContent.setOrganizationId(organization.getId());
		final LocalDateTime organizationUpdateDateTime = organization.getUpdateDateTime() != null ? DateTimes.getClientDateTime(organization.getUpdateDateTime()) : null;
		readFormContent.setOrganizationUpdateDateTime(organizationUpdateDateTime);

		final String name = readFormContent.getName();
		final int maxResult = Integer.parseInt(readFormContent.getMaxResult());

		return organizationUnitDao.readList(manager, OrganizationUnit.class, (builder, query) -> {

			final Root<OrganizationUnit> root = query.from(OrganizationUnit.class);

			final List<Predicate> predicates = new ArrayList<>();

			predicates.add(builder.equal(root.get(OrganizationUnit_.organization), organization));

			query.where(predicates.toArray(new Predicate[0]));
		}, parameters -> {

			parameters.setFirstResult(startPosition).setMaxResults(maxResult);
		}).stream().filter(unit -> {

			if (Objects.nonNull(name) && !name.isEmpty()) {

				return unit.getNames().values().stream().anyMatch(unitName -> unitName.contains(name));
			} else {

				return true;
			}
		}).collect(Collectors.toList());
	}

	private Result failureRead(final Form<ReadFormContent> searchForm, final long companyId) {

		final String companyName = readCompanyName(jpaApi.em(), companyId);

		final List<OrganizationUnit> organizationUnits = Collections.emptyList();
		return badRequest(views.html.manage.company.organization.list.index.render(searchForm, companyName, organizationUnits));
	}
}
