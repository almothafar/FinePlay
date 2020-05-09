package common.tools;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import models.user.User;
import models.user.User.Role;
import models.user.User.Theme;

class InitialUsersCreator_prod extends InitialUsersCreator {

	public static void main(String[] args) throws IOException {

		final InitialUsersCreator creator = new InitialUsersCreator_prod();
		creator.createSQL();
	}

	@Override
	protected String getFolderName() {

		return "prod";
	}

	@Override
	protected List<User> createUsers() {

		final List<User> users = new ArrayList<>();

		final User adminUser = new User();
		adminUser.setUserId("admin@example.com");
		adminUser.setPassword("admin1!aA");
		adminUser.setRoles(EnumSet.of(Role.ADMIN));
		adminUser.setLocale(Locale.US);
		adminUser.setZoneId(ZoneId.of("UTC"));
		adminUser.setTheme(Theme.DEFAULT);
		adminUser.setExpireDateTime(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
		users.add(adminUser);

		final User customerUser = new User();
		customerUser.setUserId("customer@example.com");
		customerUser.setPassword("customer1!aA");
		customerUser.setRoles(EnumSet.of(Role.CUSTOMER));
		customerUser.setLocale(Locale.US);
		customerUser.setZoneId(ZoneId.of("UTC"));
		customerUser.setTheme(Theme.DEFAULT);
		customerUser.setExpireDateTime(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
		users.add(customerUser);

		final User guestUser = new User();
		guestUser.setUserId("guest@example.com");
		guestUser.setPassword("guest1!aA");
		guestUser.setRoles(EnumSet.of(Role.GUEST));
		guestUser.setLocale(Locale.US);
		guestUser.setZoneId(ZoneId.of("UTC"));
		guestUser.setTheme(Theme.DEFAULT);
		guestUser.setExpireDateTime(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
		users.add(guestUser);

		final User adminJaJpUser = new User();
		adminJaJpUser.setUserId("adminjajp@example.com");
		adminJaJpUser.setPassword("adminjajp1!aA");
		adminJaJpUser.setRoles(EnumSet.of(Role.ADMIN));
		adminJaJpUser.setLocale(Locale.JAPAN);
		adminJaJpUser.setZoneId(ZoneId.of("Asia/Tokyo"));
		adminJaJpUser.setTheme(Theme.DEFAULT);
		adminJaJpUser.setExpireDateTime(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
		users.add(adminJaJpUser);

		return Collections.unmodifiableList(users);
	}

	@Override
	protected void writeSQL(final Path loadSQLPath, final List<String> insertSQLLines) throws IOException {

		Files.write(loadSQLPath, insertSQLLines, StandardCharsets.UTF_8);
//		Files.write(loadSQLPath, String.format("UPDATE HIBERNATE_SEQUENCE SET NEXT_VAL=(%d)", insertSQLLines.size() + 1).getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
		Files.write(loadSQLPath, String.format("ALTER SEQUENCE IF EXISTS HIBERNATE_SEQUENCE RESTART WITH %d", insertSQLLines.size() + 1).getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
	}
}
