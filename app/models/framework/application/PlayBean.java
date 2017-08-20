package models.framework.application;

import static play.libs.F.Tuple;

import common.system.MessageKeys;
import play.data.validation.Constraints;
import play.data.validation.Constraints.Validator;

public class PlayBean {

	@Constraints.Required
	private String constraint_required;
	@Constraints.Min(value = 3L)
	private int constraint_min = 2;
	@Constraints.Max(value = 7L)
	private int constraint_max = 8;
	@Constraints.MinLength(value = 3L)
	private String constraint_minLength = "12";
	@Constraints.MaxLength(value = 7L)
	private String constraint_maxLength = "12345678";
	@Constraints.Email
	private String constraint_email = "@";
	@Constraints.Pattern(value = "(>_<)")
	private String constraint_pattern = "@";
	@Constraints.ValidateWith(value = CustomValidator.class)
	private String constraint_validateWith = "@";

	public String getConstraint_required() {
		return constraint_required;
	}
	public int getConstraint_min() {
		return constraint_min;
	}
	public int getConstraint_max() {
		return constraint_max;
	}
	public String getConstraint_minLength() {
		return constraint_minLength;
	}
	public String getConstraint_maxLength() {
		return constraint_maxLength;
	}
	public String getConstraint_email() {
		return constraint_email;
	}
	public String getConstraint_pattern() {
		return constraint_pattern;
	}

	public String getConstraint_validateWith() {
		return constraint_validateWith;
	}

	private static class CustomValidator extends Validator<String> {

		final static public String message = MessageKeys.ERROR_INVALID;

		public CustomValidator() {
		}

		public boolean isValid(String object) {
			if (object == null || object.isEmpty()) {
				return true;
			}

			return "(*^-^*)".equals(object);
		}

		public Tuple<String, Object[]> getErrorMessageKey() {
			return Tuple(message, new Object[]{});
		}
	}
}
