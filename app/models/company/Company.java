package models.company;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

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
import javax.persistence.JoinColumns;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
import org.supercsv.cellprocessor.time.FmtLocalDateTime;
import org.supercsv.cellprocessor.time.ParseLocalDateTime;

import common.system.MessageKeys;
import models.company.organization.Organization;
import play.i18n.Messages;
import play.i18n.MessagesApi;

@Entity
@Table(name = "COMPANIES", //
		uniqueConstraints = { @UniqueConstraint(columnNames = { Company_.ID }) }, //
		indexes = { @Index(columnList = Company_.ID) })
public class Company {

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
	@PrimaryKeyJoinColumn(name = CompanyName_.COMPANY__ID)
	@MapKeyColumn(name = CompanyName_.LOCALE)
	@Size(min = 1, message = MessageKeys.JAVA_ERROR_SIZE)
	private Map<Locale, CompanyName> names;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "company")
	@JoinColumns(value = { @JoinColumn(nullable = true, unique = true) })
	private Organization organization;

	@Version
	@Column(nullable = false)
	private LocalDateTime updateDateTime;

	@Transient
	private String name;

	@Transient
	private String nameJaJp;

	public static final String ID = Company_.ID;
	public static final String CODE = "code";
	public static final String CODES = "codes";
	public static final String NAME = "name";
	public static final String NAMES = Company_.NAMES;
	public static final String UPDATE_DATE_TIME = Company_.UPDATE_DATE_TIME;

	public static final String MAXRESULT = "maxResult";
	public static final String LOCALNAME = "localName";
	public static final String UPLOADFILE = "uploadFile";
	public static final String OPERATION = "operation";

	@Nonnull
	private static final String[] HEADERS = { //
			Company_.ID, //
			Company_.UPDATE_DATE_TIME, //
			NAME, //
			NAME + "JaJp" //
	};

	private static final CellProcessor[] READ_CELL_PROCESSORS = new CellProcessor[] { //
			new ConvertNullTo(null, new ParseLong()), //
			new ParseLocalDateTime(), //
			null, //
			null //
	};

	private static final CellProcessor[] WRITE_CELL_PROCESSORS = new CellProcessor[] { //
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

		int i = 0;
		for (final Entry<Locale, CompanyName> localeToName : getNames().entrySet()) {

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

		final Map<Locale, CompanyName> names = new HashMap<>();
		names.put(Locale.US, new CompanyName(this.getId(), Locale.US, getName()));
		if (Objects.nonNull(getNameJaJp())) {

			names.put(Locale.JAPAN, new CompanyName(this.getId(), Locale.JAPAN, getNameJaJp()));
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

	public Map<Locale, CompanyName> getNames() {

		return names;
	}

	public void setNames(Map<Locale, CompanyName> names) {

		this.names = names;
	}

	public Organization getOrganization() {

		return organization;
	}

	public void setOrganization(Organization organization) {

		this.organization = organization;
	}

	public LocalDateTime getUpdateDateTime() {

		return updateDateTime;
	}

	public void setUpdateDateTime(LocalDateTime updateDateTime) {

		this.updateDateTime = updateDateTime;
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
