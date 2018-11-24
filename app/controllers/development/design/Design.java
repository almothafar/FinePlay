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
import javax.annotation.Nonnull;
import javax.inject.Inject;
import play.i18n.Messages;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Http.Request;

@PermissionsAllowed
public class Design extends Controller {

	@Inject
	private MessagesApi messagesApi;

	public Result index(@Nonnull final Request request, String item) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (item) {
		case "theme":

			return theme(request, lang, messages);
		case "icon":

			return icon(request, lang, messages);
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	public static Result theme(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.development.design.theme.render(request, lang, messages));
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

	public static Result icon(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.development.design.icon.render(//
				LazyHolder.FONTAWESOME_SOLID_ICON_NAMES, //
				LazyHolder.FONTAWESOME_REGULAR_ICON_NAMES, //
				LazyHolder.FONTAWESOME_BRANDS_REGULAR_ICON_NAMES, //
				LazyHolder.MATERIAL_DESIGN_ICON_NAMES, //
				LazyHolder.IONICONS_NAMES, //
				LazyHolder.ICOFONT_ICON_NAMES, //
				LazyHolder.TWEMOJIAWESOME_ICON_NAMES, //
				request, lang, messages));
	}
}
