package common.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.annotation.Nonnull;

import play.mvc.Controller;

public class DateTimes {

	private DateTimes() {
	}

	@SuppressWarnings("null")
	@Nonnull
	public static LocalDateTime getClientDateTime(@Nonnull final LocalDateTime serverDateTime) {

		Objects.requireNonNull(serverDateTime);

		return ZonedDateTime.of(serverDateTime, ZoneOffset.UTC).withZoneSameInstant(getClientZoneId()).toLocalDateTime();
	}

	@SuppressWarnings("null")
	@Nonnull
	public static LocalDateTime getServerDateTime(@Nonnull final LocalDateTime clientDateTime) {

		Objects.requireNonNull(clientDateTime);

		return ZonedDateTime.of(clientDateTime, getClientZoneId()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
	}

	@SuppressWarnings("null")
	@Nonnull
	private static ZoneId getClientZoneId() {

		return ZoneId.of(Controller.session(models.user.User.ZONEID));
	}
}
