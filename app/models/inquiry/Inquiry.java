package models.inquiry;

import java.time.LocalDateTime;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import common.data.validation.Constraints.UserId;
import common.system.MessageKeys;
import models.base.LocaleConverter;
import play.data.validation.Constraints;

@Entity
@Table(name = "INQUIRIES", //
		uniqueConstraints = { @UniqueConstraint(columnNames = { Inquiry_.ID }) }, //
		indexes = { @Index(columnList = Inquiry_.ID) })
public class Inquiry {

	public enum Type {
		QUESTION, OTHER;

		public String getMessageKey() {

			switch (this) {
			case QUESTION:

				return MessageKeys.QUESTION;
			case OTHER:

				return MessageKeys.OTHER;
			default:

				throw new IllegalStateException(this.name());
			}
		}
	}

	@Id
	@GeneratedValue
	@Column(nullable = false)
	private long id;

	@Column(nullable = false)
	private Locale locale;

	@Column(nullable = false, length = 256)
	// Play
	@Constraints.Required
	// System
	@UserId
	private String userId;

	@Column(nullable = true, length = 64)
	private String name;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Type type;

	@Column(nullable = false, length = 64)
	// Play
	@Constraints.Required
	private String title;

	@Column(nullable = false, length = 1000)
	// Play
	@Constraints.Required
	private String content;

	@Column(nullable = false)
	private LocalDateTime dateTime;

	public static final String LOCALE = "locale";
	public static final String USERID = "userId";
	public static final String NAME = "name";
	public static final String TYPE = "type";
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String DATETIME = "dateTime";

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
}
