package models.user;

import java.time.LocalDateTime;

import javax.annotation.Nonnull;

public interface ExpireHandler {

	default boolean isValidExpireTime() {

		return getExpireDateTime().isAfter(LocalDateTime.now());
	}

	@Nonnull
	LocalDateTime getExpireDateTime();
}
