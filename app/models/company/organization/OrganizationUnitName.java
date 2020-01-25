package models.company.organization;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import models.base.LocaleConverter;

@Entity
@Table(name = "ORGANIZATION_UNIT_NAMES", //
		uniqueConstraints = { @UniqueConstraint(columnNames = { OrganizationUnitName_.ORGANIZATION_UNIT__ID, OrganizationUnitName_.LOCALE }) }, //
		indexes = { @Index(columnList = OrganizationUnitName_.ORGANIZATION_UNIT__ID + "," + OrganizationUnitName_.LOCALE) })
@IdClass(OrganizationUnitName.PK.class)
public class OrganizationUnitName {

	@SuppressWarnings("serial")
	public static class PK implements Serializable {

		private long organizationUnit_Id;

		private Locale locale;

		public long getOrganizationUnit_Id() {

			return organizationUnit_Id;
		}

		public void setOrganizationUnit_Id(long organizationUnit_Id) {

			this.organizationUnit_Id = organizationUnit_Id;
		}

		public Locale getLocale() {

			return locale;
		}

		public void setLocale(Locale locale) {

			this.locale = locale;
		}

		@Override
		public int hashCode() {

			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (organizationUnit_Id ^ (organizationUnit_Id >>> 32));
			result = prime * result + ((locale == null) ? 0 : locale.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {

			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PK other = (PK) obj;
			if (organizationUnit_Id != other.organizationUnit_Id)
				return false;
			if (locale == null) {
				if (other.locale != null)
					return false;
			} else if (!locale.equals(other.locale))
				return false;
			return true;
		}
	}

	@Id
	@Column(nullable = false)
	private long organizationUnit_Id;

	@Id
	@Column(nullable = false)
	private Locale locale;

	@Column(nullable = false)
	private String name;

	public OrganizationUnitName() {
	}

	public OrganizationUnitName(final long organizationUnit_Id, final Locale locale, final String name) {

		this.organizationUnit_Id = organizationUnit_Id;
		this.locale = locale;
		this.name = name;
	}

	public long getOrganizationUnit_Id() {
		return organizationUnit_Id;
	}

	public void setOrganizationUnit_Id(long organizationUnit_Id) {
		this.organizationUnit_Id = organizationUnit_Id;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
