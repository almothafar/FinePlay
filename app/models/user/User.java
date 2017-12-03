package models.user;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ParseEnum;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import common.data.validation.Constraints.UserId;
import common.system.MessageKeys;
import common.utils.Sessions;
import models.base.LocaleConverter;
import models.base.ZoneIdConverter;
import models.company.Company;
import models.supercsv.cellprocessor.time.FmtClientLocalDateTime;
import models.supercsv.cellprocessor.time.ParseServerLocalDateTime;
import models.system.System.Permission;
import play.data.validation.Constraints;
import play.data.validation.Constraints.Validatable;
import play.data.validation.Constraints.Validate;
import play.data.validation.ValidationError;
import play.i18n.MessagesApi;
import play.mvc.Controller;

@Validate
@Entity
@Table(name = "USERS", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID", "USERID" }) }, indexes = { @Index(columnList = "USERID") })
public class User implements ExpireHandler, PasswordHandler, Validatable<List<ValidationError>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

	public static final int ROLE_COUNT_MAX = 5;

	public enum Role {
		GUEST, CUSTOMER, ADMIN
	}

	public enum Theme {
		DEFAULT, PRETTY
	}

	@Inject
	@Transient
	private MessagesApi messages;

	@Id
	@GeneratedValue
	@Column(nullable = false)
	// Play
	private long id;

	@Column(unique = true, nullable = false, length = 256)
	// Play
	@Constraints.Required
	// System
	@UserId
	private String userId;

	@Column(nullable = false, length = SALT_SIZE_MAX)
	private String salt;

	@Column(nullable = false, length = 64)
	// Play
	@Constraints.Required
	private String hashedPassword;

	@ElementCollection
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "USER_ROLES", uniqueConstraints = { @UniqueConstraint(columnNames = { "USER_ID", "ROLE" }) }, joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"))
	@Column(name = "ROLE", nullable = false)
	// JSR
	@Size(min = 1, max = ROLE_COUNT_MAX, message = "constraints.Size")
	private Set<Role> roles = EnumSet.noneOf(Role.class);

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Theme theme;

	@Column(nullable = false)
	@Convert(converter = LocaleConverter.class)
	private Locale locale;

	@Column(nullable = false)
	@Convert(converter = ZoneIdConverter.class)
	private ZoneId zoneId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID", nullable = true)
	public Company company;

	@Column(nullable = false)
	private LocalDateTime expireDateTime;

	private LocalDateTime signInDateTime;

	private LocalDateTime signOutDateTime;

	@Version
	@Column(nullable = false)
	private LocalDateTime updateDateTime;

	@Transient
	private Role role0;

	@Transient
	private Role role1;

	@Transient
	private Role role2;

	@Transient
	private Role role3;

	@Transient
	private Role role4;

	private static final String ID = "id";
	public static final String USERID = "userId";
	public static final String PASSWORD = "password";
	private static final String ROLE = "role";
	public static final String ROLES = "roles";
	public static final String THEME = "theme";
	public static final String LOCALE = "locale";
	public static final String ZONEID = "zoneId";
	public static final String EXPIREDATETIME = "expireDateTime";
	public static final String SIGNINDATETIME = "signInDateTime";
	public static final String SIGNOUTDATETIME = "signOutDateTime";
	public static final String UPDATEDATETIME = "updateDateTime";

	public static final String NEWUSERID = "newUserId";
	public static final String REPASSWORD = "rePassword";
	public static final String COMPANYID = "companyId";
	public static final String COMPANYNAME = "companyName";
	public static final String EXPIREFROM = "expireFrom";
	public static final String EXPIRETO = "expireTo";
	public static final String MAXRESULT = "maxResult";
	public static final String UPLOADFILE = "uploadFile";
	public static final String OPERATION = "operation";
	public static final String STOREACCOUNT = "storeAccount";
	public static final String OFFSETSECOND = "offsetSecond";
	public static final String SHORTZONEID = "shortZoneId";

	private static final String[] HEADERS = { //
			USERID, //
			EXPIREDATETIME, //
			SIGNINDATETIME, //
			SIGNOUTDATETIME, //
			UPDATEDATETIME, //
			ROLE + "0", //
			ROLE + "1", //
			ROLE + "2", //
			ROLE + "3", //
			ROLE + "4" //
	};

	private static final CellProcessor[] READ_CELL_PROCESSORS = new CellProcessor[] { //
			null, //
			new ParseServerLocalDateTime(), //
			new ConvertNullTo(null, new ParseServerLocalDateTime()), //
			new ConvertNullTo(null, new ParseServerLocalDateTime()), //
			new ParseServerLocalDateTime(), //
			new ConvertNullTo(null, new ParseEnum(Role.class)), //
			new ConvertNullTo(null, new ParseEnum(Role.class)), //
			new ConvertNullTo(null, new ParseEnum(Role.class)), //
			new ConvertNullTo(null, new ParseEnum(Role.class)), //
			new ConvertNullTo(null, new ParseEnum(Role.class)) //
	};

	private static final CellProcessor[] WRITE_CELL_PROCESSORS = new CellProcessor[] { //
			null, //
			new FmtClientLocalDateTime(), //
			new ConvertNullTo("", new FmtClientLocalDateTime()), //
			new ConvertNullTo("", new FmtClientLocalDateTime()), //
			new FmtClientLocalDateTime(), //
			null, //
			null, //
			null, //
			null, //
			null //
	};

	public static boolean hasAnyRole(@Nonnull final Role... roles) {

		final Set<Role> requestRoles = EnumSet.copyOf(Arrays.asList(roles));
		final String roleValues = Controller.ctx().session().get(ROLES);
		final Set<Role> userRoles = Sessions.toRoles(roleValues);
		return userRoles.stream().anyMatch(role -> requestRoles.contains(role));
	}

	public boolean isValidPassword(@Nonnull final String password) {

		Objects.requireNonNull(password);

		final String hashedPassword = toHashedPassword(password + getSalt());
		return getHashedPassword().equals(hashedPassword);
	}

	public static String[] getHeaders() {

		return HEADERS;
	}

	public static CellProcessor[] getReadCellProcessors() {

		return READ_CELL_PROCESSORS;
	}

	public static CellProcessor[] getWriteCellProcessors() {

		return WRITE_CELL_PROCESSORS;
	}

	@Nonnull
	public Set<Permission> getPermissions() {

		return getPermissions(getRoles());
	}

	@Nonnull
	public static Set<Permission> getPermissions(@Nonnull final Set<Role> roles) {

		Objects.requireNonNull(roles);

		final Config rolesConfig = ConfigFactory.parseFileAnySyntax(Paths.get("conf", "roles.conf").toFile());

		final Set<Permission> permissions = roles.stream().flatMap(role -> {

			final List<String> permissionStrings = rolesConfig.getStringList(role.name());
			Objects.requireNonNull(permissionStrings, "PermissionStrings is null. Role: " + role.name());

			final Stream<Permission> permissionStream = permissionStrings.stream().map(permissionString -> Permission.valueOf(permissionString));

			return permissionStream;
		}).collect(Collectors.toSet());

		return Collections.unmodifiableSet(permissions);
	}

	public void beforeWrite() {

		int i = 0;
		for (final Role role : getRoles()) {

			switch (i) {
			case 0:
				setRole0(role);
				break;
			case 1:
				setRole1(role);
				break;
			case 2:
				setRole2(role);
				break;
			case 3:
				setRole3(role);
				break;
			case 4:
				setRole4(role);
				break;
			default:

				throw new IllegalStateException(messages.get(Controller.ctx().lang(), MessageKeys.JAVA_ERROR_SIZE, 0, ROLE_COUNT_MAX) + " :" + i);
			}

			i++;
		}
	}

	public void afterRead() {

		final List<Role> roleList = new ArrayList<>();
		roleList.add(getRole0());
		roleList.add(getRole1());
		roleList.add(getRole2());
		roleList.add(getRole3());
		roleList.add(getRole4());

		final Set<Role> roles = roleList.stream().filter(role -> Objects.nonNull(role)).collect(Collectors.toCollection(() -> EnumSet.noneOf(Role.class)));

		if (!(1 <= roles.size())) {

			throw new IllegalStateException(messages.get(Controller.ctx().lang(), MessageKeys.JAVA_ERROR_SIZE, 0, ROLE_COUNT_MAX) + " :" + roles.size());
		}

		setRoles(roles);
	}

	@Override
	public List<ValidationError> validate() {

		LOGGER.info("Relation Validated :" + getClass().getName());
		return null;
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

	@Override
	@Nonnull
	public String getSalt() {

		return salt;
	}

	@Override
	public void setSalt(String salt) {

		this.salt = salt;
	}

	public String getHashedPassword() {

		return hashedPassword;
	}

	@Override
	public void setHashedPassword(String hashedPassword) {

		this.hashedPassword = hashedPassword;
	}

	public Set<Role> getRoles() {

		return roles;
	}

	public void setRoles(Set<Role> roles) {

		this.roles = roles;
	}

	public Theme getTheme() {

		return theme;
	}

	public void setTheme(Theme theme) {

		this.theme = theme;
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

	public Company getCompany() {

		return company;
	}

	public void setCompany(Company company) {

		this.company = company;
	}

	public LocalDateTime getSignInDateTime() {

		return signInDateTime;
	}

	public void setSignInDateTime(LocalDateTime signInDateTime) {

		this.signInDateTime = signInDateTime;
	}

	public LocalDateTime getSignOutDateTime() {

		return signOutDateTime;
	}

	public void setSignOutDateTime(LocalDateTime signOutDateTime) {

		this.signOutDateTime = signOutDateTime;
	}

	@Override
	@Nonnull
	public LocalDateTime getExpireDateTime() {

		return expireDateTime;
	}

	public void setExpireDateTime(LocalDateTime expireDateTime) {

		this.expireDateTime = expireDateTime;
	}

	public LocalDateTime getUpdateDateTime() {

		return updateDateTime;
	}

	public void setUpdateDateTime(LocalDateTime updateDateTime) {

		this.updateDateTime = updateDateTime;
	}

	public Role getRole0() {

		return role0;
	}

	public void setRole0(Role role0) {

		this.role0 = role0;
	}

	public Role getRole1() {

		return role1;
	}

	public void setRole1(Role role1) {

		this.role1 = role1;
	}

	public Role getRole2() {

		return role2;
	}

	public void setRole2(Role role2) {

		this.role2 = role2;
	}

	public Role getRole3() {

		return role3;
	}

	public void setRole3(Role role3) {

		this.role3 = role3;
	}

	public Role getRole4() {

		return role4;
	}

	public void setRole4(Role role4) {

		this.role4 = role4;
	}
}
