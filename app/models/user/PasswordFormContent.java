package models.user;

import common.data.validation.Constraints.Password;
import play.data.validation.Constraints;

public class PasswordFormContent {

	// Play
	@Constraints.Required
	// System
	@Password
	private String password;

	public static final String PASSWORD = User.PASSWORD;

	public String getPassword() {

		return password;
	}

	public void setPassword(String password) {

		this.password = password;
	}
}
