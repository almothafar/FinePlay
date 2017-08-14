package models.manage.company.organization.tree;

import java.time.LocalDateTime;

import play.data.validation.Constraints;

public class ReadFormContent {

	@Constraints.Required
	private long companyId;

	// Play
	private Long organizationId;

	// Play
	private LocalDateTime organizationUpdateDateTime;

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

	public LocalDateTime getOrganizationUpdateDateTime() {

		return organizationUpdateDateTime;
	}

	public void setOrganizationUpdateDateTime(LocalDateTime organizationUpdateDateTime) {

		this.organizationUpdateDateTime = organizationUpdateDateTime;
	}
}
