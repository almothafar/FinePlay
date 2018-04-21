package models.user;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.annotation.Nonnull;

import common.utils.Binaries;
import common.utils.Strings;

public interface PasswordHandler {

	final static int SALT_SIZE_MAX = 20;

	default void setPassword(@Nonnull final String password) {

		Objects.requireNonNull(password);

		setSalt(generateSalt());
		final String hashedPassword = toHashedPassword(password + getSalt());
		setHashedPassword(hashedPassword);
	}

	void setSalt(@Nonnull final String generateSalt);

	@Nonnull
	String getSalt();

	void setHashedPassword(@Nonnull final String hashedPassword);

	@Nonnull
	default String generateSalt() {

		return Strings.randomAscii(SALT_SIZE_MAX);
	}

	@SuppressWarnings("null")
	@Nonnull
	default String toHashedPassword(@Nonnull final String passwordAndSalt) {

		Objects.requireNonNull(passwordAndSalt);

		return Binaries.toHexString(Binaries.toHash("SHA-256", passwordAndSalt.getBytes(StandardCharsets.UTF_8)));
	}
}
