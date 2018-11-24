package controllers.environment.openjdk;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import play.i18n.Messages;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Http.Request;

@PermissionsAllowed
public class OpenJDK extends Controller {

	@Inject
	private MessagesApi messagesApi;

	public Result index(@Nonnull final Request request, String item) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (item) {
		case "systemproperties":

			return systemproperties(request, lang, messages);
		case "systemenv":

			return systemenv(request, lang, messages);
		case "runtime":

			return runtime(request, lang, messages);
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	public static Result systemproperties(final Request request, final Lang lang, final Messages messages) {

		final Map<String, String> systemPropertiesMap = new HashMap<>();
		System.getProperties().forEach((key, value) -> {

			final String keyString = key.toString();
			final String valueString = value.toString();

			final Consumer<String[]> putSeparatedValues = (separatedValues) -> {

				systemPropertiesMap.put(keyString, Arrays.asList(separatedValues).stream().collect(Collectors.joining("<br>")));
			};

			if (keyString.endsWith(".class.path")) {

				putSeparatedValues.accept(valueString.split(File.pathSeparator));
			} else if (keyString.endsWith(".path") || key.toString().endsWith(".dirs")) {

				putSeparatedValues.accept(valueString.split(File.pathSeparator));
			} else if (keyString.endsWith(".loader")) {

				putSeparatedValues.accept(valueString.split(","));
			} else if (keyString.equals("package.access")) {

				putSeparatedValues.accept(valueString.split(","));
			} else if (keyString.endsWith("ProxyHosts")) {

				putSeparatedValues.accept(valueString.split("\\|"));
			} else {

				systemPropertiesMap.put(keyString, valueString);
			}
		});

		return ok(views.html.environment.openjdk.systemproperties.render(new TreeMap<>(systemPropertiesMap), request, lang, messages));
	}

	public static Result systemenv(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.environment.openjdk.systemenv.render(new TreeMap<>(System.getenv()), request, lang, messages));
	}

	public static Result runtime(final Request request, final Lang lang, final Messages messages) {

		final Map<String, String> runtimeMap = new HashMap<>();
		final Runtime runtime = Runtime.getRuntime();
		runtimeMap.put("Available Processors", String.valueOf(runtime.availableProcessors()));
		runtimeMap.put("Max Memory", String.valueOf(runtime.maxMemory()));
		runtimeMap.put("Total Memory", String.valueOf(runtime.totalMemory()));
		runtimeMap.put("Free Memory", String.valueOf(runtime.freeMemory()));

		return ok(views.html.environment.openjdk.runtime.render(new TreeMap<>(runtimeMap), request, lang, messages));
	}
}
