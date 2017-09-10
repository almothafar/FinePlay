package controllers.manage.company.organization.tree;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import common.system.MessageKeys;
import common.utils.DateTimes;
import play.mvc.Controller;
import models.base.EntityDao;
import models.company.Company;
import models.company.organization.Organization;
import models.company.organization.OrganizationUnit;
import models.company.organization.OrganizationUnit_;
import models.manage.company.organization.tree.ReadFormContent;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.i18n.MessagesApi;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

public class Read extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(Read.class);

	@Inject
	private MessagesApi messages;

	@Inject
	private JPAApi jpaApi;

	@Inject
	private FormFactory formFactory;

	private final EntityDao<Company> companyDao = new EntityDao<Company>() {
	};

	private final EntityDao<OrganizationUnit> organizationUnitDao = new EntityDao<OrganizationUnit>() {
	};

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = {Permission.MANAGE})
	@Transactional()
	public Result index(final long companyId) {

		final String companyName = readCompanyName(jpaApi.em(), companyId);

		final ReadFormContent readFormContent = new ReadFormContent();
		readFormContent.setCompanyId(companyId);

		final String unitTreeJSON = readTree(jpaApi.em(), readFormContent);

		final Form<ReadFormContent> readForm = formFactory.form(ReadFormContent.class).fill(readFormContent);

		return ok(views.html.manage.company.organization.tree.index.render(readForm, companyName, unitTreeJSON));
	}

	private String readCompanyName(final EntityManager manager, final long companyId) {

		final Company company = manager.find(Company.class, companyId);
		final String companyName = company.getNames().getOrDefault(lang().toLocale(), company.getNames().get(Locale.US));

		return companyName;
	}

	private String readTree(@Nonnull final EntityManager manager, @Nonnull final ReadFormContent readFormContent) {

		final long companyId = readFormContent.getCompanyId();

		final Company company = manager.find(Company.class, companyId);
		final Organization organization = company.getOrganization();

		final ObjectMapper mapper = new ObjectMapper();
		final List<OrganizationUnit> rootUnits;
		if (Objects.isNull(organization)) {

			rootUnits = Collections.emptyList();
		} else {

			readFormContent.setOrganizationId(organization.getId());
			final LocalDateTime organizationUpdateDateTime = organization.getUpdateDateTime() != null ? DateTimes.getClientDateTime(organization.getUpdateDateTime()) : null;
			readFormContent.setOrganizationUpdateDateTime(organizationUpdateDateTime);

			rootUnits = readRootList(manager, organization);
		}

		final ArrayNode arrayNode = toArrayNode(mapper, rootUnits);
		if (!(1 <= arrayNode.size())) {

			flash("warning", //
					"<strong>" + messages.get(lang(), MessageKeys.PROCESS) + " " + messages.get(lang(), MessageKeys.WARNING) + "</strong> " + //
							messages.get(lang(), MessageKeys.SYSTEM_ERROR_X_NOTEXIST, messages.get(lang(), MessageKeys.ORGANIZATIONUNIT)));
		}
		try {

			final String unitTreeJSON = mapper.writeValueAsString(arrayNode);
			return unitTreeJSON;
		} catch (final JsonProcessingException e) {

			throw new RuntimeException(messages.get(lang(), MessageKeys.SYSTEM) + " " + messages.get(lang(), MessageKeys.ERROR), e);
		}
	}

	private List<OrganizationUnit> readRootList(@Nonnull final EntityManager manager, @Nonnull final Organization organization) {

		final List<OrganizationUnit> rootUnits = organizationUnitDao.readList(manager, OrganizationUnit.class, (builder, query) -> {

			final Root<OrganizationUnit> root = query.from(OrganizationUnit.class);

			query.select(root).orderBy(builder.asc(root.get(OrganizationUnit_.sortOrder)));

			final List<Predicate> predicates = new ArrayList<>();

			predicates.add(builder.equal(root.get(OrganizationUnit_.organization), organization));
			predicates.add(builder.isNull(root.get(OrganizationUnit_.parent)));

			query.where(predicates.toArray(new Predicate[0]));
		});

		rootUnits.stream().forEach(rootUnit -> manager.refresh(rootUnit));

		return rootUnits;
	}

	private static ArrayNode toArrayNode(@Nonnull final ObjectMapper mapper, @Nonnull final List<OrganizationUnit> units) {

		final ArrayNode arrayNode = mapper.createArrayNode();
		units.stream().forEach(unit -> {

			final ObjectNode objectNode = mapper.createObjectNode();
			objectNode.put("id", unit.getId());
			objectNode.put("name", unit.getNames().getOrDefault(lang().toLocale(), unit.getNames().get(Locale.US)));
			arrayNode.add(objectNode);

			final List<OrganizationUnit> children = unit.getChildren();
			children.size();
			if (!children.isEmpty()) {

				final ArrayNode childArrayNodes = toArrayNode(mapper, unit.getChildren());
				objectNode.withArray("children").addAll(childArrayNodes);
			}
		});

		return arrayNode;
	}
}
