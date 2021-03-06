package controllers.lab.application;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.Range;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.format.CurrencyStyle;
import org.javamoney.moneta.function.MonetaryFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.components.PagingInfo;
import models.system.System.PermissionsAllowed;
import models.user.User;
import models.user.User_;
import mylib.Greet;
import play.db.jpa.JPAApi;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.libs.ws.ahc.AhcCurlRequestLogger;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import javax.annotation.Nonnull;
import play.i18n.Messages;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Application extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private MessagesApi messagesApi;

	@Inject
	private JPAApi jpaApi;

	@Inject
	private WSClient ws;

	@Authenticated(common.core.Authenticator.class)
	public Result index(@Nonnull final Request request, String state) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (state) {
		case "autosave":

			return autoSave(request, lang, messages);
		case "storeform":

			return storeform(request, lang, messages);
		case "firsttimeaccess":

			return firstTimeAccess(request, lang, messages);
		case "imageviewer":

			return imageviewer(request, lang, messages);
		case "partprint":

			return partPrint(request, lang, messages);
		case "color":

			return color(request, lang, messages);
		case "ivd":

			return ivd(request, lang, messages);
		case "authorization":

			return authorization(request, lang, messages);
		case "jar":

			return jar(request, lang, messages);
		case "jpql":

			return jpql(request, lang, messages);
		case "money":

			return money(request, lang, messages);
		case "range":

			return range(request, lang, messages);
		case "serialtask":

			return serialTask(request, lang, messages);
		case "paralleltask":

			return parallelTask(request, lang, messages);
		case "webservice":

			return webService(request, lang, messages);
		case "webfont":

			return webfont(request, lang, messages);
		case "map":

			return map(request, lang, messages);
		case "translate":

			return translate(request, lang, messages);
		case "direction":

			return direction(request, lang, messages);
		case "vertical":

			return vertical(request, lang, messages);
		case "dark":

			return dark(request, lang, messages);
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	private static Result autoSave(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.application.autosave.render(request, lang, messages));
	}

	private static Result storeform(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.application.storeform.render(request, lang, messages));
	}

	private static Result firstTimeAccess(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.application.firsttimeaccess.render(request, lang, messages));
	}

	private static Result imageviewer(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.application.imageviewer.render(request, lang, messages));
	}

	private static Result partPrint(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.application.partprint.render(request, lang, messages));
	}

	private static Result color(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.application.color.render(request, lang, messages));
	}

	private static Result ivd(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.application.ivd.render(request, lang, messages));
	}

	private static Result authorization(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.application.authorization.render(request, lang, messages));
	}

	private Result jar(final Request request, final Lang lang, final Messages messages) {

		final Greet greet = new Greet();

		return ok(greet.getHello("User"));
	}

	private Result jpql(final Request request, final Lang lang, final Messages messages) {

		return jpaApi.withTransaction(manager -> {

			final TypedQuery<User> query = manager.createNamedQuery("User.findByLocale", User.class);
			query.setParameter(User_.locale.getName(), lang.toLocale());

			return ok(query.getResultList().toString());
		});
	}

	private static Result money(final Request request, final Lang lang, final Messages messages) {

		final CurrencyUnit USD = Monetary.getCurrency("USD");
		final CurrencyUnit JPY = Monetary.getCurrency("JPY");

		final MonetaryAmountFormat format = MonetaryFormats.getAmountFormat(//
				AmountFormatQueryBuilder.of(messages.lang().locale())//
						.set(CurrencyStyle.SYMBOL)//
						.set("pattern", "¤ 0.00")//
						.build());

		final List<MonetaryAmount> monies = new ArrayList<>();
		monies.add(Money.of(new BigDecimal("10000.00"), USD));
		monies.add(Money.of(new BigDecimal("12345.60"), JPY));
		monies.add(Money.of(new BigDecimal("65432.10"), JPY));

		final MonetaryAmount average = monies.stream()//
				.collect(MonetaryFunctions.summarizingMonetary(JPY)).getAverage();
		System.out.println(average);
		System.out.println(format.format(average));

		final MonetaryAmount sum = monies.stream()//
				.filter(MonetaryFunctions.filterByExcludingCurrency(USD)).reduce(MonetaryFunctions.sum()).get();
		System.out.println(sum);
		System.out.println(format.format(sum));

		return TODO(request);
	}

	private static Result range(final Request request, final Lang lang, final Messages messages) {

		final Range<ChronoLocalDate> daysOf2020 = Range.between(LocalDate.of(2020, Month.JANUARY, 1), LocalDate.of(2020, Month.DECEMBER, 31));
		System.out.println(daysOf2020.getMinimum());
		System.out.println(daysOf2020.getMaximum());

		System.out.println(daysOf2020.contains(LocalDate.of(2019, Month.DECEMBER, 31)));
		System.out.println(daysOf2020.contains(LocalDate.of(2020, Month.JANUARY, 1)));
		System.out.println(daysOf2020.contains(LocalDate.of(2020, Month.DECEMBER, 31)));
		System.out.println(daysOf2020.contains(LocalDate.of(2021, Month.JANUARY, 1)));

		System.out.println(daysOf2020.isStartedBy(LocalDate.of(2019, Month.DECEMBER, 31)));
		System.out.println(daysOf2020.isStartedBy(LocalDate.of(2020, Month.JANUARY, 1)));
		System.out.println(daysOf2020.isStartedBy(LocalDate.of(2020, Month.JANUARY, 2)));

		System.out.println(daysOf2020.isEndedBy(LocalDate.of(2020, Month.DECEMBER, 30)));
		System.out.println(daysOf2020.isEndedBy(LocalDate.of(2020, Month.DECEMBER, 31)));
		System.out.println(daysOf2020.isEndedBy(LocalDate.of(2021, Month.JANUARY, 1)));

		//

		final Range<Month> months = Range.between(Month.MARCH, Month.OCTOBER);
		System.out.println(months.getMinimum());
		System.out.println(months.getMaximum());

		System.out.println(months.contains(Month.FEBRUARY));
		System.out.println(months.contains(Month.MARCH));
		System.out.println(months.contains(Month.OCTOBER));
		System.out.println(months.contains(Month.NOVEMBER));

		System.out.println(months.isStartedBy(Month.FEBRUARY));
		System.out.println(months.isStartedBy(Month.MARCH));
		System.out.println(months.isStartedBy(Month.APRIL));

		System.out.println(months.isEndedBy(Month.SEPTEMBER));
		System.out.println(months.isEndedBy(Month.OCTOBER));
		System.out.println(months.isEndedBy(Month.NOVEMBER));

		//

		final List<String> rainbow = List.of("Red", "Orange", "Yellow", "Green", "Blue", "Indigo", "Violet");

		final Range<String> colors = Range.between("Orange", "Indigo", (o0,o1)->Integer.compare(rainbow.indexOf(o0), rainbow.indexOf(o1)));
		System.out.println(colors.getMinimum());
		System.out.println(colors.getMaximum());

		System.out.println(colors.contains("Red"));
		System.out.println(colors.contains("Orange"));
		System.out.println(colors.contains("Yellow"));
		System.out.println(colors.contains("Green"));
		System.out.println(colors.contains("Blue"));
		System.out.println(colors.contains("Indigo"));
		System.out.println(colors.contains("Violet"));

		System.out.println(colors.isStartedBy("Red"));
		System.out.println(colors.isStartedBy("Orange"));
		System.out.println(colors.isStartedBy("Yellow"));

		System.out.println(colors.isEndedBy("Blue"));
		System.out.println(colors.isEndedBy("Indigo"));
		System.out.println(colors.isEndedBy("Violet"));

		//

		return TODO(request);
	}

	private static Result serialTask(final Request request, final Lang lang, final Messages messages) {

		final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {

			try {

				Thread.sleep(1000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			return "result0";
		}).thenApply(beforeTask -> {

			try {

				Thread.sleep(1000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			return beforeTask + "+" + "result1";
		}).thenApply(beforeTask -> {

			try {

				Thread.sleep(1000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			return beforeTask + "+" + "result2";
		});

		try {

			final String result = future.get();
			LOGGER.info(result);
		} catch (InterruptedException | ExecutionException e) {

			e.printStackTrace();
		}

		return TODO(request);
	}

	private static Result parallelTask(final Request request, final Lang lang, final Messages messages) {

		final CompletableFuture<String> future0 = CompletableFuture.supplyAsync(() -> {//

			try {

				Thread.sleep(1000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			return "result0";
		});
		final CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {//

			try {

				Thread.sleep(1000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			return null;
		});
		final CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {//

			try {

				Thread.sleep(1000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			return "result2";
		});

		@SuppressWarnings("unused")
		final CompletableFuture<Void> future = CompletableFuture.allOf(//
				future0, //
				future1, //
				future2);

		try {

			final String result0 = future0.get();
			final String result1 = future1.get();
			final String result2 = future2.get();
			LOGGER.info(result0);
			LOGGER.info(result1);
			LOGGER.info(result2);
		} catch (InterruptedException | ExecutionException e) {

			e.printStackTrace();
		}

		return TODO(request);
	}

	@Authenticated(common.core.Authenticator.class)
	public Result paging(@Nonnull final Request request, Integer pageIndex, Integer pageSize) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final List<String> dummyValues = IntStream.range(0, 130).mapToObj(i -> String.valueOf(i)).collect(Collectors.toList());

		int pageCount = dummyValues.size() / pageSize;
		final int odd = dummyValues.size() % pageSize;
		if (0 < odd) {

			pageCount++;
		}

		final int pageStart = pageIndex * pageSize;
		int pageEnd = pageStart + pageSize;
		if (dummyValues.size() < pageEnd) {

			pageEnd = pageStart + odd;
		}
		final List<String> dummyPageValues = dummyValues.subList(pageStart, pageEnd);

		final PagingInfo pagingInfo = new PagingInfo();
		pagingInfo.setPageIndex(pageIndex);
		pagingInfo.setPageSize(pageSize);
		pagingInfo.setPageCount(pageCount);

		return ok(views.html.lab.application.paging.render(pagingInfo, dummyPageValues, request, lang, messages));
	}

	private Result webService(final Request request, final Lang lang, final Messages messages) {

		final Duration timeout = Duration.ofSeconds(3);
		final CompletionStage<WSResponse> responsePromise = ws.url("https://ntp-a1.nict.go.jp/cgi-bin/json")//
				.setRequestFilter(new AhcCurlRequestLogger(LOGGER))//
				.setRequestTimeout(timeout)//
				.get();
		final CompletionStage<Map<String, Object>> recoverPromise = responsePromise.handle((response, throwable) -> {

			final Map<String, Object> map = new LinkedHashMap<>();
			if (throwable == null) {

				if (Http.Status.OK == response.getStatus()) {

					final long epochMilli = (long) (response.asJson().get("st").doubleValue() * 1000);
					final Instant instant = Instant.ofEpochMilli(epochMilli);

					final LocalDateTime japanDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Tokyo"));
					map.put("Japan Time(NICT)", japanDateTime);
					final LocalDateTime utcDateTime = ZonedDateTime.of(japanDateTime, ZoneId.of("Asia/Tokyo")).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
					map.put("Server Time(NICT)", utcDateTime);
				} else {

					map.put("Japan Time(NICT)", response.getStatusText());
					map.put("Server Time(NICT)", response.getStatusText());
				}
			} else {

				map.put("Japan Time(NICT)", throwable.getLocalizedMessage());
				map.put("Server Time(NICT)", throwable.getLocalizedMessage());
			}

			return map;
		});

		Result result;
		try {

			final Map<String, Object> map = recoverPromise.toCompletableFuture().get();
			result = ok(views.html.lab.application.webservice.render(map, request, lang, messages));
		} catch (InterruptedException | ExecutionException e) {

			throw new RuntimeException(e);
		}

		return result;
	}

	private Result webfont(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.application.webfont.render(request, lang, messages));
	}

	private Result map(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.application.map.render(request, lang, messages));
	}

	private Result translate(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.application.translate.render(request, lang, messages));
	}

	private Result direction(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.application.direction.render(request, lang, messages));
	}

	private Result vertical(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.application.vertical.render(request, lang, messages));
	}

	private Result dark(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.application.dark.render(request, lang, messages));
	}
}
