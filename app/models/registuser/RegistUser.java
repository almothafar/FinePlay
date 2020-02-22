package models.registuser;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import common.data.validation.Constraints.UserId;
import common.utils.Strings;
import models.user.ExpireHandler;
import models.user.PasswordHandler;
import play.data.validation.Constraints;

@Entity
@Table(name = "REGIST_USERS", //
		uniqueConstraints = { @UniqueConstraint(columnNames = { RegistUser_.ID }) })
public class RegistUser implements ExpireHandler, PasswordHandler {

	private static final int CODE_SIZE_MAX = 64;

	@Id
	@GeneratedValue
	@Column(nullable = false)
	private long id;

	@Column(nullable = false, length = 256)
	// Play
	@Constraints.Required
	// System
	@UserId
	private String userId;

	@Column(nullable = false, length = 60)
	// Play
	@Constraints.Required
	private String hashedPassword;

	@Column(nullable = false)
	private Locale locale;

	@Column(nullable = false)
	private ZoneId zoneId;

	@Column(nullable = false, length = CODE_SIZE_MAX)
	// Play
	@Constraints.Required
	private String code;

	@Column(nullable = false)
	private LocalDateTime expireDateTime;

	public void setCode() {

		final String generatedCode = generateCode();
		setCode(generatedCode);
	}

	private String generateCode() {

		return Strings.randomAlphanumeric(CODE_SIZE_MAX);
	}

	public long getId() {

		return id;
	}

	public void setId(long id) {

		this.id = id;
	}

	public String getUserId() {

		return userId;
	}

	public void setUserId(String userId) {

		this.userId = userId;
	}

	public String getHashedPassword() {

		return hashedPassword;
	}

	@Override
	public void setHashedPassword(@Nonnull String hashedPassword) {

		this.hashedPassword = hashedPassword;
	}

	public Locale getLocale() {

		return locale;
	}

	public void setLocale(Locale locale) {

		this.locale = locale;
	}

	public ZoneId getZoneId() {

		return zoneId;
	}

	public void setZoneId(ZoneId zoneId) {

		this.zoneId = zoneId;
	}

	public String getCode() {

		return code;
	}

	public void setCode(String code) {

		this.code = code;
	}

	@Override
	@Nonnull
	public LocalDateTime getExpireDateTime() {

		return expireDateTime;
	}

	public void setExpireDateTime(LocalDateTime expireDateTime) {

		this.expireDateTime = expireDateTime;
	}
}
