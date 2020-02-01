package models.framework.entitytype;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@javax.persistence.Entity
@Table(name = Entity.NAME, //
		uniqueConstraints = { @UniqueConstraint(columnNames = { Entity_.USER__ID }) }, //
		indexes = { @Index(columnList = Entity_.USER__ID) })
public class Entity {

	@Id
	@Column(nullable = false)
	// Play
	private long user_Id;

	private Locale locale;

	private ZoneId zoneId;

	private Year year;
	private YearMonth yearMonth;
	private Month month;
	@Enumerated(EnumType.STRING)
	private DayOfWeek dayOfWeek;

	private LocalDateTime dateTime;
	private LocalDate date;
	private LocalTime time;

	public static final String NAME = "ENTITIES";

	public long getUser_Id() {
		return user_Id;
	}
	public void setUser_Id(long user_Id) {
		this.user_Id = user_Id;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public ZoneId getZoneId() {
		return zoneId;
	}
	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}
	public Year getYear() {
		return year;
	}
	public void setYear(Year year) {
		this.year = year;
	}
	public YearMonth getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(YearMonth yearMonth) {
		this.yearMonth = yearMonth;
	}
	public Month getMonth() {
		return month;
	}
	public void setMonth(Month month) {
		this.month = month;
	}
	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
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
	@Override
	public String toString() {
		return "Entity [user_Id=" + user_Id + ", locale=" + locale + ", zoneId=" + zoneId + ", year=" + year + ", yearMonth=" + yearMonth + ", month=" + month + ", dayOfWeek=" + dayOfWeek + ", dateTime=" + dateTime + ", date=" + date + ", time=" + time + "]";
	}
}
