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
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Messages;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import javax.annotation.Nonnull;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

public class Read extends Controller {

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
	public Result index(@Nonnull final Request request, final long companyId) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return jpaApi.withTransaction(manager -> {

			final String companyName = readCompanyName(manager, companyId, lang.toLocale());

			final ReadFormContent readFormContent = new ReadFormContent();
			readFormContent.setCompanyId(companyId);
			readFormContent.setMaxResult(String.valueOf(1000));

			final List<OrganizationUnit> organizationUnits = readList(request, manager, readFormContent, 0);

			final Form<ReadFormContent> readForm = formFactory.form(ReadFormContent.class).fill(readFormContent);

			return ok(views.html.manage.company.organization.list.index.render(readForm, companyName, organizationUnits, request, lang, messages));
		});
	}

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	@RequireCSRFCheck
	public Result read(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return jpaApi.withTransaction(manager -> {

			final Form<ReadFormContent> readForm = formFactory.form(ReadFormContent.class).bindFromRequest(request);
			final long companyId = readForm.value().get().getCompanyId();

			if (!readForm.hasErrors()) {

				final ReadFormContent readFormContent = readForm.get();

				final String companyName = readCompanyName(manager, companyId, lang.toLocale());

				final List<OrganizationUnit> organizationUnits = readList(request, manager, readFormContent, 0);

				return ok(views.html.manage.company.organization.list.index.render(readForm, companyName, Collections.unmodifiableList(organizationUnits), request, lang, messages));
			} else {

				return failureRead(readForm, companyId, request, lang, messages);
			}
		});
	}

	private String readCompanyName(final EntityManager manager, final long companyId, final Locale locale) {

		final Company company = manager.find(Company.class, companyId);
		final String companyName = company.getNames().getOrDefault(locale, company.getNames().get(Locale.US)).getName();

		return companyName;
	}

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	@RequireCSRFCheck
	public Result download(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final Result result = jpaApi.withTransaction(manager -> {

			final Form<ReadFormContent> downloadForm = formFactory.form(ReadFormContent.class).bindFromRequest(request);
			final long companyId = downloadForm.value().get().getCompanyId();

			if (!downloadForm.hasErrors()) {

				final ReadFormContent downloadFormContent = downloadForm.get();
				final List<OrganizationUnit> downloadCompanies = readList(request, manager, downloadFormContent, 0);
				downloadCompanies.stream().forEach(organizationUnit -> organizationUnit.beforeWrite(messages));
				final String csv = CSVs.toCSV(OrganizationUnit.getHeaders(), OrganizationUnit.getWriteCellProcessors(), downloadCompanies);

				return ok(Binaries.concat(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }, csv.getBytes(StandardCharsets.UTF_8)))//
						.as(Http.MimeTypes.BINARY)//
						.withHeader(Http.HeaderNames.CONTENT_DISPOSITION, "attachment;filename=organizationUnits.csv");
			} else {

				return failureRead(downloadForm, companyId, request, lang, messages);
			}
		});

		return result;
	}

	private List<OrganizationUnit> readList(@Nonnull final Request request, final EntityManager manager, final ReadFormContent readFormContent, int startPosition) {

		final long companyId = readFormContent.getCompanyId();

		final Company company = manager.find(Company.class, companyId);
		final Organization organization = company.getOrganization();

		if (Objects.isNull(organization)) {

			return Collections.emptyList();
		}

		readFormContent.setOrganizationId(organization.getId());
		final LocalDateTime organizationVersion = organization.getVersion();
		readFormContent.setOrganizationVersion(organizationVersion);

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

				return unit.getNames().values().stream().anyMatch(unitName -> unitName.getName().contains(name));
			} else {

				return true;
			}
		}).collect(Collectors.toList());
	}

	private Result failureRead(final Form<ReadFormContent> searchForm, final long companyId, final Request request, final Lang lang, final Messages messages) {

		return jpaApi.withTransaction(manager -> {

			final String companyName = readCompanyName(manager, companyId, lang.toLocale());

			final List<OrganizationUnit> organizationUnits = Collections.emptyList();
			return badRequest(views.html.manage.company.organization.list.index.render(searchForm, companyName, organizationUnits, request, lang, messages));
		});
	}
}
