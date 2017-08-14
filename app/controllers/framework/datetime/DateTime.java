package controllers.framework.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.security.auth.login.AccountException;

import common.utils.DateTimes;
import controllers.user.UserService;
import play.mvc.Controller;
import models.framework.datetime.DateTimeFormContent;
import models.system.System.PermissionsAllowed;
import models.user.User;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class DateTime extends Controller {

	@Inject
	private JPAApi jpaApi;

	@Inject
	private FormFactory formFactory;

	@Inject
	private UserService userService;

	@Authenticated(common.core.Authenticator.class)
	@Transactional()
	public Result index() {

		final models.framework.datetime.DateTime readedDateTime = readDateTime(jpaApi.em());
		final DateTimeFormContent readedDateTimeFormContent = setDateTimeFormContentValue(readedDateTime);

		final Form<DateTimeFormContent> readedDateTimeForm = formFactory.form(DateTimeFormContent.class).fill(readedDateTimeFormContent);

		return ok(views.html.framework.datetime.datetime.render(readedDateTimeForm));
	}

	@Nonnull
	private models.framework.datetime.DateTime readDateTime(@Nonnull final EntityManager manager) {

		final User user;
		try {

			user = userService.read(manager, session(User.USERID));
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
	@Transactional()
	@RequireCSRFCheck
	public Result update() {

		final Form<DateTimeFormContent> datetimeForm = formFactory.form(DateTimeFormContent.class).bindFromRequest();
		if (!datetimeForm.hasErrors()) {

			final DateTimeFormContent datetimeFormContent = datetimeForm.get();

			final models.framework.datetime.DateTime updatedDateTime = updateDateTime(jpaApi.em(), datetimeFormContent);
			final DateTimeFormContent updatedDateTimeFormContent = setDateTimeFormContentValue(updatedDateTime);

			final Form<DateTimeFormContent> updatedDateTimeForm = formFactory.form(DateTimeFormContent.class).fill(updatedDateTimeFormContent);

			return ok(views.html.framework.datetime.datetime.render(updatedDateTimeForm));
		} else {

			return failureRead(datetimeForm);
		}
	}

	@Nonnull
	private models.framework.datetime.DateTime updateDateTime(@Nonnull final EntityManager manager, @Nonnull final DateTimeFormContent datetimeFormContent) {

		final LocalDate dateTime_Date = datetimeFormContent.getDateTime_Date();
		final LocalTime dateTime_Time = datetimeFormContent.getDateTime_Time();
		LocalDateTime serverDateTime_DateTime = null;
		if (Objects.nonNull(dateTime_Date) && Objects.nonNull(dateTime_Time)) {

			serverDateTime_DateTime = DateTimes.getServerDateTime(LocalDateTime.of(dateTime_Date, dateTime_Time));
		}

		final LocalDate date_Date = datetimeFormContent.getDate_Date();

		final LocalDate time_Date = datetimeFormContent.getTime_Date();
		final LocalTime time_Time = datetimeFormContent.getTime_Time();
		LocalTime serverTime = null;
		if (Objects.nonNull(time_Time)) {

			final LocalDateTime serverTime_DateTime = DateTimes.getServerDateTime(LocalDateTime.of(time_Date, time_Time));
			serverTime = serverTime_DateTime.toLocalTime();
		}

		final User user;
		try {

			user = userService.read(manager, session(User.USERID));
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

	@Nonnull
	private DateTimeFormContent setDateTimeFormContentValue(@Nonnull final models.framework.datetime.DateTime datetime) {

		Objects.requireNonNull(datetime);

		final DateTimeFormContent datetimeFormContent = new DateTimeFormContent();

		if (Objects.nonNull(datetime.getDateTime())) {

			final LocalDateTime clientDateTime = DateTimes.getClientDateTime(datetime.getDateTime());
			datetimeFormContent.setDateTime_Date_submit(clientDateTime.toLocalDate());
			datetimeFormContent.setDateTime_Time_submit(clientDateTime.toLocalTime());
		}

		datetimeFormContent.setDate_Date_submit(datetime.getDate());

		if (Objects.nonNull(datetime.getTime())) {

			final LocalDateTime clientDateTime = DateTimes.getClientDateTime(LocalDateTime.of(LocalDate.now(), datetime.getTime()));
			datetimeFormContent.setTime_Date(clientDateTime.toLocalDate());
			datetimeFormContent.setTime_Time_submit(clientDateTime.toLocalTime());
		} else {

			final LocalDateTime clientDateTime = DateTimes.getClientDateTime(LocalDateTime.now());
			datetimeFormContent.setTime_Date(clientDateTime.toLocalDate());
		}

		return datetimeFormContent;
	}

	@Nonnull
	private Result failureRead(@Nonnull final Form<DateTimeFormContent> datetimeForm) {

		return badRequest(views.html.framework.datetime.datetime.render(datetimeForm));
	}
}
