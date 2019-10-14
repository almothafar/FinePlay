package common.utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import play.mvc.Http.Request;

public class DateTimes {

	private DateTimes() {
	}

	@SuppressWarnings("null")
	@Nonnull
	public static LocalDateTime toClientDateTime(@Nonnull final Request request, @Nullable final LocalDateTime serverDateTime) {

		if (Objects.isNull(serverDateTime)) {

			return null;
		}

		return ZonedDateTime.of(serverDateTime, ZoneOffset.UTC).withZoneSameInstant(getClientZoneId(request)).toLocalDateTime();
	}

	private static final int[] DST_DIFF_MINUTES = { 30, 60 };

	public static boolean isServerDateTimeConvertible(@Nonnull final Request request, @Nullable final LocalDateTime clientDateTime) {

		if (Objects.isNull(clientDateTime)) {

			return true;
		}

		final LocalDateTime serverDateTime = toServerDateTime(request, clientDateTime);
		final LocalDateTime reConvClientDateTime = toClientDateTime(request, serverDateTime);

		final boolean isConvertible = clientDateTime.equals(reConvClientDateTime);

		if (!isConvertible) {

			// daylight saving time start
			return false;
		}

		for (int i = DST_DIFF_MINUTES.length - 1; i >= 0; i--) {

			final int dstDiff = DST_DIFF_MINUTES[i];

			final LocalDateTime afterServerDateTime = toServerDateTime(request, clientDateTime.plusMinutes(dstDiff));

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
	public static LocalDateTime toServerDateTime(@Nonnull final Request request, @Nullable final LocalDateTime clientDateTime) {

		if (Objects.isNull(clientDateTime)) {

			return null;
		}

		return ZonedDateTime.of(clientDateTime, getClientZoneId(request)).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
	}

	// Strict

	@SuppressWarnings("null")
	@Nonnull
	public static ZonedDateTime toClientZonedDateTime(@Nonnull final Request request, @Nonnull final LocalDateTime serverDateTime) {

		Objects.requireNonNull(serverDateTime);

		return ZonedDateTime.of(serverDateTime, ZoneOffset.UTC).withZoneSameInstant(getClientZoneId(request));
	}

	@Nonnull
	public static List<ZonedDateTime> toClientZonedDateTimes(//
			@Nonnull final Request request, @Nonnull final LocalDate clientDate, //
			@Nonnull final LocalTime clientMinTime, @Nonnull final LocalTime clientMaxTime, //
			final int interval) {

		Objects.requireNonNull(request);
		Objects.requireNonNull(clientDate);
		Objects.requireNonNull(clientMinTime);
		Objects.requireNonNull(clientMaxTime);

		final ZoneId clientZoneId = getClientZoneId(request);
		final LocalDateTime clientStartDateTime = toClientStartDateTime(clientDate);
		final ZonedDateTime serverStartZonedDateTime = toServerStartZonedDateTime(clientZoneId, clientStartDateTime);

		final List<ZonedDateTime> clientZonedDateTimes = toClientZonedDateTimes(serverStartZonedDateTime, clientZoneId, interval);
		final List<ZonedDateTime> filterdClientZonedDateTimes = filterClientZonedDateTimes(clientZonedDateTimes, clientMinTime, clientMaxTime);
		return filterdClientZonedDateTimes;
	}

	private static LocalDateTime toClientStartDateTime(final LocalDate clientDate) {

		Objects.nonNull(clientDate);

		return clientDate.atTime(LocalTime.of(0, 0, 0));
	}

	private static ZonedDateTime toServerStartZonedDateTime(final ZoneId clientZoneId, final LocalDateTime clientStartDateTime) {

		Objects.nonNull(clientZoneId);
		Objects.nonNull(clientStartDateTime);

		return ZonedDateTime.of(clientStartDateTime, clientZoneId).withZoneSameInstant(ZoneOffset.UTC);
	}

	private static List<ZonedDateTime> toClientZonedDateTimes(final ZonedDateTime serverStartZonedDateTime, final ZoneId clientZoneId, final int interval) {

		Objects.nonNull(serverStartZonedDateTime);
		Objects.nonNull(clientZoneId);

		final List<ZonedDateTime> serverZonedDateTimes = toServerZonedDateTimes(serverStartZonedDateTime, interval);
		final List<ZonedDateTime> clientZonedDateTimes = serverZonedDateTimes.stream().map(serverZonedDateTime -> serverZonedDateTime.withZoneSameInstant(clientZoneId)).collect(Collectors.toList());

		final ZonedDateTime clientStartZonedDate = clientZonedDateTimes.get(0);
		final LocalDate clientDate = clientStartZonedDate.toLocalDate();
		return clientZonedDateTimes.stream().filter(clientZonedDateTime -> clientDate.equals(clientZonedDateTime.toLocalDate())).collect(Collectors.toList());
	}

	private static List<ZonedDateTime> toServerZonedDateTimes(final ZonedDateTime serverStartZonedDateTime, final int interval) {

		Objects.nonNull(serverStartZonedDateTime);

		return toMinutes(interval).stream().map(minute -> serverStartZonedDateTime.plusMinutes(minute)).collect(Collectors.toList());
	}

	private static List<Integer> toMinutes(final int interval) {

		return IntStream.iterate(0, i -> i + interval).limit(Duration.ofDays(1).toMinutes() / interval).boxed().collect(Collectors.toList());
	}

	private static List<ZonedDateTime> filterClientZonedDateTimes(final List<ZonedDateTime> clientZonedDateTimes, final LocalTime clientMinTime, final LocalTime clientMaxTime) {

		Objects.nonNull(clientZonedDateTimes);
		Objects.nonNull(clientMinTime);
		Objects.nonNull(clientMaxTime);

		return clientZonedDateTimes.stream().filter(clientZonedDateTime -> {

			final LocalTime clientTime = clientZonedDateTime.toLocalTime();
			final boolean inRange = //
					(clientTime.equals(clientMinTime) || clientTime.isAfter(clientMinTime)) && //
			(clientTime.equals(clientMaxTime) || clientTime.isBefore(clientMaxTime));
			return inRange;
		}).collect(Collectors.toList());
	}

	//

	@SuppressWarnings("null")
	@Nonnull
	private static ZoneId getClientZoneId(@Nonnull final Request request) {

		return ZoneId.of(request.session().get(models.user.User_.ZONE_ID).get());
	}
}
