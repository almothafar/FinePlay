package controllers.development.document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import play.mvc.Controller;
import models.system.System.PermissionsAllowed;
import play.mvc.Result;

@PermissionsAllowed
public class Document extends Controller {

	public Result index(String state) {

		switch (state) {
			case "troubleshoot" :

				return troubleshoot();
			case "faq" :

				return faq();
			default :

				return notFound(views.html.error.notfound.render(request().method(), request().uri()));
		}
	}

	public Result troubleshoot() {

		try (final InputStream inputStream = play.Environment.simple().resourceAsStream("resources/development/document/troubleshoot/troubleshoot_" + lang().code().replace('-', '_') + ".md"); //
				final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

			final String markdown = reader.lines().collect(Collectors.joining("\n"));

			return ok(views.html.development.document.troubleshoot.render(markdown));
		} catch (IOException e) {

			throw new RuntimeException(e);
		}
	}

	public Result faq() {

		try (final InputStream inputStream = play.Environment.simple().resourceAsStream("resources/development/document/faq/faq_" + lang().code().replace('-', '_') + ".json"); //
				final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

			final String troubleshootJson = reader.lines().collect(Collectors.joining("\n"));

			final ObjectMapper mapper = new ObjectMapper();
			ArrayNode arrayNode;
			try {

				arrayNode = mapper.readValue(troubleshootJson, ArrayNode.class);
			} catch (IOException e) {

				throw new RuntimeException(e);
			}
			final List<Entry<String, String>> things = toList(arrayNode);

			return ok(views.html.development.document.faq.render(things));
		} catch (IOException e) {

			throw new RuntimeException(e);
		}
	}

	private static List<Entry<String, String>> toList(@Nonnull final ArrayNode arrayNode) {

		final List<Entry<String, String>> things = new ArrayList<>();
		for (int i = 0; i < arrayNode.size(); i++) {

			final JsonNode node = arrayNode.get(i);

			final String title = node.get("title").asText();
			final String text = node.get("text").asText();
			final Entry<String, String> thing = new SimpleImmutableEntry<>(title, text);

			things.add(thing);
		}

		return Collections.unmodifiableList(things);
	}
}