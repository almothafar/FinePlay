package models.framework.application;

import play.data.validation.Constraints;

public class PlayBean {

	@Constraints.Required
	private String required;
	@Constraints.Min(value = 3L)
	private final int min = 2;
	@Constraints.Max(value = 7L)
	private final int max = 8;
	@Constraints.MinLength(value = 3L)
	private final String minLength = "12";
	@Constraints.MaxLength(value = 7L)
	private final String maxLength = "12345678";
	@Constraints.Email
	private final String email = "@";
	@Constraints.Pattern(value = "..")
	private final String pattern = "@";
}
