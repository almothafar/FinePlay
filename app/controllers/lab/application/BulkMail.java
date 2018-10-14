package controllers.lab.application;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

import common.utils.JSONs;
import common.utils.Strings;
import models.system.System.PermissionsAllowed;
import play.api.PlayException;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class BulkMail extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private Config config;

	@Inject
	private JPAApi jpa;

	@Inject
	private WSClient ws;

	@Inject
	private MailerClient mailer;

	@Inject
	private FormFactory formFactory;

	@Authenticated(common.core.Authenticator.class)
	public Result index() {

		return ok(views.html.lab.application.bulkmail.render());
	}

	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result send() {

		final DynamicForm targetForm = formFactory.form().bindFromRequest();
		final Map<String, String> data = targetForm.rawData();

		if (!data.containsKey("smtpapiJSON")) {

			return badRequest(createErrorResult("smtpapiJSON is none."));
		}
		if (!data.containsKey("templateText")) {

			return badRequest(createErrorResult("templateText is none."));
		}

		final String smtpapiJSON = data.get("smtpapiJSON");
		final String templateText = data.get("templateText");

		return jpa.withTransaction(manager -> {
			// read DB、File、etc...

			try {

				sendBulkEmail(sanitizeSmtpApi(smtpapiJSON), sanitizeTemplate(templateText));
				return ok(createResult());
			} catch (PlayException | IllegalStateException e) {

				return badRequest(createErrorResult(e.getLocalizedMessage()));
			}
		});
	}

	private String sanitizeSmtpApi(@Nonnull final String smtpapi) {

		final ObjectNode smtpapiNode = JSONs.toBean(smtpapi, ObjectNode.class);

		// Schedule
		final Duration duration = Duration.ofMinutes(1);
		final long sendAt = Instant.now().plus(duration.toMinutes(), ChronoUnit.MINUTES).getEpochSecond();// after 1 min.
		smtpapiNode.put("send_at", sendAt);

		// Cancel Schedule
		final String batchID = getBatchID();
		smtpapiNode.put("batch_id", batchID);

		String sanitizedSmtpapi = JSONs.toJSON(smtpapiNode);

		// see SendGrid spec.

		sanitizedSmtpapi = sanitizedSmtpapi.replaceAll(Strings.REGEX_NEW_LINE, "\r\n");

		return sanitizedSmtpapi;
	}

	private String sanitizeTemplate(@Nonnull final String template) {

		String sanitizedTemplate = template;

		// see SendGrid spec.

		return sanitizedTemplate;
	}

	private void sendBulkEmail(@Nonnull final String smtpapi, @Nonnull String template) {

		LOGGER.info(smtpapi);
		LOGGER.info(template);

		final Email email = new Email()//
				.setHeaders(Map.of("X-SMTPAPI", smtpapi))//
				.setSubject("Dear {{name}} Weekly news")//
				.setFrom("From<from@example.com>")//
				.addTo("to@example.com")// for Commons Email measures.
				.setBodyHtml(template);

		mailer.send(email);
	}

	private JsonNode createResult() {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		return result;
	}

	private JsonNode createErrorResult(@Nonnull final String errorMessage) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		result.put("error", errorMessage);

		return result;
	}

	private final String getBatchID() {

		final Duration timeout = Duration.ofSeconds(10);

		final String apiKey = getApiKey();
		final WSRequest wsRequest = ws.url("https://api.sendgrid.com/v3/mail/batch");
		wsRequest.addHeader("Content-Type", "application/json");
		wsRequest.addHeader("Authorization", "Bearer " + apiKey);
		final CompletionStage<WSResponse> responsePromise = wsRequest.setRequestTimeout(timeout).post("");

		final CompletionStage<String> recoverPromise = responsePromise.handle((response, throwable) -> {

			if (Objects.isNull(throwable)) {

				if (Http.Status.CREATED == response.getStatus()) {

					final String batchID = response.asJson().get("batch_id").textValue();
					return batchID;
				} else {

					final String errors = response.asJson().textValue();
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

	private String getApiKey() {

		final String apikey = config.getString("sendgrid.api_key");

		Objects.requireNonNull(apikey);

		return apikey;
	}
}
