package models.manage.user;

import play.data.validation.Constraints;

public class UploadFormContent {

	public enum Operation {
		CREATE, UPDATE
	}

	@Constraints.Required
	private Operation operation;

	public Operation getOperation() {

		return operation;
	}

	public void setOperation(Operation operation) {

		this.operation = operation;
	}
}
