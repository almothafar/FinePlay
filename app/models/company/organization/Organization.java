package models.company.organization;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import models.company.Company;
import play.Logger;
import play.Logger.ALogger;
import play.i18n.MessagesApi;

@Entity
@Table(name = "ORGANIZATIONS", uniqueConstraints = {@UniqueConstraint(columnNames = {"ID", "COMPANY_ID"})}, indexes = {@Index(columnList = "ID"), @Index(columnList = "COMPANY_ID")})
public class Organization {

	private static final ALogger LOGGER = Logger.of(Organization.class);

	@Inject
	@Transient
	private MessagesApi messages;

	@Id
	@GeneratedValue
	@Column(nullable = false)
	// Play
	private long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns(value = {@JoinColumn(nullable = false, unique = true)})
	private Company company;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "organization")
	private Set<OrganizationUnit> organizationUnits = new HashSet<>();

	@Version
	@Column(nullable = false)
	private LocalDateTime updateDateTime;

	public static final String ID = "id";
	public static final String UPDATEDATETIME = "updateDateTime";

	public Map<Long, OrganizationUnit> getIdToUnitMap() {

		final Map<Long, OrganizationUnit> idToUnitMap = getOrganizationUnits().stream()
				.collect(Collectors.toMap(//
						unit -> unit.getId(), //
						unit -> unit, //
						(k, v) -> {
							throw new IllegalStateException(v.toString());
						}, //
						HashMap::new));

		return Collections.unmodifiableMap(idToUnitMap);
	}

	public long getId() {

		return id;
	}

	public void setId(long id) {

		this.id = id;
	}

	public Company getCompany() {

		return company;
	}

	public void setCompany(Company company) {

		this.company = company;
	}

	public Set<OrganizationUnit> getOrganizationUnits() {

		return organizationUnits;
	}

	public void setOrganizationUnits(Set<OrganizationUnit> organizationUnits) {

		this.organizationUnits = organizationUnits;
	}

	public LocalDateTime getUpdateDateTime() {

		return updateDateTime;
	}

	public void setUpdateDateTime(LocalDateTime updateDateTime) {

		this.updateDateTime = updateDateTime;
	}
}
