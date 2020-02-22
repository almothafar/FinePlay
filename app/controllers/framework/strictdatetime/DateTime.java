package controllers.framework.strictdatetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.security.auth.login.AccountException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import common.system.MessageKeys;
import common.utils.DateTimes;
import controllers.user.UserService;
import models.framework.strictdatetime.DateTimeFormContent;
import models.system.System.PermissionsAllowed;
import models.user.User;
import models.user.User_;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Lang;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
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

			final models.framework.strictdatetime.StrictDateTime readedDateTime = readDateTime(manager, request, messages);
			final DateTimeFormContent readedDateTimeFormContent = setDateTimeFormContentValue(request, readedDateTime);

			final Form<DateTimeFormContent> readedDateTimeForm = formFactory.form(DateTimeFormContent.class).fill(readedDateTimeFormContent);

			return ok(views.html.framework.strictdatetime.datetime.render(new HashMap<>(), readedDateTimeForm, request, lang, messages));
		});
	}

	@Nonnull
	private models.framework.strictdatetime.StrictDateTime readDateTime(@Nonnull final EntityManager manager, @Nonnull final Request request, @Nonnull Messages messages) {

		final User user;
		try {

			user = userService.read(manager, messages, request.session().get(User_.USER_ID).get());
		} catch (final AccountException e) {

			throw new RuntimeException(e);
		}

		models.framework.strictdatetime.StrictDateTime datetime = manager.find(models.framework.strictdatetime.StrictDateTime.class, user.getId());
		final boolean isExist = Objects.nonNull(datetime);

		if (!isExist) {

			datetime = new models.framework.strictdatetime.StrictDateTime();
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

				final models.framework.strictdatetime.StrictDateTime updatedDateTime;
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

				return ok(views.html.framework.strictdatetime.datetime.render(new HashMap<>(), updatedDateTimeForm, request, lang, messages));
			} else {

				return failureRead(new HashMap<>(), datetimeForm, request, lang, messages);
			}
		});
	}

	@Nonnull
	private models.framework.strictdatetime.StrictDateTime updateDateTime(@Nonnull final EntityManager manager, @Nonnull final DateTimeFormContent datetimeFormContent, @Nonnull final Request request, @Nonnull final Messages messages) {

		final LocalDate dateTime_Date = datetimeFormContent.getDateTime_Date();
		final ZonedDateTime dateTime_DateTime = datetimeFormContent.getDateTime_DateTime();
		LocalDateTime serverDateTime_DateTime = null;
		if (Objects.nonNull(dateTime_Date) && Objects.nonNull(dateTime_DateTime)) {

			final ZonedDateTime clientZonedDateTime = dateTime_DateTime;
			serverDateTime_DateTime = clientZonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
		}

		final User user;
		try {

			user = userService.read(manager, messages, request.session().get(User_.USER_ID).get());
		} catch (final AccountException e) {

			throw new RuntimeException(e);
		}

		models.framework.strictdatetime.StrictDateTime datetime = manager.find(models.framework.strictdatetime.StrictDateTime.class, user.getId());
		final boolean isExist = Objects.nonNull(datetime);

		if (!isExist) {

			datetime = new models.framework.strictdatetime.StrictDateTime();
			datetime.setUser_Id(user.getId());
		}

		datetime.setDateTime(serverDateTime_DateTime);

		if (!isExist) {

			manager.persist(datetime);
		} else {

			manager.merge(datetime);
		}
		return datetime;
	}

	@Nonnull
	private DateTimeFormContent setDateTimeFormContentValue(@Nonnull final Request request, @Nonnull final models.framework.strictdatetime.StrictDateTime datetime) {

		Objects.requireNonNull(datetime);

		final DateTimeFormContent datetimeFormContent = new DateTimeFormContent();

		if (Objects.nonNull(datetime.getDateTime())) {

			final ZonedDateTime clientZonedDateTime = DateTimes.toClientZonedDateTime(request, datetime.getDateTime());
			datetimeFormContent.setDateTime_Date_submit(clientZonedDateTime.toLocalDate());
			datetimeFormContent.setDateTime_DateTime_submit(clientZonedDateTime);
		}

		return datetimeFormContent;
	}

	@Nonnull
	private Result failureRead(@Nonnull final Map<String, String> alertInfo, @Nonnull final Form<DateTimeFormContent> datetimeForm, final Request request, final Lang lang, final Messages messages) {

		return badRequest(views.html.framework.strictdatetime.datetime.render(alertInfo, datetimeForm, request, lang, messages));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public CompletionStage<Result> times(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		messages.lang();

		final JsonNode jsonNode = request.body().asJson();

		return CompletableFuture.supplyAsync(() -> {

			if (!jsonNode.has("date")) {

				return badRequest(jsonNode);
			}
			final LocalDate clientDate = LocalDate.parse(jsonNode.get("date").asText());

			final LocalTime clientMinTime;
			if (!jsonNode.has("min")) {

				clientMinTime = LocalTime.MIN;
			} else {

				clientMinTime = LocalTime.parse(jsonNode.get("min").asText());
			}

			final LocalTime clientMaxTime;
			if (!jsonNode.has("max")) {

				clientMaxTime = LocalTime.MAX;
			} else {

				clientMaxTime = LocalTime.parse(jsonNode.get("max").asText());
			}

			final int interval;
			if (!jsonNode.has("interval")) {

				interval = 30;
			} else {

				interval = jsonNode.get("interval").asInt();
			}

			final JsonNode response = getClientZonedDateTimeObject(request, clientDate, clientMinTime, clientMaxTime, interval);
			return ok(response);
		});
	}

	@Nonnull
	private static JsonNode getClientZonedDateTimeObject(//
			@Nonnull final Request request, @Nonnull final LocalDate clientDate, //
			@Nonnull final LocalTime clientMinTime, @Nonnull final LocalTime clientMaxTime, //
			final int interval) {

		final List<ZonedDateTime> clientZonedDateTimes = DateTimes.toClientZonedDateTimes(//
				request, clientDate, //
				clientMinTime, clientMaxTime, //
				interval);

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		final ArrayNode times = result.putArray("times");

		for (final ZonedDateTime zonedDateTime : clientZonedDateTimes) {

			final String key = zonedDateTime.toString();
			final String value = zonedDateTime.toOffsetDateTime().toOffsetTime().toString();

			final ObjectNode dateTimeToName = times.addObject();
			dateTimeToName.put("dateTime", key);
			dateTimeToName.put("name", value);
		}

		return result;
	}
}
