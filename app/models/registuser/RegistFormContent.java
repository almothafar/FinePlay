package models.registuser;

import common.data.validation.Constraints.Password;
import common.data.validation.Constraints.UserId;
import play.data.validation.Constraints;

public class RegistFormContent {

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
	@Constraints.Required
	// System
	@Password
	private String rePassword;

	// Play
	@Constraints.Required
	private String offsetSecond;

	private String shortZoneId;

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

	public String getRePassword() {

		return rePassword;
	}

	public void setRePassword(String rePassword) {

		this.rePassword = rePassword;
	}

	public String getOffsetSecond() {

		return offsetSecond;
	}

	public void setOffsetSecond(String offsetSecond) {

		this.offsetSecond = offsetSecond;
	}

	public String getShortZoneId() {

		return shortZoneId;
	}

	public void setShortZoneId(String shortZoneId) {

		this.shortZoneId = shortZoneId;
	}
}
