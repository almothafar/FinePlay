package controllers.lab.application;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.CoordinatedShutdown;
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
public class Chat extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private MessagesApi messagesApi;

	private final ActorSystem actorSystem;
	private final Materializer materializer;

	@Inject
	public Chat(final ActorSystem actorSystem, final Materializer materializer) {

		this.actorSystem = actorSystem;
		this.materializer = materializer;

		CoordinatedShutdown.get(actorSystem).addJvmShutdownHook(() -> System.out.println("custom JVM shutdown hook..."));
	}

	@Authenticated(common.core.Authenticator.class)
	public Result index(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return ok(views.html.lab.application.chat.render(request, lang, messages));
	}

	public play.mvc.WebSocket enter() {

		return play.mvc.WebSocket.Text.acceptOrResult(request -> {

			final Optional<String> userIdOpt = request.session().get(models.user.User_.USER_ID);
			if (userIdOpt.isPresent()) {

				final Function<ActorRef, Props> createProps = (memberRef) -> Props.create(Member.class, memberRef, userIdOpt.get());
				return CompletableFuture.completedFuture(F.Either.Right(ActorFlow.actorRef(createProps, actorSystem, materializer)));
			} else {
				// Reject

				return CompletableFuture.completedFuture(F.Either.Left(forbidden()));
			}
		});
	}

	public static class Member extends AbstractActor {

		private final ActorRef memberRef;
		private final String userId;

		public static Props props(final ActorRef memberRef, final String userId) {

			return Props.create(Member.class, memberRef, userId);
		}

		public Member(final ActorRef memberRef, final String userId) {

			this.memberRef = memberRef;
			this.userId = userId;

			getContext().getSystem().eventStream().subscribe(getSelf(), Message.class);
		}

		@Override
		public Receive createReceive() {

			return receiveBuilder()//
					.match(String.class, text -> {

						getContext().getSystem().eventStream().publish(new Message(getUserId(), text));
					})//
					.match(Message.class, message -> {

						if (!getUserId().equals(message.getUserId())) {

							final ObjectMapper mapper = new ObjectMapper();
							final ObjectNode result = mapper.createObjectNode();
							result.put("sender", message.getUserId());
							result.put("text", message.getText());

							memberRef.tell(mapper.writeValueAsString(result), self());
						}
					})//
					.build();
		}

		@Override
		public void postStop() throws Exception {

			super.postStop();

			// clean resource
		}

		public String getUserId() {

			return userId;
		}

		public static class Message {

			private final String userId;
			private final String text;

			public Message(final String userId, final String text) {

				this.userId = userId;
				this.text = text;
			}

			public String getUserId() {

				return userId;
			}

			public String getText() {

				return text;
			}
		}
	}
}
