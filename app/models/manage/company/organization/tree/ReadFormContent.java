package models.manage.company.organization.tree;

import java.time.LocalDateTime;

import play.data.validation.Constraints;

public class ReadFormContent {

	@Constraints.Required
	private long companyId;

	// Play
	private Long organizationId;

	// Play
	private LocalDateTime organizationVersion;

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

	public LocalDateTime getOrganizationVersion() {

		return organizationVersion;
	}

	public void setOrganizationVersion(LocalDateTime organizationVersion) {

		this.organizationVersion = organizationVersion;
	}
}
