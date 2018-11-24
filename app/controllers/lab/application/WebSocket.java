package controllers.lab.application;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.stream.Materializer;
import models.system.System.PermissionsAllowed;
import play.libs.F;
import play.libs.streams.ActorFlow;
import play.mvc.Controller;
import play.mvc.Result;
import javax.annotation.Nonnull;
import play.i18n.Messages;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class WebSocket extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private MessagesApi messagesApi;

	private final ActorSystem actorSystem;
	private final Materializer materializer;

	@Inject
	public WebSocket(final ActorSystem actorSystem, final Materializer materializer) {

		this.actorSystem = actorSystem;
		this.materializer = materializer;
	}

	@Authenticated(common.core.Authenticator.class)
	public Result index(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return ok(views.html.lab.application.websocket.render(request, lang, messages));
	}

	public play.mvc.WebSocket connect() {

		return play.mvc.WebSocket.Text.acceptOrResult(request -> {

			final Optional<String> userIdOpt = request.session().getOptional(models.user.User_.USER_ID);

			if (userIdOpt.isPresent()) {

				final ZoneId zoneId = ZoneId.of(request.session().getOptional(models.user.User_.ZONE_ID).get());
				final Function<ActorRef, Props> createProps = (ref) -> Props.create(Client.class, ref, zoneId);

				return CompletableFuture.completedFuture(F.Either.Right(ActorFlow.actorRef(createProps, actorSystem, materializer)));
			} else {

				return CompletableFuture.completedFuture(F.Either.Left(forbidden()));
			}
		});
	}

	public static class Client extends AbstractActor {

		public static Props props(final ActorRef out) {

			return Props.create(Client.class, out);
		}

		private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");

		private final ActorRef out;

		private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(100);

		public Client(final ActorRef out, final ZoneId zoneId) {

			this.out = out;

			scheduler.scheduleAtFixedRate(() -> {

				final LocalDateTime serverTime = LocalDateTime.now();
				final String clientTime = toClientTime(zoneId, serverTime);
				out.tell(clientTime, self());
			}, 0, 75, TimeUnit.MILLISECONDS);
		}

		@Override
		public Receive createReceive() {

			return receiveBuilder()//
					.match(String.class, message -> {

						out.tell("Received: " + message, self());
					})//
					.build();
		}

		private static String toClientTime(final ZoneId zoneId, final LocalDateTime time) {

			final LocalDateTime clientDateTime = ZonedDateTime.of(time, ZoneOffset.UTC).withZoneSameInstant(zoneId).toLocalDateTime();
			return FORMATTER.format(clientDateTime);
		}
	}
}
