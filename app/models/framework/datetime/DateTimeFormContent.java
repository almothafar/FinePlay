package models.framework.datetime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateTimeFormContent {

	private LocalDate dateTime_Date;
	private LocalTime dateTime_Time;

	private LocalDate date_Date;

	private LocalDate time_Date;
	private LocalTime time_Time;

	public static final String DATETIME_DATE = "dateTime_Date";
	public static final String DATETIME_TIME = "dateTime_Time";

	public static final String DATE_DATE = "date_Date";

	public static final String TIME_DATE = "time_Date";
	public static final String TIME_TIME = "time_Time";

	public String validate() {

		if (Objects.isNull(getDateTime_Date()) && Objects.isNull(getDateTime_Time())) {

			return null;
		} else if (Objects.nonNull(getDateTime_Date()) && Objects.nonNull(getDateTime_Time())) {

			return null;
		} else if (Objects.isNull(getDateTime_Date()) && Objects.nonNull(getDateTime_Time())) {

			return "Date of DateTime is empty.";
		} else if (Objects.nonNull(getDateTime_Date()) && Objects.isNull(getDateTime_Time())) {

			return "Time of DateTime is empty.";
		}

		return null;
	}

	private static final DateTimeFormatter CUSTOM_DATE = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	private static final DateTimeFormatter CUSTOM_TIME = DateTimeFormatter.ofPattern("HH:mm");

	public String getDateTime_Date_receive() {

		return Objects.nonNull(getDateTime_Date()) ? CUSTOM_DATE.format(getDateTime_Date()) : "";
	}

	public String getDateTime_Time_receive() {

		return Objects.nonNull(getDateTime_Time()) ? CUSTOM_TIME.format(getDateTime_Time()) : "";
	}

	public String getDate_Date_receive() {

		return Objects.nonNull(getDate_Date()) ? CUSTOM_DATE.format(getDate_Date()) : "";
	}

	public String getTime_Time_receive() {

		return Objects.nonNull(getTime_Time()) ? CUSTOM_TIME.format(getTime_Time()) : "";
	}

	public LocalDate getDateTime_Date() {

		return dateTime_Date;
	}

	public void setDateTime_Date_submit(LocalDate dateTime_Date) {

		this.dateTime_Date = dateTime_Date;
	}

	public LocalTime getDateTime_Time() {

		return dateTime_Time;
	}

	public void setDateTime_Time_submit(LocalTime dateTime_Time) {

		this.dateTime_Time = dateTime_Time;
	}

	public LocalDate getDate_Date() {

		return date_Date;
	}

	public void setDate_Date_submit(LocalDate date_Date) {

		this.date_Date = date_Date;
	}

	public LocalDate getTime_Date() {

		return time_Date;
	}

	public void setTime_Date(LocalDate time_Date) {

		this.time_Date = time_Date;
	}

	public LocalTime getTime_Time() {

		return time_Time;
	}

	public void setTime_Time_submit(LocalTime time_Time) {

		this.time_Time = time_Time;
	}
}
