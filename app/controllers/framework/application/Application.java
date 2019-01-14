package controllers.framework.application;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.security.auth.login.AccountException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Pattern.Flag;

import org.hibernate.HibernateException;
import org.hibernate.internal.SessionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.Config;

import akka.Done;
import common.system.MessageKeys;
import common.utils.DateTimes;
import common.utils.JSONs;
import common.utils.Locales;
import controllers.user.UserService;
import models.framework.application.FinePlayBean;
import models.framework.application.Jsr380Bean;
import models.framework.application.PlayBean;
import models.system.System.PermissionsAllowed;
import models.user.User;
import models.user.User_;
import play.api.PlayException;
import play.cache.AsyncCacheApi;
import play.cache.SyncCacheApi;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import play.db.jpa.JPAApi;
import play.i18n.Langs;
import play.i18n.Messages;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.Cookie;
import play.mvc.Http.Flash;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.mvc.With;
import play.routing.HandlerDef;
import play.routing.Router;
import play.twirl.api.Html;
import scala.Tuple2;

@PermissionsAllowed
public class Application extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private Config config;

	@Inject
	private Langs langs;

	@Inject
	private MessagesApi messagesApi;

	@Inject
	private SyncCacheApi syncCache;

	@Inject
	private AsyncCacheApi aSyncCache;

	@Inject
	private JPAApi jpaApi;

	@Inject
	private UserService userService;

	@Inject
	private FormFactory formFactory;

	@Inject
	private Clock clock;

	@With(LoggingAction.class)
	@Authenticated(common.core.Authenticator.class)
	public Result index(@Nonnull final Request request, String state) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (state) {
		case "config":

			return config(request, lang, messages);
		case "cookie":

			return cookie(request, lang, messages);
		case "session":

			return sessionlist(request, lang, messages);
		case "beforeflash":

			return beforeflashlist(request, lang, messages);
		case "flash":

			return flashlist(request, lang, messages);
		case "logger":

			return logger(request, lang, messages);
		case "cache":

			return cache(request, lang, messages);
		case "i18n":

			return i18n(request, lang, messages);
		case "message":

			return message(request, lang, messages);
		case "validation":

			return validation(request, lang, messages);
		case "request":

			return requestlist(request, lang, messages);
		case "exception":

			return exception(request, lang, messages);
		case "reporterror":

			return reporterror(request, lang, messages);
		case "clock":

			return clock(request, lang, messages);
		case "datetime":

			return datetime(request, lang, messages);
		case "daylightsavingtime":

			return daylightsavingtime(request, lang, messages);
		case "entitymanager":

			return entitymanager(request, lang, messages);
		case "user":

			return user(request, lang, messages);
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	public Result config(final Request request, final Lang lang, final Messages messages) {

		final Map<String, Object> map = config.entrySet().stream().collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue()));
		return ok(views.html.framework.application.config.render(new TreeMap<>(map), request, lang, messages));
	}

	public static Result cookie(final Request request, final Lang lang, final Messages messages) {

		// get
		request.cookie("key");

		final Map<String, Object> map = StreamSupport.stream(request.cookies().spliterator(), false).collect(Collectors.toMap(cookie -> cookie.name(), v -> v.value()));

		Result result = ok(views.html.framework.application.cookie.render(new TreeMap<>(map), request, lang, messages));

		result.cookies();
		// set
		result = result.withCookies(Cookie.builder("key", "value").withMaxAge(Duration.ofDays(10)).build());
		// delete
		result = result.discardingCookie("key");

		return result;
	}

	public static Result sessionlist(final Request request, final Lang lang, final Messages messages) {

		request.session();// Map

		// set
		request.session().adding("key", "value");

		// get
		request.session().getOptional("key");

		final Map<String, Object> map = request.session().data().entrySet().stream().collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue()));
		return ok(views.html.framework.application.session.render(new TreeMap<>(map), request, lang, messages));
	}

	public Result beforeflashlist(final Request request, final Lang lang, final Messages messages) {

		final Flash flash = new Flash(Map.of(//
				"success", "<strong>" + messages.at(MessageKeys.SUCCESS) + "</strong> " + messages.at(MessageKeys.MESSAGE), //
				"info", "<strong>" + messages.at(MessageKeys.INFO) + "</strong> " + messages.at(MessageKeys.MESSAGE), //
				"warning", "<strong>" + messages.at(MessageKeys.WARNING) + "</strong> " + messages.at(MessageKeys.MESSAGE), //
				"danger", "<strong>" + messages.at(MessageKeys.DANGER) + "</strong> " + messages.at(MessageKeys.MESSAGE)//
		));

		return redirect(controllers.framework.application.routes.Application.index("flash"))//
				// set
				.withFlash(flash);
	}

	public Result flashlist(final Request request, final Lang lang, final Messages messages) {

		// get
		final Flash flash = request.flash();

		return ok(views.html.framework.application.flashlist.render(flash, new TreeMap<>(flash), request, lang, messages));
	}

	public Result logger(final Request request, final Lang lang, final Messages messages) {

		LOGGER.trace("TRACE Log.");
		LOGGER.debug("DEBUG Log.");
		LOGGER.info("Info Log.");
		LOGGER.warn("WARN Log.");
		LOGGER.error("Error Log.");

		final StackTraceElement element = Thread.currentThread().getStackTrace()[1];

		return ok("See " + element.getClassName() + "#" + element.getMethodName() + " Line:" + element.getLineNumber());
	}

	public Result cache(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.framework.application.cache.render(request, lang, messages));
	}

	public Result i18n(final Request request, final Lang lang, final Messages messages) {

		final Map<String, Object> map = new LinkedHashMap<>();
		map.put("Locale name", messages.lang().toLocale().getDisplayName(messages.lang().toLocale()));
		map.put("Current lang", messages.lang());
		map.put("Browser Languages", request.acceptLanguages());
		map.put("HTTP Context Language", play.api.i18n.Lang.defaultLang());
		map.put("application.conf Languages", langs.availables());
		map.put("Current Locale " + createBadge(messages.at(MessageKeys.SERVER)), messages.at(MessageKeys.ERROR_REAL_PRECISION, 10, 2));
		map.put("US Locale", messagesApi.get(new Lang(Locale.US), MessageKeys.ERROR_REAL_PRECISION, 10, 1));
		map.put("Japan Locale", messagesApi.get(new Lang(Locale.JAPAN), MessageKeys.ERROR_REAL_PRECISION, 10, 0));

		final Currency currency = Currency.getInstance(messages.lang().locale());
		map.put("Currency Code", currency.getCurrencyCode());
		map.put("Currency Symbol", currency.getSymbol(messages.lang().locale()));
		map.put("Currency Name", currency.getDisplayName(messages.lang().locale()));

		map.put("Format Number", NumberFormat.getNumberInstance(messages.lang().locale()).format(new BigDecimal("123456.789")));
		map.put("Format Integer", NumberFormat.getIntegerInstance(messages.lang().locale()).format(new BigDecimal("123456.789")));
		map.put("Format Percent", NumberFormat.getPercentInstance(messages.lang().locale()).format(new BigDecimal("123456.789")));
		map.put("Format Currency", NumberFormat.getCurrencyInstance(messages.lang().locale()).format(new BigDecimal("123456.789")));

		return ok(views.html.framework.application.i18n.render(map, request, lang, messages));
	}

	public Result message(final Request request, final Lang lang, final Messages messages) {

		final Map<String, String> langIdToNameMap = Locales.getLocaleIdToNameMap(messages.lang().toLocale());

		final List<Locale> systemLocales = new ArrayList<>(langs.availables().stream().map(availableLang -> availableLang.locale()).collect(Collectors.toList()));

		final int enIndex = systemLocales.indexOf(Locale.US);
		systemLocales.add(0, systemLocales.remove(enIndex));

		final List<String> headerColumns = new ArrayList<>();
		headerColumns.add(messages.at(MessageKeys.KEY));
		final List<String> displaySystemLocales = systemLocales.stream().map(systemLocale -> {

			final StringBuilder builder = new StringBuilder();
			builder.append(messagesApi.get(new Lang(systemLocale), MessageKeys.COUNTRY_FLAG));
			builder.append(" ");
			builder.append(langIdToNameMap.get(systemLocale.toLanguageTag()));
			return builder.toString();
		}).collect(Collectors.toList());
		headerColumns.addAll(displaySystemLocales);

		final List<List<String>> lineColumnsList = getMessageKeySet().stream().map(messageKey -> {

			final List<String> lineColumns = new ArrayList<>();
			lineColumns.add(messageKey);
			final List<String> displayMessageOfSystemLangs = systemLocales.stream().map(systemLocale -> messagesApi.get(new Lang(systemLocale), messageKey)).collect(Collectors.toList());
			lineColumns.addAll(displayMessageOfSystemLangs);

			return lineColumns;
		}).collect(Collectors.toList());

		return ok(views.html.framework.application.message.render(displaySystemLocales, headerColumns, lineColumnsList, request, lang, messages));
	}

	private static SortedSet<String> getMessageKeySet() {

		final SortedSet<String> set = Arrays.asList(MessageKeys.class.getDeclaredFields()).stream()//
				.filter(field -> //
				Modifier.isPublic(field.getModifiers()) && //
						Modifier.isStatic(field.getModifiers()) && //
						Modifier.isFinal(field.getModifiers()) && //
						(String.class.getName().equals(field.getType().getName())))//
				.map(field -> {

					String value = null;
					try {
						value = (String) field.get(null);
					} catch (final Exception e) {
					}
					return value;
				})//
				.collect(Collectors.toCollection(TreeSet::new));

		return Collections.unmodifiableSortedSet(set);
	}

	public Result validation(final Request request, final Lang lang, final Messages messages) {

		final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

		final PlayBean playBean = new PlayBean();
		final Set<ConstraintViolation<PlayBean>> playViolations = validator.validate(playBean);
		playViolations.stream().forEach(violation -> {
			violation.getPropertyPath().toString();
			messages.at(violation.getMessage());
		});

		final Jsr380Bean jsr380Bean = new Jsr380Bean();
		final Set<ConstraintViolation<Jsr380Bean>> jsr380Violations = validator.validate(jsr380Bean);
		jsr380Violations.stream().forEach(violation -> {
			violation.getPropertyPath().toString();
			messages.at(violation.getMessage());
		});

		final FinePlayBean fineplayBean = new FinePlayBean();
		final Set<ConstraintViolation<FinePlayBean>> fineplayViolations = validator.validate(fineplayBean);
		fineplayViolations.stream().forEach(violation -> {
			violation.getPropertyPath().toString();
			messages.at(violation.getMessage());
		});

		final Function<ValidationError, Entry<String, Object>> createErrorDisplayEntry = error -> {

			String messageKeyAndTypeValue = error.key().replaceFirst("_", ".");
			final boolean isSystem = error.key().startsWith("java_") || error.key().startsWith("system_");
			if (isSystem) {

				messageKeyAndTypeValue = messageKeyAndTypeValue.replaceFirst("_", ".");
			}
			final String[] messageKeyAndType = messageKeyAndTypeValue.split("_");

			final String messageKey = messageKeyAndType[0];
			final String messageType = 1 == messageKeyAndType.length ? "" : "(" + messageKeyAndType[1] + ")";
			final Object[] arguments = error.arguments().stream().map(argument -> {

				switch (argument.getClass().getSimpleName()) {
				case "Boolean":

					return Boolean.TRUE.equals(argument) ? 1 : 0;
				case "Flag[]":

					final Flag[] flags = Flag[].class.cast(argument);
					final List<String> flagList = Arrays.stream(flags).map(flag -> {

						switch (flag.getValue()) {
						case Pattern.CANON_EQ:

							return "CANON_EQ";
						case Pattern.CASE_INSENSITIVE:

							return "CASE_INSENSITIVE";
						case Pattern.COMMENTS:

							return "COMMENTS";
						case Pattern.DOTALL:

							return "DOTALL";
						case Pattern.UNICODE_CASE:

							return "UNICODE_CASE";
						case Pattern.MULTILINE:

							return "MULTILINE";
						case Pattern.UNICODE_CHARACTER_CLASS:

							return "UNICODE_CHARACTER_CLASS";
						case Pattern.UNIX_LINES:

							return "UNIX_LINES";
						default:

							throw new IllegalStateException(":" + flag.getValue());
						}
					}).collect(Collectors.toList());

					return flagList.toString();
				default:

					return argument;
				}
			}).collect(Collectors.toList()).toArray(new Object[0]);

			final String constraintKey = messageKey;
			final String constraint = messages.at(constraintKey, arguments);

			final String errorKey = error.message();
			final String errorMessage = messages.at(errorKey, arguments);

			final String messageDescription = messageKey + messageType;

			return new SimpleImmutableEntry<String, Object>(messageDescription, constraint + "<br>" + errorMessage);
		};

		final Form<PlayBean> playForm = formFactory.form(PlayBean.class).bindFromRequest(request);
		final Map<String, Object> playMap = playForm.errors().stream().map(createErrorDisplayEntry).collect(Collectors.toMap(//
				entry -> entry.getKey(), //
				entry -> entry.getValue(), //
				(u, v) -> {
					throw new IllegalStateException(v.toString());
				}, //
				LinkedHashMap::new));

		final Form<Jsr380Bean> jsr380Form = formFactory.form(Jsr380Bean.class).bindFromRequest(request);
		final Map<String, Object> jsr380Map = jsr380Form.errors().stream().map(createErrorDisplayEntry).collect(Collectors.toMap(//
				entry -> entry.getKey(), //
				entry -> entry.getValue(), //
				(u, v) -> {
					throw new IllegalStateException(v.toString());
				}, //
				LinkedHashMap::new));

		final Form<FinePlayBean> fineplayForm = formFactory.form(FinePlayBean.class).bindFromRequest(request);
		final Map<String, Object> fineplayMap = fineplayForm.errors().stream().map(createErrorDisplayEntry).collect(Collectors.toMap(//
				entry -> entry.getKey(), //
				entry -> entry.getValue(), //
				(u, v) -> {
					throw new IllegalStateException(v.toString());
				}, //
				LinkedHashMap::new));

		return ok(views.html.framework.application.validation.render(//
				new TreeMap<>(playMap), //
				new TreeMap<>(jsr380Map), //
				new TreeMap<>(fineplayMap), //
				request, lang, messages));
	}

	public static Result requestlist(final Request request, final Lang lang, final Messages messages) {

		final Map<String, Object> map = new LinkedHashMap<>();
		map.put("Remote Address", request.remoteAddress());
		map.put("Method", request.method());
		map.put("Host", request.host());
		map.put("Path", request.path());
		map.put("URI", request.uri());
		map.put("Headers", request.getHeaders().toMap().entrySet().stream().map(entry -> {

			final String key = entry.getKey();
			final String value = entry.getValue().stream().collect(Collectors.joining("<br>"));
			return "<tr><td>" + key + "</td><td>" + value + "</td></tr>";
		}).collect(Collectors.joining("", "<table><thead><tr><td>KEY</td><td>VALUE</td></tr></thead><tbody>", "</tbody></table>")));

		final HandlerDef handlerDef = request.attrs().get(Router.Attrs.HANDLER_DEF);
		final List<String> modifiers = handlerDef.getModifiers();
		;
		map.put("HandlerDef", modifiers);

		return ok(views.html.framework.application.request.render(map, request, lang, messages));
	}

	public static Result exception(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.framework.application.exception.render(request, lang, messages));
	}

	public static Result reporterror(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.framework.application.reporterror.render(request, lang, messages));
	}

	public Result clock(final Request request, final Lang lang, final Messages messages) {

		final LocalDateTime serverDateTime = LocalDateTime.now(clock);
		return ok(views.html.framework.application.clock.render(serverDateTime, request, lang, messages));
	}

	public Result datetime(final Request request, final Lang lang, final Messages messages) {

		final Map<String, Object> map = new LinkedHashMap<>();

		final ZoneId machineZoneId = ZoneId.systemDefault();
		final LocalDateTime machineTime = LocalDateTime.now();
		final OffsetDateTime machineOffsetTime = OffsetDateTime.now();
		final ZonedDateTime machineZonedTime = ZonedDateTime.now();

		final LocalDateTime serverTime = LocalDateTime.now();
		final ZoneId serverZoneId = ZoneOffset.UTC;
		final OffsetDateTime serverOffsetTime = OffsetDateTime.of(serverTime, serverZoneId.getRules().getOffset(serverTime));
		final ZonedDateTime serverZonedTime = ZonedDateTime.of(serverTime, serverZoneId);

		final ZoneId clientZoneId = ZoneId.of(request.session().getOptional(models.user.User_.ZONE_ID).get());
		final ZonedDateTime clientZonedTime = serverZonedTime.withZoneSameInstant(clientZoneId);
		final LocalDateTime clientTime = clientZonedTime.toLocalDateTime();
		final OffsetDateTime clientOffsetTime = OffsetDateTime.of(clientTime, clientZoneId.getRules().getOffset(clientTime));

		map.put("Machine ZoneId", machineZoneId);
		map.put("Machine Time", machineTime);
		map.put("Machine OffsetTime", machineOffsetTime);
		map.put("Machine ZonedTime", machineZonedTime);
		map.put("Server ZoneId", serverZoneId);
		map.put("Server Time " + createBadge(messages.at(MessageKeys.SERVER)), serverTime);
		map.put("Server OffsetTime", serverOffsetTime);
		map.put("Server ZonedTime", serverZonedTime);
		map.put("Client ZoneId", clientZoneId);
		map.put("Client Time " + createBadge(messages.at(MessageKeys.CLIENT)), clientTime);
		map.put("Client OffsetTime", clientOffsetTime);
		map.put("Client ZonedTime", clientZonedTime);
		map.put("Client Formated Full DateTime", DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(messages.lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Long DateTime", DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG).withLocale(messages.lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Medium DateTime", DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(messages.lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Short DateTime", DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(messages.lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Full Date", DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(messages.lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Long Date", DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(messages.lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Medium Date", DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(messages.lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Short Date", DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(messages.lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Full Time", DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL).withLocale(messages.lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Long Time", DateTimeFormatter.ofLocalizedTime(FormatStyle.LONG).withLocale(messages.lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Medium Time", DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).withLocale(messages.lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Short Time", DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(messages.lang().toLocale()).format(clientZonedTime));
		map.put("Server = Client", serverZonedTime.isEqual(clientZonedTime));

		final JapaneseDate japaneseDate = JapaneseDate.from(clientTime);
		map.put("Client(Japan) Year of Era", japaneseDate.format(DateTimeFormatter.ofPattern("G y").withLocale(messages.lang().toLocale())));

		final HijrahDate hijrahDate = HijrahDate.from(clientTime);
		map.put("Client(Hijrah) Year of Era", hijrahDate.format(DateTimeFormatter.ofPattern("G y").withLocale(messages.lang().toLocale())));

		final MinguoDate minguoDate = MinguoDate.from(clientTime);
		map.put("Client(Minguo) Year of Era", minguoDate.format(DateTimeFormatter.ofPattern("G y").withLocale(messages.lang().toLocale())));

		final ThaiBuddhistDate thaiBuddhistDate = ThaiBuddhistDate.from(clientTime);
		map.put("Client(ThaiBuddhist) Year of Era", thaiBuddhistDate.format(DateTimeFormatter.ofPattern("G y").withLocale(messages.lang().toLocale())));

		return ok(views.html.framework.application.datetime.render(map, request, lang, messages));
	}

	public Result daylightsavingtime(final Request request, final Lang lang, final Messages messages) {

		final List<List<String>> startDayInfo = new ArrayList<>();
		startDayInfo.add(Arrays.asList("2017-03-12T09:00Z", ZonedDateTime.parse("2017-03-12T09:00Z").withZoneSameInstant(ZoneId.of("US/Pacific")).toString(), "2017-03-12T01:00:00-08:00[US/Pacific]", ZonedDateTime.parse("2017-03-12T01:00:00-08:00[US/Pacific]").withZoneSameInstant(ZoneOffset.UTC).toString()));
		startDayInfo.add(Arrays.asList("2017-03-12T09:30Z", ZonedDateTime.parse("2017-03-12T09:30Z").withZoneSameInstant(ZoneId.of("US/Pacific")).toString(), "2017-03-12T01:30:00-08:00[US/Pacific]", ZonedDateTime.parse("2017-03-12T01:30:00-08:00[US/Pacific]").withZoneSameInstant(ZoneOffset.UTC).toString()));
		startDayInfo.add(Arrays.asList("", "", /* ! */"2017-03-12T02:00:00-08:00[US/Pacific]", ZonedDateTime.parse("2017-03-12T02:00:00-08:00[US/Pacific]").withZoneSameInstant(ZoneOffset.UTC).toString()));
		startDayInfo.add(Arrays.asList("", "", /* ! */"2017-03-12T02:30:00-07:00[US/Pacific]", ZonedDateTime.parse("2017-03-12T02:30:00-07:00[US/Pacific]").withZoneSameInstant(ZoneOffset.UTC).toString()));
		startDayInfo.add(Arrays.asList("2017-03-12T10:00Z", ZonedDateTime.parse("2017-03-12T10:00Z").withZoneSameInstant(ZoneId.of("US/Pacific")).toString(), "2017-03-12T03:00:00-07:00[US/Pacific]", ZonedDateTime.parse("2017-03-12T03:00:00-08:00[US/Pacific]").withZoneSameInstant(ZoneOffset.UTC).toString()));
		startDayInfo.add(Arrays.asList("2017-03-12T10:30Z", ZonedDateTime.parse("2017-03-12T10:30Z").withZoneSameInstant(ZoneId.of("US/Pacific")).toString(), "2017-03-12T03:30:00-07:00[US/Pacific]", ZonedDateTime.parse("2017-03-12T03:30:00-08:00[US/Pacific]").withZoneSameInstant(ZoneOffset.UTC).toString()));

		final List<List<String>> endDayInfo = new ArrayList<>();
		endDayInfo.add(Arrays.asList("2017-11-05T07:00Z", ZonedDateTime.parse("2017-11-05T07:00Z").withZoneSameInstant(ZoneId.of("US/Pacific")).toString(), "2017-11-05T00:00:00-07:00[US/Pacific]", ZonedDateTime.parse("2017-11-05T00:00:00-07:00[US/Pacific]").withZoneSameInstant(ZoneOffset.UTC).toString()));
		endDayInfo.add(Arrays.asList("2017-11-05T07:30Z", ZonedDateTime.parse("2017-11-05T07:30Z").withZoneSameInstant(ZoneId.of("US/Pacific")).toString(), "2017-11-05T00:30:00-07:00[US/Pacific]", ZonedDateTime.parse("2017-11-05T00:30:00-07:00[US/Pacific]").withZoneSameInstant(ZoneOffset.UTC).toString()));
		endDayInfo.add(Arrays.asList("2017-11-05T08:00Z", ZonedDateTime.parse("2017-11-05T08:00Z").withZoneSameInstant(ZoneId.of("US/Pacific")).toString(), "2017-11-05T01:00:00-07:00[US/Pacific]", ZonedDateTime.parse("2017-11-05T01:00:00-07:00[US/Pacific]").withZoneSameInstant(ZoneOffset.UTC).toString()));
		endDayInfo.add(Arrays.asList("2017-11-05T08:30Z", ZonedDateTime.parse("2017-11-05T08:30Z").withZoneSameInstant(ZoneId.of("US/Pacific")).toString(), "2017-11-05T01:30:00-07:00[US/Pacific]", ZonedDateTime.parse("2017-11-05T01:30:00-07:00[US/Pacific]").withZoneSameInstant(ZoneOffset.UTC).toString()));
		endDayInfo.add(Arrays.asList("2017-11-05T09:00Z", ZonedDateTime.parse("2017-11-05T09:00Z").withZoneSameInstant(ZoneId.of("US/Pacific")).toString(), "2017-11-05T01:00:00-08:00[US/Pacific]", ZonedDateTime.parse("2017-11-05T01:00:00-08:00[US/Pacific]").withZoneSameInstant(ZoneOffset.UTC).toString()));
		endDayInfo.add(Arrays.asList("2017-11-05T09:30Z", ZonedDateTime.parse("2017-11-05T09:30Z").withZoneSameInstant(ZoneId.of("US/Pacific")).toString(), "2017-11-05T01:30:00-08:00[US/Pacific]", ZonedDateTime.parse("2017-11-05T01:30:00-08:00[US/Pacific]").withZoneSameInstant(ZoneOffset.UTC).toString()));
		endDayInfo.add(Arrays.asList("2017-11-05T10:00Z", ZonedDateTime.parse("2017-11-05T10:00Z").withZoneSameInstant(ZoneId.of("US/Pacific")).toString(), "2017-11-05T02:00:00-08:00[US/Pacific]", ZonedDateTime.parse("2017-11-05T02:00:00-08:00[US/Pacific]").withZoneSameInstant(ZoneOffset.UTC).toString()));
		endDayInfo.add(Arrays.asList("2017-11-05T10:30Z", ZonedDateTime.parse("2017-11-05T10:30Z").withZoneSameInstant(ZoneId.of("US/Pacific")).toString(), "2017-11-05T02:30:00-08:00[US/Pacific]", ZonedDateTime.parse("2017-11-05T02:30:00-08:00[US/Pacific]").withZoneSameInstant(ZoneOffset.UTC).toString()));

		final List<List<LocalDateTime>> dateTimeList = createDateTimeList();

		return ok(views.html.framework.application.daylightsavingtime.render(startDayInfo, endDayInfo, dateTimeList, request, lang, messages));
	}

	private static List<List<LocalDateTime>> createDateTimeList() {

		final List<List<LocalDateTime>> dateTimes = new ArrayList<>();

		final LocalDateTime start = LocalDateTime.of(Year.now().getValue(), Month.JANUARY, 1, 0, 0);

		for (int days = 0; days < 365; days++) {

			final LocalDateTime day = start.plusDays(days);

			final List<LocalDateTime> dateTimesOfDay = new ArrayList<>();
			for (int minutes = 0; minutes < 1440; minutes = minutes + 10) {

				final LocalDateTime time = day.plusMinutes(minutes);
				dateTimesOfDay.add(time);
			}
			dateTimes.add(dateTimesOfDay);
		}

		return Collections.unmodifiableList(dateTimes);
	}

	public Result entitymanager(final Request request, final Lang lang, final Messages messages) {

		return jpaApi.withTransaction(manager -> {

			final SessionImpl entityManager = (SessionImpl) manager;

			final Map<String, Object> propertiesMap = entityManager.getProperties();

			final Map<String, Object> platformMap = new LinkedHashMap<>();
			try {

				final DatabaseMetaData metaData = entityManager.connection().getMetaData();
				platformMap.put("DatabaseProductName", metaData.getDatabaseProductName());
				platformMap.put("DatabaseProductVersion", metaData.getDatabaseProductVersion());
				platformMap.put("DriverName", metaData.getDriverName());
				platformMap.put("DriverVersion", metaData.getDriverVersion());

				switch (metaData.getDatabaseProductName()) {
				case "H2":

					// Database depend code is here.
					break;
				case "PostgreSQL":

					// Database depend code is here.
					break;
				default:
					break;
				}
			} catch (HibernateException | SQLException e) {

				throw new RuntimeException();
			}

			return ok(views.html.framework.application.entitymanager.render(//
					new TreeMap<>(propertiesMap), //
					new TreeMap<>(platformMap), //
					request, lang, messages));
		});
	}

	@SuppressWarnings("null")
	public Result user(final Request request, final Lang lang, final Messages messages) {

		return jpaApi.withTransaction(manager -> {

			final Map<String, Object> map = new LinkedHashMap<>();

			final User user;
			try {

				user = userService.read(manager, messages, request.session().getOptional(User_.USER_ID).get());
			} catch (final AccountException e) {

				throw new RuntimeException(e);
			}

			map.put(User_.USER_ID, user.getUserId());
			map.put(User_.ROLES, user.getRoles());
			map.put("permissions", user.getPermissions());
			map.put("expireTime(UTC)", user.getExpireDateTime().toString());
			map.put("signInTime(UTC)", Objects.toString(user.getSignInDateTime(), ""));
			map.put("signOutTime(UTC)", Objects.toString(user.getSignOutDateTime(), ""));
			map.put("updateTime(UTC)", user.getUpdateDateTime().toString());
			map.put("expireTime", DateTimes.toClientDateTime(request, user.getExpireDateTime()).toString());
			final LocalDateTime signInTime = user.getSignInDateTime() != null ? DateTimes.toClientDateTime(request, user.getSignInDateTime()) : null;
			map.put("signInTime", Objects.toString(signInTime, ""));
			final LocalDateTime signOutTime = user.getSignOutDateTime() != null ? DateTimes.toClientDateTime(request, user.getSignOutDateTime()) : null;
			map.put("signOutTime", Objects.toString(signOutTime, ""));
			map.put("updateTime", DateTimes.toClientDateTime(request, user.getUpdateDateTime()).toString());
			return ok(views.html.framework.application.user.render(map, request, lang, messages));
		});
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result synccache(Request request) {

		final Messages messages = messagesApi.preferred(request);
		messages.lang();

		final JsonNode jsonNode = request.body().asJson();
		LOGGER.debug(JSONs.toJSON(jsonNode));
		final String key = jsonNode.get("key") != null ? jsonNode.get("key").textValue() : "";
		final String value = jsonNode.get("value") != null ? jsonNode.get("value").textValue() : "";

		if (key.isEmpty() && value.isEmpty()) {

			badRequest(jsonNode);
		}

		final ObjectNode result = Json.newObject();
		result.put("key", key);
		if (value.isEmpty()) {
			// get

			final Optional<String> cachedValueOptional = syncCache.getOptional(key);
			final String cachedValue = cachedValueOptional.orElseGet(() -> "");

			result.put("value", cachedValue);
		} else {
			// set

			syncCache.set(key, value);

			result.put("value", value);
		}

		return ok(result);
	}

	@BodyParser.Of(BodyParser.Json.class)
	public CompletionStage<Result> asynccache(Request request) {

		final Messages messages = messagesApi.preferred(request);
		messages.lang();

		final JsonNode jsonNode = request.body().asJson();
		LOGGER.debug(JSONs.toJSON(jsonNode));
		final String key = jsonNode.get("key") != null ? jsonNode.get("key").textValue() : "";
		final String value = jsonNode.get("value") != null ? jsonNode.get("value").textValue() : "";

		if (key.isEmpty() && value.isEmpty()) {

			badRequest(jsonNode);
		}

		return CompletableFuture.supplyAsync(() -> {

			final ObjectNode result = Json.newObject();
			result.put("key", key);
			if (value.isEmpty()) {
				// get

				final CompletionStage<Optional<String>> stage = aSyncCache.getOptional(key);
				try {

					final Optional<String> cachedValueOptional = stage.toCompletableFuture().get(3, TimeUnit.SECONDS);
					final String cachedValue = cachedValueOptional.orElseGet(() -> "");
					result.put("value", cachedValue);
				} catch (InterruptedException | ExecutionException e) {

					throw new RuntimeException(e);
				} catch (TimeoutException e) {

					result.put("value", e.getLocalizedMessage());
				}
			} else {
				// set

				final CompletionStage<Done> stage = aSyncCache.set(key, value, 60 * 60);
				try {

					stage.toCompletableFuture().get(3, TimeUnit.SECONDS);
					result.put("value", value);
				} catch (InterruptedException | ExecutionException e) {

					throw new RuntimeException(e);
				} catch (TimeoutException e) {

					result.put("value", e.getLocalizedMessage());
				}
			}

			return ok(result);
		});
	}

	public Result playException(Request request) {

		final Messages messages = messagesApi.preferred(request);
		messages.lang();

		throw new PlayException(messages.at(MessageKeys.TITLE), messages.at(MessageKeys.DESCRIPTION) + //
				" <a href=\"" + controllers.framework.application.routes.Application.index("exception").url() + "\">" + messages.at(MessageKeys.RECOVERY) + "</a>");
	}

	public Result runtimeException(Request request) {

		final Messages messages = messagesApi.preferred(request);
		messages.lang();

		throw new RuntimeException(messages.at(MessageKeys.MESSAGE));
	}

	private static Html createBadge(final String text) {

		final scala.collection.mutable.ListBuffer<scala.Tuple2<String, String>> props = new scala.collection.mutable.ListBuffer<>();
		props.$plus$eq(new Tuple2<String, String>("type", "success"));
		props.$plus$eq(new Tuple2<String, String>("text", text));
		@SuppressWarnings("unchecked")
		final scala.collection.immutable.Map<String, String> prop = scala.collection.immutable.HashMap$.MODULE$.apply(props.toList());

		return views.html.components.badge.render(prop);
	}
}
