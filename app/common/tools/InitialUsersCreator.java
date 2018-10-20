package common.tools;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import common.utils.JSONs;
import models.user.User;
import models.user.User.Role;
import models.user.User.Theme;

class InitialUsersCreator {

	private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args) throws IOException {

		final InitialUsersCreator creator = new InitialUsersCreator();
		creator.createSQL();
		creator.createJSON();
	}

	protected void createSQL() throws IOException {

		final Path loadPath = getSQLPath();
		if (!Files.exists(loadPath)) {

			throw new RuntimeException(": " + loadPath);
		}

		final List<User> users = createUsers();

		final List<String> insertLines = createInsertLines(users);

		writeSQL(loadPath, insertLines);
	}

	protected void createJSON() throws IOException {

		final Path usersPath = getJSONPath();
		if (!Files.exists(usersPath)) {

			throw new RuntimeException(": " + usersPath);
		}

		final List<User> users = createUsers();

		writeJSON(usersPath, users);
	}

	private static User createUser() {

		return new User() {

			private String password;

			@Override
			public void setPassword(@Nonnull String password) {

				super.setPassword(password);
				this.password = password;
			}

			@SuppressWarnings("unused")
			public String getPassword() {

				return this.password;
			}
		};
	}

	protected List<User> createUsers() {

		final List<User> users = new ArrayList<>();

		final User adminUser = createUser();
		adminUser.setUserId("admin@example.com");
		adminUser.setPassword("admin1!aA");
		adminUser.setRoles(EnumSet.of(Role.ADMIN));
		adminUser.setLocale(Locale.US);
		adminUser.setZoneId(ZoneId.of("US/Pacific"));
		adminUser.setTheme(Theme.DEFAULT);
		adminUser.setExpireDateTime(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
		adminUser.setUpdateDateTime(LocalDateTime.now());
		users.add(adminUser);

		final User customerUser = createUser();
		customerUser.setUserId("customer@example.com");
		customerUser.setPassword("customer1!aA");
		customerUser.setRoles(EnumSet.of(Role.CUSTOMER));
		customerUser.setLocale(Locale.US);
		customerUser.setZoneId(ZoneId.of("US/Pacific"));
		customerUser.setTheme(Theme.DEFAULT);
		customerUser.setExpireDateTime(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
		customerUser.setUpdateDateTime(LocalDateTime.now());
		users.add(customerUser);

		final User guestUser = createUser();
		guestUser.setUserId("guest@example.com");
		guestUser.setPassword("guest1!aA");
		guestUser.setRoles(EnumSet.of(Role.GUEST));
		guestUser.setLocale(Locale.US);
		guestUser.setZoneId(ZoneId.of("US/Pacific"));
		guestUser.setTheme(Theme.DEFAULT);
		guestUser.setExpireDateTime(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
		users.add(guestUser);

		final User adminJaJpUser = createUser();
		adminJaJpUser.setUserId("adminjajp@example.com");
		adminJaJpUser.setPassword("adminjajp1!aA");
		adminJaJpUser.setRoles(EnumSet.of(Role.ADMIN));
		adminJaJpUser.setLocale(Locale.JAPAN);
		adminJaJpUser.setZoneId(ZoneId.of("Asia/Tokyo"));
		adminJaJpUser.setTheme(Theme.DEFAULT);
		adminJaJpUser.setExpireDateTime(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
		adminJaJpUser.setUpdateDateTime(LocalDateTime.now());
		users.add(adminJaJpUser);

		final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		final LocalDateTime now = LocalDateTime.now();
		users.addAll(IntStream.range(0, 100).mapToObj(i -> {

			final User user = createUser();
			user.setUserId("user" + i + "@example.com");
			user.setPassword("user" + i + "1!aA");
			user.setRoles(EnumSet.of(Role.values()[i % Role.values().length]));
			user.setLocale(Locale.US);
			user.setZoneId(ZoneId.of("UTC"));
			user.setTheme(Theme.DEFAULT);
			user.setExpireDateTime(LocalDateTime.of(now.getYear() + i, Month.JANUARY, 1, 0, 0));
			final Set<ConstraintViolation<User>> violations = validator.validate(user);
			violations.stream().forEach(violation -> {

				throw new RuntimeException(violation.getMessage());
			});

			return user;
		}).collect(Collectors.toList()));

		return Collections.unmodifiableList(users);
	}

	protected List<String> createInsertLines(final List<User> users) {

		return IntStream.range(0, users.size()).mapToObj(i -> {

			final User user = users.get(i);

			final List<String> lines = new ArrayList<>();

			user.setId(i + 1);
			final String userLine = String.format("INSERT INTO USERS (ID, USERID, SALT, HASHEDPASSWORD, LOCALE, ZONEID, THEME, EXPIREDATETIME, SIGNINDATETIME, SIGNOUTDATETIME, UPDATEDATETIME) VALUES (%d, %s, %s, %s, %s, %s, %s, %s, %s, %s, CURRENT_TIMESTAMP)", //
					user.getId(), //
					"'" + user.getUserId() + "'", //
					"'" + user.getSalt().replace("'", "''") + "'", //
					"'" + user.getHashedPassword().replace("'", "''") + "'", //
					"'" + user.getLocale().toLanguageTag() + "'", //
					"'" + user.getZoneId().getId() + "'", //
					"'" + user.getTheme().name() + "'", //
					"'" + FORMATTER.format(user.getExpireDateTime()) + "'", //
					user.getSignInDateTime() != null ? "'" + FORMATTER.format(user.getSignInDateTime()) + "'" : "NULL", //
					user.getSignOutDateTime() != null ? "'" + FORMATTER.format(user.getSignOutDateTime()) + "'" : "NULL");
			lines.add(userLine);

			final List<String> userRoleLines = user.getRoles().stream().map(role -> {

				final String userRoleLine = String.format("INSERT INTO USER_ROLES (USER_ID, ROLE) VALUES (%s, %s)", //
						user.getId(), //
						"'" + role + "'");

				return userRoleLine;
			}).collect(Collectors.toList());
			lines.addAll(userRoleLines);

			return lines.stream().collect(Collectors.joining("\r\n"));
		}).collect(Collectors.toList());
	}

	protected String getFolderName() {

		return "dev";
	}

	protected Path getSQLPath() {

		return Paths.get(".", getFolderName(), "load.sql");
	}

	protected void writeSQL(final Path loadSQLPath, final List<String> insertSQLLines) throws IOException {

		Files.write(loadSQLPath, insertSQLLines, StandardCharsets.UTF_8);
		Files.write(loadSQLPath, String.format("ALTER SEQUENCE IF EXISTS HIBERNATE_SEQUENCE RESTART WITH (%d)", insertSQLLines.size() + 1).getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
	}

	protected Path getJSONPath() {

		return Paths.get(".", getFolderName(), "users.json");
	}

	private void writeJSON(final Path usersPath, List<User> users) throws IOException {

		@SuppressWarnings("null")
		final String usersJson = JSONs.toJSON(users);
		Files.write(usersPath, usersJson.getBytes(StandardCharsets.UTF_8));
	}
}
