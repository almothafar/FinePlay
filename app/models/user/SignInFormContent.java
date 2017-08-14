package models.user;

import common.data.validation.Constraints.Password;
import common.data.validation.Constraints.UserId;
import play.data.validation.Constraints;

public class SignInFormContent {

	// Play
	@Constraints.Required
	// System
	@UserId
	private String userId;

	// Play
	@Constraints.Required
	// System
	@Password
	private String password;

	// Play
	private String storeAccount;

	public String getUserId() {

		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {

		return password;
	}

	public void setPassword(String password) {

		this.password = password;
	}

	public String getStoreAccount() {

		return storeAccount;
	}

	public void setStoreAccount(String storeAccount) {

		this.storeAccount = storeAccount;
	}
}
