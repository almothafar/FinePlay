package models.user;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.mindrot.jbcrypt.BCrypt;

public interface PasswordHandler {

	default void setPassword(@Nonnull final String password) {

		Objects.requireNonNull(password);

		final String salt = generateSalt();
		final String hashedPassword = toHashedPassword(password, salt);
		setHashedPassword(hashedPassword);
	}

	void setHashedPassword(@Nonnull final String hashedPassword);

	@Nonnull
	default String generateSalt() {

		return BCrypt.gensalt();
	}

	@SuppressWarnings("null")
	@Nonnull
	default String toHashedPassword(@Nonnull final String password, @Nonnull final String salt) {

		Objects.requireNonNull(password);
		Objects.requireNonNull(salt);

		return BCrypt.hashpw(password, salt);
	}
}
