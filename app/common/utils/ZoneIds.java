package common.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

public class ZoneIds {

	private ZoneIds() {
	}

	@SuppressWarnings("null")
	@Nonnull
	public static Map<String, String> getZoneIdToNameMap(@Nonnull final Locale locale) {

		Objects.requireNonNull(locale);

		final Map<String, String> zoneIdToNameMap = ZoneId.getAvailableZoneIds()//
				.stream()//
				.map(id -> ZoneId.of(id))//
				.sorted((ZoneId z0, ZoneId z1) -> {

					final LocalDateTime currentDateTime = LocalDateTime.now();
					final int offset0 = z0.getRules().getOffset(currentDateTime).getTotalSeconds();
					final int offset1 = z1.getRules().getOffset(currentDateTime).getTotalSeconds();

					final int offsetCompared = Integer.compare(offset0, offset1);
					if (offsetCompared != 0) {

						return offsetCompared;
					}

					final String displayName0 = z0.getDisplayName(TextStyle.FULL, locale);
					final String displayName1 = z1.getDisplayName(TextStyle.FULL, locale);

					return displayName0.compareTo(displayName1);
				})//
				.collect(Collectors.toMap(//
						zoneId -> zoneId.toString(), //
						zoneId -> zoneId.getRules().getOffset(LocalDateTime.now()) + " " + zoneId.getDisplayName(TextStyle.FULL, locale) + " (" + zoneId.toString() + ")", //
						(u, v) -> {
							throw new IllegalStateException(v);
						}, //
						LinkedHashMap::new));

		return zoneIdToNameMap;
	}
}
