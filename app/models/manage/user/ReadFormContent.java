package models.manage.user;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.Size;

import common.system.MessageKeys;
import models.user.User;
import models.user.User.Role;
import play.data.validation.Constraints;

public class ReadFormContent {

	@Constraints.MaxLength(value = 256)
	private String userId;

	@Size(message = MessageKeys.CONSTRAINTS_SIZE, min = 1, max = User.ROLE_COUNT_MAX)
	private List<Role> roles;

	private Long companyId;

	public LocalDate expireFrom;

	public LocalDate expireTo;

	@Constraints.Required
	@Constraints.Pattern(value = "\\d+")
	private String maxResult;

	public String getUserId() {

		return userId;
	}

	public void setUserId(String userId) {

		this.userId = userId;
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

	public LocalDate getExpireFrom() {

		return expireFrom;
	}

	public void setExpireFrom(LocalDate expireFrom) {

		this.expireFrom = expireFrom;
	}

	public LocalDate getExpireTo() {

		return expireTo;
	}

	public void setExpireTo(LocalDate expireTo) {

		this.expireTo = expireTo;
	}

	public String getMaxResult() {

		return maxResult;
	}

	public void setMaxResult(String maxResult) {

		this.maxResult = maxResult;
	}
}
