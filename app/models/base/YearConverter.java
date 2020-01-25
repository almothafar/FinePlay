package models.base;

import java.time.Year;

import javax.annotation.Nullable;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class YearConverter implements AttributeConverter<Year, Integer> {

	@Nullable
	@Override
	public Integer convertToDatabaseColumn(@Nullable final Year year) {

		return (year == null) ? null : year.getValue();
	}

	@Nullable
	@Override
	public Year convertToEntityAttribute(@Nullable final Integer yearInteger) {

		return (yearInteger == null) ? null : Year.of(yearInteger);
	}
}
