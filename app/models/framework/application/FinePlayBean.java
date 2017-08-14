package models.framework.application;

import common.data.validation.Constraints;

public class FinePlayBean {

	@Constraints.UserId
	private final String userId = "@";
	@Constraints.Password
	private final String password = "@";

	@Constraints.Hiragana
	private final String hiragana = "@あいうえお@";
	@Constraints.Katakana
	private final String katakana = "@カキクケコ@";
}
