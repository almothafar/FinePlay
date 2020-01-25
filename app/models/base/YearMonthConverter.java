package models.base;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.function.Function;

import javax.annotation.Nullable;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class YearMonthConverter implements AttributeConverter<YearMonth, Date> {

	@Nullable
	@Override
	public Date convertToDatabaseColumn(@Nullable final YearMonth yearMonth) {

		return (yearMonth == null) ? null : Date.valueOf(yearMonth.atDay(1));
	}

	@Nullable
	@Override
	public YearMonth convertToEntityAttribute(@Nullable final Date yearMonthDate) {

		final Function<Date, YearMonth> toYearMonth = (date) -> {

			final LocalDate localDate = yearMonthDate.toLocalDate();
			return YearMonth.of(localDate.getYear(), localDate.getMonth());
		};
		return (yearMonthDate == null) ? null : toYearMonth.apply(yearMonthDate);
	}
}
