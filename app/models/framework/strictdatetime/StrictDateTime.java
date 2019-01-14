package models.framework.strictdatetime;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "STRICT_DATETIMES", //
		uniqueConstraints = { @UniqueConstraint(columnNames = { StrictDateTime_.USER__ID }) }, //
		indexes = { @Index(columnList = StrictDateTime_.USER__ID) })
public class StrictDateTime {

	@Id
	@Column(nullable = false)
	// Play
	private long user_Id;

	private LocalDateTime dateTime;

	public static final String USER_ID = "user_Id";
	public static final String DATETIME = "dateTime";

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
}
