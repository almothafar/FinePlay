package controllers.development.design;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import play.mvc.Controller;
import models.system.System.PermissionsAllowed;
import play.mvc.Result;

@PermissionsAllowed
public class Design extends Controller {

	public Result index(String item) {

		switch (item) {
			case "theme" :

				return theme();
			case "icon" :

				return icon();
			default :

				return notFound(views.html.error.notfound.render(request().method(), request().uri()));
		}
	}

	public static Result theme() {

		return ok(views.html.development.design.theme.render());
	}

	private static class LazyHolder {

		private static List<String> FONTAWESOME_ICON_NAMES = initIconNames(Paths.get(".", "conf", "development", "design", "icon", "font-awesome.txt"));
		private static List<String> MATERIAL_DESIGN_ICON_NAMES = initIconNames(Paths.get(".", "conf", "development", "design", "icon", "material-design.txt"));
		private static List<String> ICOFONT_ICON_NAMES = initIconNames(Paths.get(".", "conf", "development", "design", "icon", "icofont.txt"));
	}

	private static List<String> initIconNames(final Path path) {

		if (!Files.exists(path)) {

			throw new RuntimeException(": " + path);
		}

		try {

			return Files.readAllLines(path, StandardCharsets.UTF_8);
		} catch (IOException e) {

			throw new UncheckedIOException(e);
		}
	}

	public static Result icon() {

		return ok(views.html.development.design.icon.render(//
				LazyHolder.FONTAWESOME_ICON_NAMES, //
				LazyHolder.MATERIAL_DESIGN_ICON_NAMES, //
				LazyHolder.ICOFONT_ICON_NAMES));
	}
}
