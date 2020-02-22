package models.company;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "COMPANY_NAMES", //
		uniqueConstraints = { @UniqueConstraint(columnNames = { CompanyName_.COMPANY__ID, CompanyName_.LOCALE }) }, //
		indexes = { @Index(columnList = CompanyName_.COMPANY__ID + "," + CompanyName_.LOCALE) })
@IdClass(CompanyName.PK.class)
public class CompanyName {

	@SuppressWarnings("serial")
	public static class PK implements Serializable {

		private long company_Id;

		private Locale locale;

		public long getCompany_Id() {

			return company_Id;
		}

		public void setCompany_Id(long company_Id) {

			this.company_Id = company_Id;
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
			result = prime * result + (int) (company_Id ^ (company_Id >>> 32));
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
			if (company_Id != other.company_Id)
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
	private long company_Id;

	@Id
	@Column(nullable = false)
	private Locale locale;

	@Column(nullable = false)
	private String name;

	public CompanyName() {
	}

	public CompanyName(final long company_Id, final Locale locale, final String name) {

		this.company_Id = company_Id;
		this.locale = locale;
		this.name = name;
	}

	public long getCompany_Id() {
		return company_Id;
	}

	public void setCompany_Id(long company_Id) {
		this.company_Id = company_Id;
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
