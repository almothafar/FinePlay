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

	private static final int[] DST_DIFF_MINUTES = { 30, 60/* , 90 *//* , 120 */ };

	public static boolean isServerDateTimeConvertible(@Nonnull final LocalDateTime clientDateTime) {

		Objects.requireNonNull(clientDateTime);

		final LocalDateTime serverDateTime = getServerDateTime(clientDateTime);
		final LocalDateTime reConvClientDateTime = getClientDateTime(serverDateTime);

		final boolean isConvertible = clientDateTime.equals(reConvClientDateTime);

		if (!isConvertible) {

			// daylight saving time start
			return false;
		}

		for (int i = DST_DIFF_MINUTES.length - 1; i >= 0; i--) {

			final int dstDiff = DST_DIFF_MINUTES[i];

			final LocalDateTime afterServerDateTime = getServerDateTime(clientDateTime.plusMinutes(dstDiff));

			final long minutes = Duration.between(serverDateTime, afterServerDateTime).toMinutes();
			final boolean isNormal = dstDiff == minutes;

			if (!isNormal) {

				if (minutes == dstDiff * 2) {

					// daylight saving time end
					return false;
				} else {

					// next diff loop
				}
			}
		}

		return true;
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
