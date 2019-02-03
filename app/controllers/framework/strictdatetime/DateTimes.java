package controllers.framework.strictdatetime;

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

import play.mvc.Http.Request;

class DateTimes {

	private DateTimes() {
	}

	@SuppressWarnings("null")
	@Nonnull
	public static ZonedDateTime toClientZonedDateTime(@Nonnull final Request request, @Nonnull final LocalDateTime serverDateTime) {

		Objects.requireNonNull(serverDateTime);

		return ZonedDateTime.of(serverDateTime, ZoneOffset.UTC).withZoneSameInstant(getClientZoneId(request));
	}

	@SuppressWarnings("null")
	@Nonnull
	private static ZoneId getClientZoneId(@Nonnull final Request request) {

		return ZoneId.of(request.session().getOptional(models.user.User_.ZONE_ID).get());
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
}
