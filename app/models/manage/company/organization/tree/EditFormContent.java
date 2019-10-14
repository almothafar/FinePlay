package models.manage.company.organization.tree;

import java.time.LocalDateTime;

import common.data.validation.groups.Create;
import common.data.validation.groups.Delete;
import common.data.validation.groups.Update;
import play.data.validation.Constraints;

public class EditFormContent {

	// Play
	@Constraints.Required
	private long companyId;

	// Play
	@Constraints.Required(groups = { Create.class, Update.class, Delete.class })
	private long organizationId;

	// Play
	@Constraints.Required(groups = { Update.class, Delete.class })
	private LocalDateTime organizationVersion;

	// Play
	@Constraints.Required(groups = { Update.class })
	private String unitTreeJSON;

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

	public String getUnitTreeJSON() {

		return unitTreeJSON;
	}

	public void setUnitTreeJSON(String unitTreeJSON) {

		this.unitTreeJSON = unitTreeJSON;
	}
}
