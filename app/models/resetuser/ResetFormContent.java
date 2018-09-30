package models.resetuser;

import common.data.validation.Constraints.Password;
import common.data.validation.Constraints.UserId;
import common.data.validation.groups.Read;
import common.data.validation.groups.Update;
import play.data.validation.Constraints;

public class ResetFormContent {

	// Play
	@Constraints.Required(groups = { Read.class })
	// System
	@UserId(groups = { Read.class })
	private String userId;

	// Play
	@Constraints.Required(groups = { Update.class })
	// System
	@Password(groups = { Update.class })
	private String password;

	// Play
	@Constraints.Required(groups = { Update.class })
	// System
	@Password(groups = { Update.class })
	private String rePassword;

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

	/**
	 * @return the rePassword
	 */
	public String getRePassword() {
		return rePassword;
	}

	/**
	 * @param rePassword the rePassword to set
	 */
	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}
}
