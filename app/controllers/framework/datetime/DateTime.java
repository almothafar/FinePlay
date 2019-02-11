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
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Result;
import play.i18n.Messages;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class DateTime extends Controller {

	@Inject
	private MessagesApi messagesApi;

	@Inject
	private JPAApi jpaApi;

	@Inject
	private FormFactory formFactory;

	@Inject
	private UserService userService;

	@Authenticated(common.core.Authenticator.class)
	public Result index(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return jpaApi.withTransaction(manager -> {

			final models.framework.datetime.DateTime readedDateTime = readDateTime(manager, request, messages);
			final DateTimeFormContent readedDateTimeFormContent = setDateTimeFormContentValue(request, readedDateTime);

			final Form<DateTimeFormContent> readedDateTimeForm = formFactory.form(DateTimeFormContent.class).fill(readedDateTimeFormContent);

			return ok(views.html.framework.datetime.datetime.render(new HashMap<>(), readedDateTimeForm, request, lang, messages));
		});
	}

	@Nonnull
	private models.framework.datetime.DateTime readDateTime(@Nonnull final EntityManager manager, @Nonnull final Request request, @Nonnull Messages messages) {

		final User user;
		try {

			user = userService.read(manager, messages, request.session().getOptional(User_.USER_ID).get());
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
	public Result update(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return jpaApi.withTransaction(manager -> {

			final Form<DateTimeFormContent> datetimeForm = formFactory.form(DateTimeFormContent.class).bindFromRequest(request);
			if (!datetimeForm.hasErrors()) {

				final DateTimeFormContent datetimeFormContent = datetimeForm.get();

				final models.framework.datetime.DateTime updatedDateTime;
				try {

					updatedDateTime = updateDateTime(manager, datetimeFormContent, request, messages);
				} catch (IllegalStateException e) {

					final Map<String, String> alertInfo = new HashMap<String, String>() {
						{
							put("dateTimeWarning", "<strong>" + messages.at(MessageKeys.WARNING) + "</strong> " + e.getLocalizedMessage());
						}
					};

					return failureRead(alertInfo, datetimeForm, request, lang, messages);
				}
				final DateTimeFormContent updatedDateTimeFormContent = setDateTimeFormContentValue(request, updatedDateTime);

				final Form<DateTimeFormContent> updatedDateTimeForm = formFactory.form(DateTimeFormContent.class).fill(updatedDateTimeFormContent);

				return ok(views.html.framework.datetime.datetime.render(new HashMap<>(), updatedDateTimeForm, request, lang, messages));
			} else {

				return failureRead(new HashMap<>(), datetimeForm, request, lang, messages);
			}
		});
	}

	@Nonnull
	private models.framework.datetime.DateTime updateDateTime(@Nonnull final EntityManager manager, @Nonnull final DateTimeFormContent datetimeFormContent, @Nonnull final Request request, @Nonnull final Messages messages) {

		final LocalDate dateTime_Date = datetimeFormContent.getDateTime_Date();
		final LocalTime dateTime_Time = datetimeFormContent.getDateTime_Time();
		LocalDateTime serverDateTime_DateTime = null;
		if (Objects.nonNull(dateTime_Date) && Objects.nonNull(dateTime_Time)) {

			if (!DateTimes.isServerDateTimeConvertible(request, LocalDateTime.of(dateTime_Date, dateTime_Time))) {

				throw new IllegalStateException(getIllegalDateTimeMessage(messages));
			}

			serverDateTime_DateTime = DateTimes.toServerDateTime(request, LocalDateTime.of(dateTime_Date, dateTime_Time));
		}

		final LocalDate date_Date = datetimeFormContent.getDate_Date();

		final LocalDate time_Date = datetimeFormContent.getTime_Date();
		final LocalTime time_Time = datetimeFormContent.getTime_Time();
		LocalTime serverTime = null;
		if (Objects.nonNull(time_Time)) {

			if (!DateTimes.isServerDateTimeConvertible(request, LocalDateTime.of(time_Date, time_Time))) {

				throw new IllegalStateException(getIllegalDateTimeMessage(messages));
			}

			final LocalDateTime serverTime_DateTime = DateTimes.toServerDateTime(request, LocalDateTime.of(time_Date, time_Time));
			serverTime = serverTime_DateTime.toLocalTime();
		}

		final User user;
		try {

			user = userService.read(manager, messages, request.session().getOptional(User_.USER_ID).get());
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

	private String getIllegalDateTimeMessage(@Nonnull final Messages messages) {

		final String message = messages.at(MessageKeys.SYSTEM_ERROR_X_ILLEGAL, messages.at(MessageKeys.DATETIME)) + //
				"(" + //
				messages.at(MessageKeys.X__IS__X, //
						messages.at(MessageKeys.DATETIME), //
						messages.at(MessageKeys.X__OF__X, //
								messages.at(MessageKeys.X__OR__X, //
										messages.at(MessageKeys.START), //
										messages.at(MessageKeys.END)), //
								messages.at(MessageKeys.DAYLIGHT__SAVING__TIME)))
				+ ")";
		return message;
	}

	@Nonnull
	private DateTimeFormContent setDateTimeFormContentValue(@Nonnull final Request request, @Nonnull final models.framework.datetime.DateTime datetime) {

		Objects.requireNonNull(datetime);

		final DateTimeFormContent datetimeFormContent = new DateTimeFormContent();

		if (Objects.nonNull(datetime.getDateTime())) {

			final LocalDateTime clientDateTime = DateTimes.toClientDateTime(request, datetime.getDateTime());
			datetimeFormContent.setDateTime_Date_submit(clientDateTime.toLocalDate());
			datetimeFormContent.setDateTime_Time_submit(clientDateTime.toLocalTime());
		}

		datetimeFormContent.setDate_Date_submit(datetime.getDate());

		if (Objects.nonNull(datetime.getTime())) {

			final LocalDateTime clientDateTime = DateTimes.toClientDateTime(request, LocalDateTime.of(LocalDate.now(), datetime.getTime()));
			datetimeFormContent.setTime_Date(clientDateTime.toLocalDate());
			datetimeFormContent.setTime_Time_submit(clientDateTime.toLocalTime());
		} else {

			final LocalDateTime clientDateTime = DateTimes.toClientDateTime(request, LocalDateTime.now());
			datetimeFormContent.setTime_Date(clientDateTime.toLocalDate());
		}

		return datetimeFormContent;
	}

	@Nonnull
	private Result failureRead(@Nonnull final Map<String, String> alertInfo, @Nonnull final Form<DateTimeFormContent> datetimeForm, final Request request, final Lang lang, final Messages messages) {

		return badRequest(views.html.framework.datetime.datetime.render(alertInfo, datetimeForm, request, lang, messages));
	}
}
