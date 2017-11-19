package controllers.environment.javalang;

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

@PermissionsAllowed
public class Java extends Controller {

	public Result index(String item) {

		switch (item) {
		case "systemproperties":

			return systemproperties();
		case "systemenv":

			return systemenv();
		case "runtime":

			return runtime();
		default:

			return notFound(views.html.system.pages.notfound.render(request().method(), request().uri()));
		}
	}

	public static Result systemproperties() {

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

		return ok(views.html.environment.javalang.systemproperties.render(new TreeMap<>(systemPropertiesMap)));
	}

	public static Result systemenv() {

		return ok(views.html.environment.javalang.systemenv.render(new TreeMap<>(System.getenv())));
	}

	public static Result runtime() {

		final Map<String, String> runtimeMap = new HashMap<>();
		final Runtime runtime = Runtime.getRuntime();
		runtimeMap.put("Available Processors", String.valueOf(runtime.availableProcessors()));
		runtimeMap.put("Max Memory", String.valueOf(runtime.maxMemory()));
		runtimeMap.put("Total Memory", String.valueOf(runtime.totalMemory()));
		runtimeMap.put("Free Memory", String.valueOf(runtime.freeMemory()));

		return ok(views.html.environment.javalang.runtime.render(new TreeMap<>(runtimeMap)));
	}
}
