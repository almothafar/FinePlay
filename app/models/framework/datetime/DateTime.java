package models.framework.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "DATETIMES", uniqueConstraints = { @UniqueConstraint(columnNames = { "USER_ID" }) }, indexes = { @Index(columnList = "USER_ID") })
public class DateTime {

	@Id
	@Column(nullable = false)
	// Play
	private long user_Id;

	private LocalDateTime dateTime;

	private LocalDate date;

	private LocalTime time;

	public static final String USER_ID = "user_Id";
	public static final String DATETIME = "dateTime";
	public static final String DATE = "date";
	public static final String TIME = "time";

	public long getUser_Id() {

		return user_Id;
	}

	public void setUser_Id(long user_Id) {

		this.user_Id = user_Id;
	}

	public LocalDateTime getDateTime() {

		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {

		this.dateTime = dateTime;
	}

	public LocalDate getDate() {

		return date;
	}

	public void setDate(LocalDate date) {

		this.date = date;
	}

	public LocalTime getTime() {

		return time;
	}

	public void setTime(LocalTime time) {

		this.time = time;
	}
}
