package controllers.development.design;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;

@PermissionsAllowed
public class Design extends Controller {

	public Result index(String item) {

		switch (item) {
		case "theme":

			return theme();
		case "icon":

			return icon();
		default:

			return notFound(views.html.system.pages.notfound.render(request().method(), request().uri()));
		}
	}

	public static Result theme() {

		return ok(views.html.development.design.theme.render());
	}

	private static class LazyHolder {

		private static List<String> FONTAWESOME_SOLID_ICON_NAMES = initIconNames(Paths.get(".", "conf", "resources", "development", "design", "icon", "font-awesome-solid.txt"));
		private static List<String> FONTAWESOME_REGULAR_ICON_NAMES = initIconNames(Paths.get(".", "conf", "resources", "development", "design", "icon", "font-awesome-regular.txt"));
		private static List<String> FONTAWESOME_BRANDS_REGULAR_ICON_NAMES = initIconNames(Paths.get(".", "conf", "resources", "development", "design", "icon", "font-awesome-brands-regular.txt"));
		private static List<String> MATERIAL_DESIGN_ICON_NAMES = initIconNames(Paths.get(".", "conf", "resources", "development", "design", "icon", "material-design.txt"));
		private static List<String> IONICONS_NAMES = initIconNames(Paths.get(".", "conf", "resources", "development", "design", "icon", "ionicons.txt"));
		private static List<String> ICOFONT_ICON_NAMES = initIconNames(Paths.get(".", "conf", "resources", "development", "design", "icon", "icofont.txt"));
		private static List<String> TWEMOJIAWESOME_ICON_NAMES = initIconNames(Paths.get(".", "conf", "resources", "development", "design", "icon", "twemoji-awesome.txt"));
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
				LazyHolder.FONTAWESOME_SOLID_ICON_NAMES, //
				LazyHolder.FONTAWESOME_REGULAR_ICON_NAMES, //
				LazyHolder.FONTAWESOME_BRANDS_REGULAR_ICON_NAMES, //
				LazyHolder.MATERIAL_DESIGN_ICON_NAMES, //
				LazyHolder.IONICONS_NAMES, //
				LazyHolder.ICOFONT_ICON_NAMES, //
				LazyHolder.TWEMOJIAWESOME_ICON_NAMES));
	}
}
