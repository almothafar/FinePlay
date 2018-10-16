package controllers.framework.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.security.auth.login.AccountException;

import common.system.MessageKeys;
import common.utils.DateTimes;
import controllers.user.UserService;
import models.framework.datetime.DateTimeFormContent;
import models.system.System.PermissionsAllowed;
import models.user.User;
import models.user.User_;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class DateTime extends Controller {

	@Inject
	private MessagesApi messages;

	@Inject
	private JPAApi jpa;

	@Inject
	private FormFactory formFactory;

	@Inject
	private UserService userService;

	@Authenticated(common.core.Authenticator.class)
	public Result index() {

		return jpa.withTransaction(manager -> {

			final models.framework.datetime.DateTime readedDateTime = readDateTime(manager);
			final DateTimeFormContent readedDateTimeFormContent = setDateTimeFormContentValue(readedDateTime);

			final Form<DateTimeFormContent> readedDateTimeForm = formFactory.form(DateTimeFormContent.class).fill(readedDateTimeFormContent);

			return ok(views.html.framework.datetime.datetime.render(new HashMap<>(), readedDateTimeForm));
		});
	}

	@Nonnull
	private models.framework.datetime.DateTime readDateTime(@Nonnull final EntityManager manager) {

		final User user;
		try {

			user = userService.read(manager, request().session().get(User_.USER_ID));
		} catch (final AccountException e) {

			throw new RuntimeException(e);
		}

		models.framework.datetime.DateTime datetime = manager.find(models.framework.datetime.DateTime.class, user.getId());
		final boolean isExist = Objects.nonNull(datetime);

		if (!isExist) {

			datetime = new models.framework.datetime.DateTime();
			datetime.setUser_Id(user.getId());
		}

		return datetime;
	}

	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result update() {

		return jpa.withTransaction(manager -> {

			final Form<DateTimeFormContent> datetimeForm = formFactory.form(DateTimeFormContent.class).bindFromRequest();
			if (!datetimeForm.hasErrors()) {

				final DateTimeFormContent datetimeFormContent = datetimeForm.get();

				final models.framework.datetime.DateTime updatedDateTime;
				try {

					updatedDateTime = updateDateTime(manager, datetimeFormContent);
				} catch (IllegalStateException e) {

					final Map<String, String> alertInfo = Map.of("dateTimeWarning", "<strong>" + messages.get(lang(), MessageKeys.WARNING) + "</strong> " + e.getLocalizedMessage());

					return failureRead(alertInfo, datetimeForm);
				}
				final DateTimeFormContent updatedDateTimeFormContent = setDateTimeFormContentValue(updatedDateTime);

				final Form<DateTimeFormContent> updatedDateTimeForm = formFactory.form(DateTimeFormContent.class).fill(updatedDateTimeFormContent);

				return ok(views.html.framework.datetime.datetime.render(new HashMap<>(), updatedDateTimeForm));
			} else {

				return failureRead(new HashMap<>(), datetimeForm);
			}
		});
	}

	@Nonnull
	private models.framework.datetime.DateTime updateDateTime(@Nonnull final EntityManager manager, @Nonnull final DateTimeFormContent datetimeFormContent) {

		final LocalDate dateTime_Date = datetimeFormContent.getDateTime_Date();
		final LocalTime dateTime_Time = datetimeFormContent.getDateTime_Time();
		LocalDateTime serverDateTime_DateTime = null;
		if (Objects.nonNull(dateTime_Date) && Objects.nonNull(dateTime_Time)) {

			if (!DateTimes.isServerDateTimeConvertible(LocalDateTime.of(dateTime_Date, dateTime_Time))) {

				throw new IllegalStateException(getIllegalDateTimeMessage());
			}

			serverDateTime_DateTime = DateTimes.toServerDateTime(LocalDateTime.of(dateTime_Date, dateTime_Time));
		}

		final LocalDate date_Date = datetimeFormContent.getDate_Date();

		final LocalDate time_Date = datetimeFormContent.getTime_Date();
		final LocalTime time_Time = datetimeFormContent.getTime_Time();
		LocalTime serverTime = null;
		if (Objects.nonNull(time_Time)) {

			if (!DateTimes.isServerDateTimeConvertible(LocalDateTime.of(time_Date, time_Time))) {

				throw new IllegalStateException(getIllegalDateTimeMessage());
			}

			final LocalDateTime serverTime_DateTime = DateTimes.toServerDateTime(LocalDateTime.of(time_Date, time_Time));
			serverTime = serverTime_DateTime.toLocalTime();
		}

		final User user;
		try {

			user = userService.read(manager, request().session().get(User_.USER_ID));
		} catch (final AccountException e) {

			throw new RuntimeException(e);
		}

		models.framework.datetime.DateTime datetime = manager.find(models.framework.datetime.DateTime.class, user.getId());
		final boolean isExist = Objects.nonNull(datetime);

		if (!isExist) {

			datetime = new models.framework.datetime.DateTime();
			datetime.setUser_Id(user.getId());
		}

		datetime.setDateTime(serverDateTime_DateTime);
		datetime.setDate(date_Date);
		datetime.setTime(serverTime);

		if (!isExist) {

			manager.persist(datetime);
		} else {

			manager.merge(datetime);
		}
		return datetime;
	}

	private String getIllegalDateTimeMessage() {

		final String message = messages.get(lang(), MessageKeys.SYSTEM_ERROR_X_ILLEGAL, messages.get(lang(), MessageKeys.DATETIME)) + //
				"(" + //
				messages.get(lang(), MessageKeys.X__IS__X, //
						messages.get(lang(), MessageKeys.DATETIME), //
						messages.get(lang(), MessageKeys.X__OF__X, //
								messages.get(lang(), MessageKeys.X__OR__X, //
										messages.get(lang(), MessageKeys.START), //
										messages.get(lang(), MessageKeys.END)), //
								messages.get(lang(), MessageKeys.DAYLIGHT__SAVING__TIME)))
				+ ")";
		return message;
	}

	@Nonnull
	private DateTimeFormContent setDateTimeFormContentValue(@Nonnull final models.framework.datetime.DateTime datetime) {

		Objects.requireNonNull(datetime);

		final DateTimeFormContent datetimeFormContent = new DateTimeFormContent();

		if (Objects.nonNull(datetime.getDateTime())) {

			final LocalDateTime clientDateTime = DateTimes.toClientDateTime(datetime.getDateTime());
			datetimeFormContent.setDateTime_Date_submit(clientDateTime.toLocalDate());
			datetimeFormContent.setDateTime_Time_submit(clientDateTime.toLocalTime());
		}

		datetimeFormContent.setDate_Date_submit(datetime.getDate());

		if (Objects.nonNull(datetime.getTime())) {

			final LocalDateTime clientDateTime = DateTimes.toClientDateTime(LocalDateTime.of(LocalDate.now(), datetime.getTime()));
			datetimeFormContent.setTime_Date(clientDateTime.toLocalDate());
			datetimeFormContent.setTime_Time_submit(clientDateTime.toLocalTime());
		} else {

			final LocalDateTime clientDateTime = DateTimes.toClientDateTime(LocalDateTime.now());
			datetimeFormContent.setTime_Date(clientDateTime.toLocalDate());
		}

		return datetimeFormContent;
	}

	@Nonnull
	private Result failureRead(@Nonnull final Map<String, String> alertInfo, @Nonnull final Form<DateTimeFormContent> datetimeForm) {

		return badRequest(views.html.framework.datetime.datetime.render(alertInfo, datetimeForm));
	}
}
