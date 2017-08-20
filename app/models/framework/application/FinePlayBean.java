package models.framework.application;

import common.data.validation.Constraints;

public class FinePlayBean {

	@Constraints.UserId
	private final String system_constraint_userid = "@";

	@Constraints.Password
	private final String system_constraint_password = "@";

	@Constraints.Hiragana
	private final String system_constraint_hiragana = "@あいうえお@";

	@Constraints.Katakana
	private final String system_constraint_katakana = "@カキクケコ@";

	public String getSystem_constraint_userid() {
		return system_constraint_userid;
	}

	public String getSystem_constraint_password() {
		return system_constraint_password;
	}

	public String getSystem_constraint_hiragana() {
		return system_constraint_hiragana;
	}

	public String getSystem_constraint_katakana() {
		return system_constraint_katakana;
	}
}
