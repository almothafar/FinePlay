package apis.development.http;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;

import models.system.System.PermissionsAllowed;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

@PermissionsAllowed
public class Http extends Controller {

	@BodyParser.Of(BodyParser.Json.class)
	public CompletionStage<Result> ajaxdata() {

		final JsonNode jsonNode = request().body().asJson();

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
