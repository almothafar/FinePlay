package models.base;

import java.time.ZoneId;

import javax.annotation.Nullable;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ZoneIdConverter implements AttributeConverter<ZoneId, String> {

	@Nullable
	@Override
	public String convertToDatabaseColumn(@Nullable final ZoneId zoneId) {

		return (zoneId == null) ? null : zoneId.getId();
	}

	@Nullable
	@Override
	public ZoneId convertToEntityAttribute(@Nullable final String zoneIdString) {

		return (zoneIdString == null) ? null : ZoneId.of(zoneIdString);
	}
}
