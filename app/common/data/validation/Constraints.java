package common.data.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static play.libs.F.Tuple;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;

import common.system.MessageKeys;
import play.data.validation.Constraints.Validator;

public class Constraints {

	// --- UserId

	/**
	 * Defines a userId constraint for a string field.
	 */
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Constraint(validatedBy = UserIdValidator.class)
	@play.data.Form.Display(name = MessageKeys.SYSTEM_CONSTRAINT_USERID, attributes = {})
	public static @interface UserId {
		String message() default UserIdValidator.message;

		Class<?>[] groups() default {};

		Class<? extends Payload>[] payload() default {};
	}

	/**
	 * Validator for <code>@UserId</code> fields.
	 */
	public static class UserIdValidator extends Validator<String> implements ConstraintValidator<UserId, String> {

		final static public String message = MessageKeys.SYSTEM_ERROR_USERID;
		final static java.util.regex.Pattern regex = java.util.regex.Pattern.compile("\\b[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\\b");

		public UserIdValidator() {
		}

		@Override
		public void initialize(UserId constraintAnnotation) {
		}

		@Override
		public boolean isValid(String object) {
			if (object == null || object.length() == 0) {
				return true;
			}

			return regex.matcher(object).matches();
		}

		@Override
		public Tuple<String, Object[]> getErrorMessageKey() {
			return Tuple(message, new Object[] {});
		}

	}

	/**
	 * Constructs a 'userId' validator.
	 *
	 * @return userId
	 */
	public static Validator<String> userId() {
		return new UserIdValidator();
	}

	// --- Password

	/**
	 * Defines a password constraint for a string field.
	 */
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Constraint(validatedBy = PasswordValidator.class)
	@play.data.Form.Display(name = MessageKeys.SYSTEM_CONSTRAINT_PASSWORD, attributes = {})
	public static @interface Password {
		String message() default PasswordValidator.message;

		Class<?>[] groups() default {};

		Class<? extends Payload>[] payload() default {};
	}

	/**
	 * Validator for <code>@Password</code> fields.
	 */
	public static class PasswordValidator extends Validator<String> implements ConstraintValidator<Password, String> {

		final static public String message = MessageKeys.SYSTEM_ERROR_PASSWORD;
		final static java.util.regex.Pattern regex = java.util.regex.Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!#$%&'*+/=?^_`{|}~-]).{8,16}");

		public PasswordValidator() {
		}

		@Override
		public void initialize(Password constraintAnnotation) {
		}

		@Override
		public boolean isValid(String object) {
			if (object == null || object.length() == 0) {
				return true;
			}

			return regex.matcher(object).matches();
		}

		@Override
		public Tuple<String, Object[]> getErrorMessageKey() {
			return Tuple(message, new Object[] {});
		}

	}

	/**
	 * Constructs a 'password' validator.
	 *
	 * @return password
	 */
	public static Validator<String> password() {
		return new PasswordValidator();
	}

	// --- Hiragana

	/**
	 * Defines a hiragana constraint for a string field.
	 */
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Constraint(validatedBy = HiraganaValidator.class)
	@play.data.Form.Display(name = MessageKeys.SYSTEM_CONSTRAINT_HIRAGANA, attributes = {})
	public static @interface Hiragana {
		String message() default HiraganaValidator.message;

		Class<?>[] groups() default {};

		Class<? extends Payload>[] payload() default {};
	}

	/**
	 * Validator for <code>@Hiragana</code> fields.
	 */
	public static class HiraganaValidator extends Validator<String> implements ConstraintValidator<Hiragana, String> {

		final static public String message = MessageKeys.SYSTEM_ERROR_HIRAGANA;
		final static java.util.regex.Pattern regex = java.util.regex.Pattern.compile("^[ぁ-ゖ゛゜ゝゞー]*$");

		public HiraganaValidator() {
		}

		@Override
		public void initialize(Hiragana constraintAnnotation) {
		}

		@Override
		public boolean isValid(String object) {
			if (object == null || object.length() == 0) {
				return true;
			}

			return regex.matcher(object).matches();
		}

		@Override
		public Tuple<String, Object[]> getErrorMessageKey() {
			return Tuple(message, new Object[] {});
		}

	}

	/**
	 * Constructs a 'hiragana' validator.
	 *
	 * @return hiragana
	 */
	public static Validator<String> hiragana() {
		return new HiraganaValidator();
	}

	// --- Katakana

	/**
	 * Defines a katakana constraint for a string field.
	 */
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Constraint(validatedBy = KatakanaValidator.class)
	@play.data.Form.Display(name = MessageKeys.SYSTEM_CONSTRAINT_KATAKANA, attributes = {})
	public static @interface Katakana {
		String message() default KatakanaValidator.message;

		Class<?>[] groups() default {};

		Class<? extends Payload>[] payload() default {};
	}

	/**
	 * Validator for <code>@Katakana</code> fields.
	 */
	public static class KatakanaValidator extends Validator<String> implements ConstraintValidator<Katakana, String> {

		final static public String message = MessageKeys.SYSTEM_ERROR_KATAKANA;
		final static java.util.regex.Pattern regex = java.util.regex.Pattern.compile("^[ァ-ヶ゛゜ヽヾー]*$");

		public KatakanaValidator() {
		}

		@Override
		public void initialize(Katakana constraintAnnotation) {
		}

		@Override
		public boolean isValid(String object) {
			if (object == null || object.length() == 0) {
				return true;
			}

			return regex.matcher(object).matches();
		}

		@Override
		public Tuple<String, Object[]> getErrorMessageKey() {
			return Tuple(message, new Object[] {});
		}

	}

	/**
	 * Constructs a 'katakana' validator.
	 *
	 * @return katakana
	 */
	public static Validator<String> katakana() {
		return new KatakanaValidator();
	}
}
