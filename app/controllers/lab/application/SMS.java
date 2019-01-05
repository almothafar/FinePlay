package controllers.lab.application;

import java.lang.invoke.MethodHandles;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.Config;

import models.system.System.PermissionsAllowed;
import play.api.PlayException;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Lang;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.libs.ws.WSAuthScheme;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.libs.ws.ahc.AhcCurlRequestLogger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class SMS extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private Config config;

	@Inject
	private MessagesApi messagesApi;

	@Inject
	private JPAApi jpaApi;

	@Inject
	private WSClient ws;

	@Inject
	private FormFactory formFactory;

	@Authenticated(common.core.Authenticator.class)
	public Result index(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return ok(views.html.lab.application.sms.render(request, lang, messages));
	}

	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result send(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		messages.lang();

		final DynamicForm targetForm = formFactory.form().bindFromRequest(request);
		final Map<String, String> data = targetForm.rawData();

		if (!data.containsKey("to")) {

			return badRequest(createErrorResult("To is none."));
		}
		if (!data.containsKey("message")) {

			return badRequest(createErrorResult("Message is none."));
		}

		final String to = data.get("to");
		final String message = data.get("message");

		return jpaApi.withTransaction(manager -> {
			// read DB、File、etc...

			try {

				final String result = sendSMS(to, message);
				return ok(result);
			} catch (PlayException | IllegalStateException e) {

				return badRequest(createErrorResult(e.getLocalizedMessage()));
			}
		});
	}

	private JsonNode createErrorResult(@Nonnull final String errorMessage) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		result.put("error", errorMessage);

		return result;
	}

	private final String sendSMS(final String to, final String message) {

		System.out.println(to);
		final Duration timeout = Duration.ofSeconds(10);

		final CompletionStage<WSResponse> responsePromise = ws.url("https://api.twilio.com/2010-04-01/Accounts/" + getAccountSID() + "/Messages.json")//
				.setRequestFilter(new AhcCurlRequestLogger(LOGGER))//
				.setAuth(getAccountSID(), getAuthToken(), WSAuthScheme.BASIC)//
				.setContentType("application/x-www-form-urlencoded")//
				.setRequestTimeout(timeout).post(//
						"From=" + URLEncoder.encode(getFrom(), StandardCharsets.UTF_8) + "&" + //
								"Body=" + URLEncoder.encode(message, StandardCharsets.UTF_8) + "&" + //
								"To=" + URLEncoder.encode(to, StandardCharsets.UTF_8));

		final CompletionStage<String> recoverPromise = responsePromise.handle((response, throwable) -> {

			if (Objects.isNull(throwable)) {

				if (Http.Status.CREATED == response.getStatus()) {

					final String result = response.asJson().toString();
					return result;
				} else {

					final String errors = response.asJson().get("message").textValue();
					throw new IllegalStateException(errors);
				}
			} else {

				throw new IllegalStateException(throwable.getLocalizedMessage());
			}
		});

		try {

			return recoverPromise.toCompletableFuture().get();
		} catch (InterruptedException | ExecutionException e) {

			throw new IllegalStateException(e.getLocalizedMessage());
		}
	}

	private String getAccountSID() {

		return getValue("twilio.message.account_sid");
	}

	private String getAuthToken() {

		return getValue("twilio.message.auth_token");
	}

	private String getFrom() {

		return getValue("twilio.message.from");
	}

	private String getValue(final String key) {

		final String value = config.getString(key);

		Objects.requireNonNull(value);

		return value;
	}
}