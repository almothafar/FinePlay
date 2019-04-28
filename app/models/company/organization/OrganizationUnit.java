package models.company.organization;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.ift.CellProcessor;

import common.system.MessageKeys;
import org.supercsv.cellprocessor.time.FmtLocalDateTime;
import org.supercsv.cellprocessor.time.ParseLocalDateTime;
import play.i18n.Messages;
import play.i18n.MessagesApi;

@Entity
@Table(name = "ORGANIZATION_UNITS", //
		uniqueConstraints = { @UniqueConstraint(columnNames = { OrganizationUnit_.ID, "ORGANIZATION_ID" }) }, //
		indexes = { @Index(columnList = OrganizationUnit_.ID), @Index(columnList = "ORGANIZATION_ID") })
public class OrganizationUnit {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final int NAME_COUNT_MAX = 2;

	@Inject
	@Transient
	private MessagesApi messagesApi;

	@Id
	@GeneratedValue
	@Column(nullable = false)
	// Play
	private long id;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@PrimaryKeyJoinColumn(name = OrganizationUnitName_.ORGANIZATION_UNIT__ID)
	@MapKeyColumn(name = OrganizationUnitName_.LOCALE)
	@Size(min = 1, message = MessageKeys.JAVA_ERROR_SIZE)
	private Map<Locale, OrganizationUnitName> names;

	@OneToMany(fetch = FetchType.LAZY/* , cascade = CascadeType.ALL */, mappedBy = "parent")
	@OrderColumn(name = "sortOrder")
	private List<OrganizationUnit> children;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID", nullable = true)
	private OrganizationUnit parent;

	@Column(nullable = true)
	private Long sortOrder;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(nullable = false)
	private Organization organization;

	@Column(nullable = false)
	private LocalDateTime updateDateTime;

	@Version
	private long version;

	@Transient
	private long organizationId;

	@Transient
	private String name;

	@Transient
	private String nameJaJp;

	public static final String ORGANIZATION_ID = "organizationId";
	public static final String ID = OrganizationUnit_.ID;
	public static final String NAME = "name";
	public static final String NAMES = OrganizationUnit_.NAMES;
	public static final String UPDATE_DATE_TIME = OrganizationUnit_.UPDATE_DATE_TIME;
	public static final String VERSION = OrganizationUnit_.VERSION;

	public static final String COMPANYID = "companyId";
	public static final String ORGANIZATIONID = "organizationId";
	public static final String ORGANIZATIONVERSION = "organizationVersion";
	public static final String MAXRESULT = "maxResult";
	public static final String LOCALNAME = "localName";
	public static final String UPLOADFILE = "uploadFile";
	public static final String OPERATION = "operation";
	public static final String UNITTREEJSON = "unitTreeJSON";

	@Nonnull
	private static final String[] HEADERS = { //
			ORGANIZATION_ID, //
			OrganizationUnit_.ID, //
			OrganizationUnit_.UPDATE_DATE_TIME, //
			NAME, //
			NAME + "JaJp" //
	};

	private static final CellProcessor[] READ_CELL_PROCESSORS = new CellProcessor[] { //
			new ConvertNullTo(null, new ParseLong()), //
			new ConvertNullTo(null, new ParseLong()), //
			new ParseLocalDateTime(), //
			null, //
			null //
	};

	private static final CellProcessor[] WRITE_CELL_PROCESSORS = new CellProcessor[] { //
			null, //
			null, //
			new FmtLocalDateTime(), //
			null, //
			null //
	};

	@Nonnull
	public static String[] getHeaders() {

		return HEADERS;
	}

	public static CellProcessor[] getReadCellProcessors() {

		return READ_CELL_PROCESSORS;
	}

	public static CellProcessor[] getWriteCellProcessors() {

		return WRITE_CELL_PROCESSORS;
	}

	public void beforeWrite(Messages messages) {

		setOrganizationId(getOrganization().getId());

		int i = 0;
		for (final Entry<Locale, OrganizationUnitName> localeToName : getNames().entrySet()) {

			final Locale locale = localeToName.getKey();
			final String name = localeToName.getValue().getName();
			if (Locale.US == locale) {

				setName(name);
			} else if (Locale.JAPAN == locale) {

				setNameJaJp(name);
			} else {

				throw new IllegalStateException(messages.at(MessageKeys.JAVA_ERROR_SIZE, 0, NAME_COUNT_MAX) + " :" + i);
			}

			i++;
		}
	}

	public void afterRead(Messages messages) {

		final Map<Locale, OrganizationUnitName> names = new HashMap<>();
		names.put(Locale.US, new OrganizationUnitName(this.getId(), Locale.US, getName()));
		if (Objects.nonNull(getNameJaJp())) {

			names.put(Locale.JAPAN, new OrganizationUnitName(this.getId(), Locale.JAPAN, getNameJaJp()));
		}

		if (!(1 <= names.size())) {

			throw new IllegalStateException(messages.at(MessageKeys.JAVA_ERROR_SIZE, 0, NAME_COUNT_MAX) + " :" + names.size());
		}

		setNames(names);
	}

	public long getId() {

		return id;
	}

	public void setId(long id) {

		this.id = id;
	}

	public Map<Locale, OrganizationUnitName> getNames() {

		return names;
	}

	public void setNames(Map<Locale, OrganizationUnitName> names) {

		this.names = names;
	}

	public Organization getOrganization() {

		return organization;
	}

	public void setOrganization(Organization organization) {

		this.organization = organization;
	}

	public OrganizationUnit getParent() {

		return parent;
	}

	public void setParent(OrganizationUnit parent) {

		this.parent = parent;
	}

	public List<OrganizationUnit> getChildren() {

		return children;
	}

	public void setChildren(List<OrganizationUnit> children) {

		this.children = children;
	}

	public Long getSortOrder() {

		return sortOrder;
	}

	public void setSortOrder(Long sortOrder) {

		this.sortOrder = sortOrder;
	}

	public LocalDateTime getUpdateDateTime() {

		return updateDateTime;
	}

	public void setUpdateDateTime(LocalDateTime updateDateTime) {

		this.updateDateTime = updateDateTime;
	}

	public long getVersion() {

		return version;
	}

	public void setVersion(long version) {

		this.version = version;
	}

	public long getOrganizationId() {

		return organizationId;
	}

	public void setOrganizationId(long organizationId) {

		this.organizationId = organizationId;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getNameJaJp() {

		return nameJaJp;
	}

	public void setNameJaJp(String nameJaJp) {

		this.nameJaJp = nameJaJp;
	}
}
