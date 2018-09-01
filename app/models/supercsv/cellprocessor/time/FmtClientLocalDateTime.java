package models.supercsv.cellprocessor.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.supercsv.cellprocessor.time.FmtLocalDateTime;
import org.supercsv.util.CsvContext;

import common.utils.DateTimes;

public class FmtClientLocalDateTime extends FmtLocalDateTime {

	public FmtClientLocalDateTime() {
		super();
	}

	public FmtClientLocalDateTime(final DateTimeFormatter formatter) {
		super(formatter);
	}

	@Override
	public Object execute(Object serverDateTime, CsvContext context) {

		if (serverDateTime == null) {

			return super.execute(null, context);
		} else {

			final LocalDateTime clientDateTime = DateTimes.toClientDateTime((LocalDateTime) serverDateTime);

			return super.execute(clientDateTime, context);
		}
	}
}
