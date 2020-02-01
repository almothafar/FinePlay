package models.base;

import java.time.Month;

import javax.annotation.Nullable;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MonthConverter implements AttributeConverter<Month, Integer> {

	@Nullable
	@Override
	public Integer convertToDatabaseColumn(@Nullable final Month month) {

		return (month == null) ? null : month.getValue();
	}

	@Nullable
	@Override
	public Month convertToEntityAttribute(@Nullable final Integer monthInteger) {

		return (monthInteger == null) ? null : Month.of(monthInteger);
	}
}
