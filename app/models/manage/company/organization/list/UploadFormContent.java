package models.manage.company.organization.list;

import play.data.validation.Constraints;

public class UploadFormContent {

	@Constraints.Required
	private long companyId;

	// Play
	private Long organizationId;

	public enum Operation {
		CREATE, UPDATE
	}

	@Constraints.Required
	private Operation operation;

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

	public Operation getOperation() {

		return operation;
	}

	public void setOperation(Operation operation) {

		this.operation = operation;
	}
}
