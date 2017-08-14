package controllers.framework.application;

import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.security.auth.login.AccountException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.hibernate.HibernateException;
import org.hibernate.internal.SessionImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.Config;

import akka.Done;
import common.system.MessageKeys;
import common.utils.DateTimes;
import controllers.user.UserService;
import play.mvc.Controller;
import models.framework.application.Jsr303Bean;
import models.framework.application.PlayBean;
import models.framework.application.FinePlayBean;
import models.system.System.PermissionsAllowed;
import models.user.User;
import play.api.PlayException;
import play.cache.AsyncCacheApi;
import play.cache.SyncCacheApi;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.i18n.Lang;
import play.i18n.Langs;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.routing.Router;
import play.twirl.api.Html;
import scala.Tuple2;

@PermissionsAllowed
public class Application extends Controller {

	@Inject
	private Config config;

	@Inject
	private Langs langs;

	@Inject
	private MessagesApi messages;

	@Inject
	private SyncCacheApi syncCacheApi;

	@Inject
	private AsyncCacheApi aSyncCacheApi;

	@Inject
	private JPAApi jpaApi;

	@Inject
	private UserService userService;

	@Authenticated(common.core.Authenticator.class)
	@Transactional(readOnly = true)
	public Result index(String state) {

		switch (state) {
			case "config" :

				return config();
			case "cookie" :

				return cookie();
			case "session" :

				return sessionlist();
			case "flash" :

				return flashlist();
			case "cache" :

				return cache();
			case "i18n" :

				return i18n();
			case "message" :

				return message();
			case "validation" :

				return validation();
			case "request" :

				return requestlist();
			case "exception" :

				return exception();
			case "reporterror" :

				return reporterror();
			case "datetime" :

				return datetime();
			case "entitymanager" :

				return entitymanager();
			case "user" :

				return user();
			default :

				return notFound(views.html.error.notfound.render(request().method(), request().uri()));
		}
	}

	public Result config() {

		final Map<String, Object> map = config.entrySet().stream().collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue()));
		return ok(views.html.framework.application.config.render(new TreeMap<>(map)));
	}

	public static Result cookie() {

		response().cookies();// Iterable

		// set
		// response().setCookie(Cookie.builder("key",
		// "value").withMaxAge(Duration.ofDays(10)).build());

		// get
		request().cookie("key");

		// delete
		response().discardCookie("key");

		final Map<String, Object> map = StreamSupport.stream(request().cookies().spliterator(), false).collect(Collectors.toMap(cookie -> cookie.name(), v -> v.value()));
		return ok(views.html.framework.application.cookie.render(new TreeMap<>(map)));
	}

	public static Result sessionlist() {

		session();// Map

		// set
		// session("key", "value");

		// get
		session("key");

		final Map<String, Object> map = session().entrySet().stream().collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue()));
		return ok(views.html.framework.application.session.render(new TreeMap<>(map)));
	}

	public Result flashlist() {

		flash();// Map

		// set
		flash("success", "<strong>" + messages.get(lang(), MessageKeys.SUCCESS) + "</strong> " + messages.get(lang(), MessageKeys.MESSAGE));
		flash("info", "<strong>" + messages.get(lang(), MessageKeys.INFO) + "</strong> " + messages.get(lang(), MessageKeys.MESSAGE));
		flash("warning", "<strong>" + messages.get(lang(), MessageKeys.WARNING) + "</strong> " + messages.get(lang(), MessageKeys.MESSAGE));
		flash("danger", "<strong>" + messages.get(lang(), MessageKeys.DANGER) + "</strong> " + messages.get(lang(), MessageKeys.MESSAGE));

		final Map<String, Object> map = flash().entrySet().stream().collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue()));
		return ok(views.html.framework.application.flash.render(new TreeMap<>(map)));
	}

	public Result cache() {

		// set
		// aSyncCacheApi.set("key", "value", 3600);

		// get
		aSyncCacheApi.get("key");

		// get
		aSyncCacheApi.getOrElseUpdate("item.key1", () -> {

			return CompletableFuture.supplyAsync(() -> {

				return "Cache is empty.";
			});
		}, 3600);

		return ok(views.html.framework.application.cache.render());
	}

	public Result i18n() {

		final Map<String, Object> map = new LinkedHashMap<>();
		map.put("Locale name", lang().toLocale().getDisplayName(lang().toLocale()));
		map.put("Current lang", lang());
		map.put("Browser Languages", request().acceptLanguages());
		map.put("HTTP Context Language", play.api.i18n.Lang.defaultLang());
		map.put("application.conf Languages", langs.availables());
		map.put("Current Locale " + createBadge(messages.get(lang(), MessageKeys.SERVER)), messages.get(lang(), MessageKeys.ERROR_REAL_PRECISION, 10, 2));
		map.put("US Locale", messages.get(common.utils.Locales.toLang(Locale.US), MessageKeys.ERROR_REAL_PRECISION, 10, 1));
		map.put("Japan Locale", messages.get(common.utils.Locales.toLang(Locale.JAPAN), MessageKeys.ERROR_REAL_PRECISION, 10, 0));

		final Currency currency = Currency.getInstance(lang().locale());
		map.put("Currency Code", currency.getCurrencyCode());
		map.put("Currency Symbol", currency.getSymbol(lang().locale()));
		map.put("Currency Name", currency.getDisplayName(lang().locale()));

		map.put("Format Number", NumberFormat.getNumberInstance(lang().locale()).format(new BigDecimal("123456.789")));
		map.put("Format Integer", NumberFormat.getIntegerInstance(lang().locale()).format(new BigDecimal("123456.789")));
		map.put("Format Percent", NumberFormat.getPercentInstance(lang().locale()).format(new BigDecimal("123456.789")));
		map.put("Format Currency", NumberFormat.getCurrencyInstance(lang().locale()).format(new BigDecimal("123456.789")));

		return ok(views.html.framework.application.i18n.render(map));
	}

	public Result message() {

		final Map<String, String> langIdToNameMap = common.utils.Locales.getLocaleIdToNameMap(lang().toLocale());

		final List<Lang> systemLangs = new ArrayList<>(langs.availables());

		final int enIndex = systemLangs.indexOf(common.utils.Locales.toLang(Locale.US));
		systemLangs.add(0, systemLangs.remove(enIndex));

		final List<String> headerColumns = new ArrayList<>();
		headerColumns.add(messages.get(lang(), MessageKeys.KEY));
		final List<String> displaySystemLangs = systemLangs.stream().map(systemLang -> {

			final StringBuilder builder = new StringBuilder();
			builder.append(messages.get(systemLang, MessageKeys.COUNTRY_FLAG));
			builder.append(" ");
			builder.append(langIdToNameMap.get(systemLang.code()));
			return builder.toString();
		}).collect(Collectors.toList());
		headerColumns.addAll(displaySystemLangs);

		final List<List<String>> lineColumnsList = getMessageKeySet().stream().map(messageKey -> {

			final List<String> lineColumns = new ArrayList<>();
			lineColumns.add(messageKey);
			final List<String> displayMessageOfSystemLangs = systemLangs.stream().map(systemLang -> messages.get(systemLang, messageKey)).collect(Collectors.toList());
			lineColumns.addAll(displayMessageOfSystemLangs);

			return lineColumns;
		}).collect(Collectors.toList());

		return ok(views.html.framework.application.message.render(displaySystemLangs, headerColumns, lineColumnsList));
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

	public Result validation() {

		final Map<String, Object> playMap = new LinkedHashMap<>();

		final PlayBean playBean = new PlayBean();
		final Set<ConstraintViolation<PlayBean>> playViolations = Validation.buildDefaultValidatorFactory().getValidator().validate(playBean);
		playViolations.stream().forEach(violation -> {
			playMap.put(violation.getPropertyPath().toString(), messages.get(lang(), violation.getMessage()));
		});

		final Map<String, Object> jsr303Map = new LinkedHashMap<>();

		final Jsr303Bean jsr303Bean = new Jsr303Bean();
		final Set<ConstraintViolation<Jsr303Bean>> jsr303Violations = Validation.buildDefaultValidatorFactory().getValidator().validate(jsr303Bean);
		jsr303Violations.stream().forEach(violation -> {
			jsr303Map.put(violation.getPropertyPath().toString(), messages.get(lang(), violation.getMessage()));
		});

		final Map<String, Object> fineplayMap = new LinkedHashMap<>();

		final FinePlayBean fineplayBean = new FinePlayBean();
		final Set<ConstraintViolation<FinePlayBean>> fineplayViolations = Validation.buildDefaultValidatorFactory().getValidator().validate(fineplayBean);
		fineplayViolations.stream().forEach(violation -> {
			fineplayMap.put(violation.getPropertyPath().toString(), messages.get(lang(), violation.getMessage()));
		});

		return ok(views.html.framework.application.validation.render(//
				new TreeMap<>(playMap), //
				new TreeMap<>(jsr303Map), //
				new TreeMap<>(fineplayMap)));
	}

	public static Result requestlist() {

		final Map<String, Object> map = new LinkedHashMap<>();
		map.put("Remote Address", ctx().request().remoteAddress());
		map.put("Method", ctx().request().method());
		map.put("Host", ctx().request().host());
		map.put("Path", ctx().request().path());
		map.put("URI", ctx().request().uri());
		map.put("Headers", ctx().request().getHeaders().toMap().entrySet().stream().map(entry -> {

			final String key = entry.getKey();
			final String value = entry.getValue().stream().collect(Collectors.joining("<br>"));
			return "<tr><td>" + key + "</td><td>" + value + "</td></tr>";
		}).collect(Collectors.joining("", "<table><thead><tr><td>KEY</td><td>VALUE</td></tr></thead><tbody>", "</tbody></table>")));

		final /* play.routing.HandlerDef ??? */play.api.routing.HandlerDef handlerDef = ctx().request().attrs().get(Router.Attrs.HANDLER_DEF);
		final List<String> modifiers = handlerDef.getModifiers();;
		map.put("HandlerDef", modifiers);

		return ok(views.html.framework.application.request.render(map));
	}

	public static Result exception() {

		return ok(views.html.framework.application.exception.render());
	}

	public static Result reporterror() {

		return ok(views.html.framework.application.reporterror.render());
	}

	public Result datetime() {

		final Map<String, Object> map = new LinkedHashMap<>();

		final ZoneId machineZoneId = ZoneId.systemDefault();
		final LocalDateTime machineTime = LocalDateTime.now();
		final OffsetDateTime machineOffsetTime = OffsetDateTime.now();
		final ZonedDateTime machineZonedTime = ZonedDateTime.now();

		final LocalDateTime serverTime = LocalDateTime.now();
		final ZoneId serverZoneId = ZoneOffset.UTC;
		final OffsetDateTime serverOffsetTime = OffsetDateTime.of(serverTime, serverZoneId.getRules().getOffset(serverTime));
		final ZonedDateTime serverZonedTime = ZonedDateTime.of(serverTime, serverZoneId);

		final ZoneId clientZoneId = ZoneId.of(session(models.user.User.ZONEID));
		final ZonedDateTime clientZonedTime = serverZonedTime.withZoneSameInstant(clientZoneId);
		final LocalDateTime clientTime = clientZonedTime.toLocalDateTime();
		final OffsetDateTime clientOffsetTime = OffsetDateTime.of(clientTime, clientZoneId.getRules().getOffset(clientTime));

		map.put("Machine ZoneId", machineZoneId);
		map.put("Machine Time", machineTime);
		map.put("Machine OffsetTime", machineOffsetTime);
		map.put("Machine ZonedTime", machineZonedTime);
		map.put("Server ZoneId", serverZoneId);
		map.put("Server Time " + createBadge(messages.get(lang(), MessageKeys.SERVER)), serverTime);
		map.put("Server OffsetTime", serverOffsetTime);
		map.put("Server ZonedTime", serverZonedTime);
		map.put("Client ZoneId", clientZoneId);
		map.put("Client Time " + createBadge(messages.get(lang(), MessageKeys.CLIENT)), clientTime);
		map.put("Client OffsetTime", clientOffsetTime);
		map.put("Client ZonedTime", clientZonedTime);
		map.put("Client Formated Full DateTime", DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Long DateTime", DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG).withLocale(lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Medium DateTime", DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Short DateTime", DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Full Date", DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Long Date", DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Medium Date", DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Short Date", DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Full Time", DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL).withLocale(lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Long Time", DateTimeFormatter.ofLocalizedTime(FormatStyle.LONG).withLocale(lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Medium Time", DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).withLocale(lang().toLocale()).format(clientZonedTime));
		map.put("Client Formated Short Time", DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(lang().toLocale()).format(clientZonedTime));
		map.put("Server = Client", serverZonedTime.isEqual(clientZonedTime));

		final JapaneseDate japaneseDate = JapaneseDate.from(clientTime);
		map.put("Client(Japan) Year of Era", japaneseDate.format(DateTimeFormatter.ofPattern("G y").withLocale(lang().toLocale())));

		final HijrahDate hijrahDate = HijrahDate.from(clientTime);
		map.put("Client(Hijrah) Year of Era", hijrahDate.format(DateTimeFormatter.ofPattern("G y").withLocale(lang().toLocale())));

		final MinguoDate minguoDate = MinguoDate.from(clientTime);
		map.put("Client(Minguo) Year of Era", minguoDate.format(DateTimeFormatter.ofPattern("G y").withLocale(lang().toLocale())));

		final ThaiBuddhistDate thaiBuddhistDate = ThaiBuddhistDate.from(clientTime);
		map.put("Client(ThaiBuddhist) Year of Era", thaiBuddhistDate.format(DateTimeFormatter.ofPattern("G y").withLocale(lang().toLocale())));

		return ok(views.html.framework.application.datetime.render(map));
	}

	public Result entitymanager() {

		final SessionImpl entityManager = (SessionImpl) jpaApi.em();

		final Map<String, Object> propertiesMap = entityManager.getProperties();

		final Map<String, Object> platformMap = new LinkedHashMap<>();
		try {

			final DatabaseMetaData metaData = entityManager.connection().getMetaData();
			platformMap.put("DatabaseProductName", metaData.getDatabaseProductName());
			platformMap.put("DatabaseProductVersion", metaData.getDatabaseProductVersion());
			platformMap.put("DriverName", metaData.getDriverName());
			platformMap.put("DriverVersion", metaData.getDriverVersion());
		} catch (HibernateException | SQLException e) {

			throw new RuntimeException();
		}

		return ok(views.html.framework.application.entitymanager.render(//
				new TreeMap<>(propertiesMap), //
				new TreeMap<>(platformMap)));
	}

	@SuppressWarnings("null")
	public Result user() {

		final Map<String, Object> map = new LinkedHashMap<>();

		final User user;
		try {

			user = userService.read(jpaApi.em(), session(User.USERID));
		} catch (final AccountException e) {

			throw new RuntimeException(e);
		}

		map.put(User.USERID, user.getUserId());
		map.put(User.ROLES, user.getRoles());
		map.put("permissions", user.getPermissions());
		map.put("expireTime(UTC)", user.getExpireDateTime().toString());
		map.put("signInTime(UTC)", Objects.toString(user.getSignInDateTime(), ""));
		map.put("signOutTime(UTC)", Objects.toString(user.getSignOutDateTime(), ""));
		map.put("updateTime(UTC)", user.getUpdateDateTime().toString());
		map.put("expireTime", DateTimes.getClientDateTime(user.getExpireDateTime()).toString());
		final LocalDateTime signInTime = user.getSignInDateTime() != null ? DateTimes.getClientDateTime(user.getSignInDateTime()) : null;
		map.put("signInTime", Objects.toString(signInTime, ""));
		final LocalDateTime signOutTime = user.getSignOutDateTime() != null ? DateTimes.getClientDateTime(user.getSignOutDateTime()) : null;
		map.put("signOutTime", Objects.toString(signOutTime, ""));
		map.put("updateTime", DateTimes.getClientDateTime(user.getUpdateDateTime()).toString());
		return ok(views.html.framework.application.user.render(map));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public CompletionStage<Result> synccache() {

		final JsonNode jsonNode = request().body().asJson();
		final String key = jsonNode.get("key") != null ? jsonNode.get("key").textValue() : "";
		final String value = jsonNode.get("value") != null ? jsonNode.get("value").textValue() : "";

		if (key.isEmpty() && value.isEmpty()) {

			badRequest(jsonNode);
		}

		final ObjectNode result = Json.newObject();
		if (value.isEmpty()) {
			// get

			final String cachedValue = syncCacheApi.getOrElseUpdate(key, () -> "Cache is empty.", 60 * 60);

			result.put("key", key);
			result.put("value", cachedValue);
		} else {
			// set

			syncCacheApi.set(key, value, 60 * 60);

			result.put("key", key);
			result.put("value", value);
		}

		return CompletableFuture.supplyAsync(() -> {

			return ok(result);
		});
	}

	@BodyParser.Of(BodyParser.Json.class)
	public CompletionStage<Result> asynccache() {

		final JsonNode jsonNode = request().body().asJson();
		final String key = jsonNode.get("key") != null ? jsonNode.get("key").textValue() : "";
		final String value = jsonNode.get("value") != null ? jsonNode.get("value").textValue() : "";

		if (key.isEmpty() && value.isEmpty()) {

			badRequest(jsonNode);
		}

		final ObjectNode result = Json.newObject();
		if (value.isEmpty()) {
			// get

			final CompletionStage<String> stage = aSyncCacheApi.getOrElseUpdate(key, () -> {

				return CompletableFuture.supplyAsync(() -> {

					return "Cache is empty.";
				});
			}, 60 * 60);
			final String cachedValue;
			try {

				cachedValue = stage.toCompletableFuture().get();
			} catch (InterruptedException | ExecutionException e) {

				throw new RuntimeException(e);
			}

			result.put("key", key);
			result.put("value", cachedValue);
		} else {
			// set

			final CompletionStage<Done> stage = aSyncCacheApi.set(key, value, 60 * 60);

			try {

				stage.toCompletableFuture().get();

				result.put("key", key);
				result.put("value", value);
			} catch (InterruptedException | ExecutionException e) {

				throw new RuntimeException(e);
			}
		}

		return CompletableFuture.supplyAsync(() -> {

			return ok(result);
		});
	}

	public Result playException() {

		throw new PlayException(messages.get(lang(), MessageKeys.TITLE), messages.get(lang(), MessageKeys.DESCRIPTION));
	}

	public Result runtimeException() {

		throw new RuntimeException(messages.get(lang(), MessageKeys.MESSAGE));
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
