package models.manage.company.organization.tree;

import play.data.validation.Constraints;

public class ReadFormContent {

	@Constraints.Required
	private long companyId;

	// Play
	private Long organizationId;

	// Play
	private Long organizationVersion;

	public long getCompanyId() {

		return companyId;
	}

	public void setCompanyId(long companyId) {

		this.companyId = companyId;
	}

	public Long getOrganizationId() {

		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {

		this.organizationId = organizationId;
	}

	public Long getOrganizationVersion() {

		return organizationVersion;
	}

	public void setOrganizationVersion(Long organizationVersion) {

		this.organizationVersion = organizationVersion;
	}
}
