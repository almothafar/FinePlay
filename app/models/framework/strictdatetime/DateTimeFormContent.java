package models.framework.strictdatetime;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public class DateTimeFormContent {

	private LocalDate dateTime_Date;
	private ZonedDateTime dateTime_DateTime;

	public static final String DATETIME_DATE = "dateTime_Date";
	public static final String DATETIME_DATETIME = "dateTime_DateTime";

	public String validate() {

		if (Objects.isNull(getDateTime_Date()) && Objects.isNull(getDateTime_DateTime())) {

			return null;
		} else if (Objects.nonNull(getDateTime_Date()) && Objects.nonNull(getDateTime_DateTime())) {

			return null;
		} else if (Objects.isNull(getDateTime_Date()) && Objects.nonNull(getDateTime_DateTime())) {

			return "Date of DateTime is empty.";
		} else if (Objects.nonNull(getDateTime_Date()) && Objects.isNull(getDateTime_DateTime())) {

			return "Time of DateTime is empty.";
		}

		return null;
	}

	private static final DateTimeFormatter CUSTOM_DATE = DateTimeFormatter.ofPattern("yyyy/MM/dd");

	public String getDateTime_Date_receive() {

		return Objects.nonNull(getDateTime_Date()) ? CUSTOM_DATE.format(getDateTime_Date()) : "";
	}

	public String getDateTime_DateTime_receive() {

		return Objects.nonNull(getDateTime_DateTime()) ? getDateTime_DateTime().toString() : "";
	}

	public LocalDate getDateTime_Date() {

		return dateTime_Date;
	}

	public void setDateTime_Date_submit(LocalDate dateTime_Date) {

		this.dateTime_Date = dateTime_Date;
	}

	public ZonedDateTime getDateTime_DateTime() {

		return dateTime_DateTime;
	}

	public void setDateTime_DateTime_submit(ZonedDateTime dateTime_DateTime) {

		this.dateTime_DateTime = dateTime_DateTime;
	}
}
