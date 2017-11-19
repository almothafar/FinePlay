package apis.transrator;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.Config;

import models.system.System.PermissionsAllowed;
import play.db.jpa.Transactional;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Transrator extends Controller {

	@Inject
	private Config config;

	@Inject
	private WSClient ws;

	@Transactional(readOnly = true)
	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result translate(@Nullable final String from, @Nonnull final String to, @Nonnull final String text) {

		try {

			final String translatedText = getTranslatedText(from, to, text);
			return ok(createResult(translatedText));
		} catch (IllegalStateException e) {

			return badRequest(createErrorResult(e.getLocalizedMessage()));
		}
	}

	private String getTranslatedText(@Nullable final String from, @Nonnull final String to, @Nonnull final String text) {

		try {

			final CompletionStage<Map<String, String>> accessTokenStage;
			try {

				accessTokenStage = getAccessToken(from, to, text);
			} catch (NullPointerException e) {

				throw new IllegalStateException(e.getLocalizedMessage());
			}

			final Map<String, String> map = accessTokenStage.toCompletableFuture().get();

			if (!map.containsKey("error")) {

				return map.get("response");
			} else {

				throw new IllegalStateException(map.get("error"));
			}
		} catch (InterruptedException | ExecutionException e) {

			throw new RuntimeException(e);
		}
	}

	private JsonNode createResult(@Nonnull final String translatedText) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		result.put("text", translatedText);

		return result;
	}

	private JsonNode createErrorResult(@Nonnull final String errorMessage) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		result.put("text", "");
		result.put("error", errorMessage);

		return result;
	}

	private final CompletionStage<Map<String, String>> getAccessToken(@Nullable final String from, @Nonnull final String to, @Nonnull final String text) {

		final Duration timeout = Duration.ofSeconds(10);

		final String subscriptionKey = getSubscriptionKey();
		final WSRequest wsRequest = ws.url("https://api.cognitive.microsoft.com/sts/v1.0/issueToken");
		wsRequest.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
		final CompletionStage<WSResponse> responsePromise = wsRequest.setRequestTimeout(timeout).post("");

		final CompletionStage<Map<String, String>> recoverPromise = responsePromise.handle((response, throwable) -> {

			final Map<String, String> map = new LinkedHashMap<>();
			if (throwable == null) {

				if (Http.Status.OK == response.getStatus()) {

					final String accessToken = response.getBody();

					try {

						return executeTranslate(accessToken, from, to, text).toCompletableFuture().get();
					} catch (InterruptedException | ExecutionException e) {

						throw new RuntimeException(e);
					}
				} else if (Http.Status.UNAUTHORIZED == response.getStatus()) {

					map.put("error", "Get access token[Unauthorized. Ensure that the key provided is valid.]");
				} else if (Http.Status.PAYMENT_REQUIRED == response.getStatus()) {

					map.put("error", "Get access token[Unauthorized. For an account in the free-tier, this indicates that the account quota has been exceeded.]");
				} else {

					map.put("error", "Get access token[HTTP Status is not OK. :" + response.getStatus() + "]");
				}
			} else {

				map.put("error", "Get access token[" + throwable.getLocalizedMessage() + "]");
			}

			return Collections.unmodifiableMap(map);
		});

		return recoverPromise;
	}

	private String getSubscriptionKey() {

		final String subscriptionKey = config.getString("microsoft.transrator.subscription_key");

		Objects.requireNonNull(subscriptionKey);

		return subscriptionKey;
	}

	private final CompletionStage<Map<String, String>> executeTranslate(//
			@Nonnull final String accessToken, //
			@Nullable final String from, //
			@Nonnull final String to, //
			@Nonnull final String text) {

		final Duration timeout = Duration.ofSeconds(10);

		final WSRequest wsRequest = ws.url("https://api.microsofttranslator.com/v2/http.svc/Translate");
		wsRequest.addQueryParameter("appid", "Bearer " + accessToken);
		if (Objects.nonNull(from)) {

			wsRequest.addQueryParameter("from", from);
		}
		wsRequest.addQueryParameter("to", to);
		wsRequest.addQueryParameter("text", text);
		final CompletionStage<WSResponse> responsePromise = wsRequest.setRequestTimeout(timeout).get();

		final CompletionStage<Map<String, String>> translateRecoverPromise = responsePromise.handle((response, throwable) -> {

			final Map<String, String> map = new LinkedHashMap<>();
			if (throwable == null) {

				if (Http.Status.OK == response.getStatus()) {

					final String translatedText = response.asXml().getDocumentElement().getTextContent();
					map.put("response", translatedText);
				} else if (Http.Status.BAD_REQUEST == response.getStatus()) {

					map.put("error", "Translate[" + response.getStatus() + "]");
				} else {

					map.put("error", "Translate[HTTP Status is not OK. :" + response.getStatus() + "]");
				}
			} else {

				map.put("error", "Translate[" + throwable.getLocalizedMessage() + "]");
			}

			return Collections.unmodifiableMap(map);
		});

		return translateRecoverPromise;
	}
}
