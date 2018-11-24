package apis.system;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import common.system.MessageKeys;
import models.system.System.PermissionsAllowed;
import play.i18n.MessagesApi;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.i18n.Messages;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Logger extends Controller {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private MessagesApi messagesApi;

	@Authenticated(common.core.Authenticator.class)
	@BodyParser.Of(BodyParser.Json.class)
	public CompletionStage<Result> log(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		messages.lang();

		final JsonNode errorReportContent = request.body().asJson();
		final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

		final String message = messages.at(MessageKeys.THANK__YOU__VERY__MUCH);
		return CompletableFuture.supplyAsync(() -> {

			try {

				final String prettyJson = mapper.writeValueAsString(errorReportContent);
				LOGGER.error("---------- Report state\n{}", prettyJson);

				return ok(createResult(message));
			} catch (JsonProcessingException e) {

				return badRequest(createErrorResult(e.getLocalizedMessage()));
			}
		});
	}

	private JsonNode createResult(@Nonnull final String message) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		result.put("message", message);

		return result;
	}

	private JsonNode createErrorResult(@Nonnull final String errorMessage) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		result.put("message", "");
		result.put("error", errorMessage);

		return result;
	}
}
