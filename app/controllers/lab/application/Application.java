package controllers.lab.application;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import akka.stream.javadsl.Source;
import play.mvc.Controller;
import models.components.PagingInfo;
import models.system.System.PermissionsAllowed;
import play.Logger;
import play.Logger.ALogger;
import play.libs.Comet;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import akka.actor.Cancellable;

@PermissionsAllowed
public class Application extends Controller {

	private static final ALogger LOGGER = Logger.of(Application.class);

	@Inject
	private WSClient ws;

	@Authenticated(common.core.Authenticator.class)
	public Result index(String state) {

		switch (state) {
			case "autosave" :

				return autoSave();
			case "storeform" :

				return storeform();
			case "firsttimeaccess" :

				return firstTimeAccess();
			case "partprint" :

				return partPrint();
			case "ivd" :

				return ivd();
			case "authorization" :

				return authorization();
			case "serialtask" :

				return serialTask();
			case "paralleltask" :

				return parallelTask();
			case "webservice" :

				return webService();
			case "translate" :

				return translate();
			case "righttoleft" :

				return righttoleft();
			case "vertical" :

				return vertical();
			case "comet" :

				return comet();
			default :

				return notFound(views.html.error.notfound.render(request().method(), request().uri()));
		}
	}

	private static Result autoSave() {

		return ok(views.html.lab.application.autosave.render());
	}

	private static Result storeform() {

		return ok(views.html.lab.application.storeform.render());
	}

	private static Result firstTimeAccess() {

		return ok(views.html.lab.application.firsttimeaccess.render());
	}

	private static Result partPrint() {

		return ok(views.html.lab.application.partprint.render());
	}

	private static Result ivd() {

		return ok(views.html.lab.application.ivd.render());
	}

	private static Result authorization() {

		return ok(views.html.lab.application.authorization.render());
	}

	private static Result serialTask() {

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

		return TODO;
	}

	private static Result parallelTask() {

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

		return TODO;
	}

	@Authenticated(common.core.Authenticator.class)
	public Result paging(Integer pageIndex, Integer pageSize) {

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

		return ok(views.html.lab.application.paging.render(pagingInfo, dummyPageValues));
	}

	private Result webService() {

		final Duration timeout = Duration.ofSeconds(3);
		final CompletionStage<WSResponse> responsePromise = ws.url("https://ntp-a1.nict.go.jp/cgi-bin/json").setRequestTimeout(timeout).get();
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
			result = ok(views.html.lab.application.webservice.render(map));
		} catch (InterruptedException | ExecutionException e) {

			throw new RuntimeException(e);
		}

		return result;
	}

	private Result translate() {

		return ok(views.html.lab.application.translate.render());
	}

	private Result righttoleft() {

		return ok(views.html.lab.application.righttoleft.render());
	}

	private Result vertical() {

		return ok(views.html.lab.application.vertical.render());
	}

	public static Result comet() {

		return ok(views.html.lab.application.comet.render());
	}

	public Result clock(@Nonnull String zoneId) {

		return ok().chunked(getSource(zoneId).via(Comet.string("(function(){"//
				+ "var scripts = document.getElementsByTagName('script');"//
				+ "for(var i=scripts.length-2; 0<=i; i--){"//
				+ "	scripts[i].parentNode.removeChild(scripts[i]);"//
				+ "}})();"//
				+ "parent.clockChanged"))).as(play.mvc.Http.MimeTypes.HTML);
	}

	private Source<String, ?> getSource(@Nonnull String zoneId) {

		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		final Source<String, Cancellable> tickSource = Source.tick(scala.concurrent.duration.Duration.Zero(), scala.concurrent.duration.Duration.create(1000, TimeUnit.MILLISECONDS), "TICK");
		return tickSource.map((tick) -> {

			final LocalDateTime clientDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneOffset.UTC).withZoneSameInstant(ZoneId.of(zoneId)).toLocalDateTime();
			return formatter.format(clientDateTime);
		});
	}
}
