package models.user;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.xml.bind.DatatypeConverter;

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

	@Nonnull
	default String toHashedPassword(@Nonnull final String passwordAndSalt) {

		Objects.requireNonNull(passwordAndSalt);

		MessageDigest messageDigest = null;
		try {

			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (final NoSuchAlgorithmException e) {

			throw new IllegalStateException(e);
		}

		messageDigest.update(passwordAndSalt.getBytes(StandardCharsets.UTF_8));
		final byte[] hash = messageDigest.digest();
		if (hash.length != 32) {

			throw new IllegalStateException(": " + hash.length);
		}

		return DatatypeConverter.printHexBinary(hash).toLowerCase();
	}
}
