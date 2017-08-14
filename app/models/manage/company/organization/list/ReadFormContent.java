package models.manage.company.organization.list;

import java.time.LocalDateTime;

import play.data.validation.Constraints;

public class ReadFormContent {

	@Constraints.Required
	private long companyId;

	// Play
	private Long organizationId;

	// Play
	private LocalDateTime organizationUpdateDateTime;

	@Constraints.MaxLength(value = 256)
	private String name;

	@Constraints.Required
	@Constraints.Pattern(value = "\\d+")
	private String maxResult;

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

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getMaxResult() {

		return maxResult;
	}

	public void setMaxResult(String maxResult) {

		this.maxResult = maxResult;
	}
}
