package apis.development.http;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.system.System.PermissionsAllowed;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import javax.annotation.Nonnull;
import javax.inject.Inject;

import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Http.Request;

@PermissionsAllowed
public class Http extends Controller {

	@Inject
	private MessagesApi messagesApi;

	public CompletionStage<Result> getData(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		messages.lang();

		final Map<String, String[]> query = request.queryString();

		return CompletableFuture.supplyAsync(() -> {

			if (!query.containsKey("mock")) {

				return badRequest(createErrorResult("Not exist mock param. :" + request.toString()));
			}

			final String decodedMockString = decodeMockString(query.get("mock")[0]);

			final ObjectMapper mapper = new ObjectMapper();
			final JsonNode mock;
			try {
				mock = mapper.readValue(decodedMockString, JsonNode.class);
			} catch (IOException e) {

				throw new UncheckedIOException(e);
			}

			int wait = 0;
			if (mock.has("wait")) {

				wait = mock.get("wait").intValue();
			}

			if (!mock.has("response")) {

				return badRequest(createErrorResult("Not exist response key of mock param. :" + request.toString()));
			}

			final JsonNode response = mock.get("response");

			try {

				Thread.sleep(wait);
			} catch (final Exception e) {

				throw new RuntimeException(e);
			}

			return ok(response);
		});
	}

	private static String decodeMockString(@Nonnull final String userId) {

		final String decodedUserId;
		try {

			decodedUserId = URLDecoder.decode(userId, StandardCharsets.UTF_8.name());
		} catch (final UnsupportedEncodingException e) {

			throw new RuntimeException(e);
		}

		return decodedUserId;
	}

	private JsonNode createErrorResult(@Nonnull final String errorMessage) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		result.put("error", errorMessage);

		return result;
	}

	@BodyParser.Of(BodyParser.Json.class)
	public CompletionStage<Result> postData(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		messages.lang();

		final JsonNode jsonNode = request.body().asJson();

		return CompletableFuture.supplyAsync(() -> {

			if (!jsonNode.has("mock")) {

				return badRequest(jsonNode);
			}

			final JsonNode mock = jsonNode.get("mock");

			int wait = 0;
			if (mock.has("wait")) {

				wait = mock.get("wait").intValue();
			}

			if (!mock.has("response")) {

				return badRequest(jsonNode);
			}

			final JsonNode response = mock.get("response");

			try {

				Thread.sleep(wait);
			} catch (final Exception e) {

				throw new RuntimeException(e);
			}

			return ok(response);
		});
	}
}
