package models.manage.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Size;

import common.data.validation.Constraints.Password;
import common.data.validation.Constraints.UserId;
import common.data.validation.groups.Create;
import common.data.validation.groups.Delete;
import common.data.validation.groups.Update;
import common.system.MessageKeys;
import models.user.User;
import models.user.User.Role;
import play.data.validation.Constraints;

public class EditFormContent {

	// Play
	@Constraints.Required(groups = {Create.class, Update.class, Delete.class})
	// System
	@UserId(groups = {Create.class, Update.class, Delete.class})
	private String userId;

	// Play
	@Constraints.Required(groups = {Update.class})
	// System
	@UserId(groups = {Update.class})
	private String newUserId;

	// Play
	@Constraints.Required(groups = {Create.class})
	// System
	@Password(groups = {Create.class})
	private String password;

	// Play
	@Constraints.Required(groups = {Create.class})
	// System
	@Password(groups = {Create.class})
	private String rePassword;

	// JSR-303
	@Size(message = MessageKeys.JAVA_ERROR_SIZE, min = 1, max = User.ROLE_COUNT_MAX, groups = {Create.class, Update.class})
	private List<Role> roles;

	private Long companyId;

	// Play
	@Constraints.Required(groups = {Update.class, Delete.class})
	private LocalDateTime updateDateTime;

	public String getUserId() {

		return userId;
	}

	public void setUserId(String userId) {

		this.userId = userId;
	}

	public String getNewUserId() {

		return newUserId;
	}

	public void setNewUserId(String newUserId) {

		this.newUserId = newUserId;
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

	public List<Role> getRoles() {

		return roles;
	}

	public void setRoles(List<Role> roles) {

		this.roles = roles;
	}

	public Long getCompanyId() {

		return companyId;
	}

	public void setCompanyId(Long companyId) {

		this.companyId = companyId;
	}

	public LocalDateTime getUpdateDateTime() {

		return updateDateTime;
	}

	public void setUpdateDateTime(LocalDateTime updateDateTime) {

		this.updateDateTime = updateDateTime;
	}
}
