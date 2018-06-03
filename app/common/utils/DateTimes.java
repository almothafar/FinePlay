package common.utils;

import java.time.Duration;
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

	public static boolean isServerDateTimeConvertible(@Nonnull final LocalDateTime clientDateTime) {

		Objects.requireNonNull(clientDateTime);

		final LocalDateTime serverDateTime = getServerDateTime(clientDateTime);
		final LocalDateTime after1HourServerDateTime = getServerDateTime(clientDateTime.plusHours(1));

		final long hours = Duration.between(serverDateTime, after1HourServerDateTime).toHours();
		switch ((int) hours) {
		case 0:

			// daylight saving time start
			return false;
		case 1:

			return true;
		case 2:

			// daylight saving time end
			return false;
		default:

			throw new IllegalStateException("" + hours);
		}
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

		return ZoneId.of(Controller.session(models.user.User_.ZONE_ID));
	}
}
