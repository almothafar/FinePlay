package models.manage.company;

import play.data.validation.Constraints;

public class ReadFormContent {

	@Constraints.MaxLength(value = 256)
	private String name;

	@Constraints.Required
	@Constraints.Pattern(value = "\\d+")
	private String maxResult;

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
