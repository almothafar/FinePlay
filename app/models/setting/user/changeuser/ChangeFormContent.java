package models.setting.user.changeuser;

import common.data.validation.Constraints.UserId;
import play.data.validation.Constraints;

public class ChangeFormContent {

	// Play
	@Constraints.Required
	// System
	@UserId
	private String newUserId;

	public String getNewUserId() {

		return newUserId;
	}

	public void setNewUserId(String newUserId) {

		this.newUserId = newUserId;
	}
}
