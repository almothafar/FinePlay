package models.supercsv.cellprocessor.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.supercsv.cellprocessor.time.ParseLocalDateTime;

import common.utils.DateTimes;

public class ParseServerLocalDateTime extends ParseLocalDateTime {

	public ParseServerLocalDateTime() {
	}

	public ParseServerLocalDateTime(final DateTimeFormatter formatter) {
		super(formatter);
	}

	@SuppressWarnings("null")
	@Override
	protected LocalDateTime parse(String string) {

		return DateTimes.getServerDateTime(super.parse(string));
	}

	@SuppressWarnings("null")
	@Override
	protected LocalDateTime parse(String string, DateTimeFormatter formatter) {

		return DateTimes.getServerDateTime(super.parse(string, formatter));
	}
}
