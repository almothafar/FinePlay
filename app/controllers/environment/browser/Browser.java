package controllers.environment.browser;

import java.util.TreeMap;

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
public class Browser extends Controller {

	@Inject
	private MessagesApi messagesApi;

	public Result index(@Nonnull final Request request, String item) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (item) {
		case "navigator":

			return navigator(request, lang, messages);
		case "window":

			return window(request, lang, messages);
		case "document":

			return document(request, lang, messages);
		case "box":

			return box(request, lang, messages);
		case "position":

			return position(request, lang, messages);
		case "event":

			return event(request, lang, messages);
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	public static Result navigator(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.environment.browser.navigator.render(new TreeMap<>(), request, lang, messages));
	}

	public static Result window(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.environment.browser.window.render(new TreeMap<>(), request, lang, messages));
	}

	public static Result document(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.environment.browser.document.render(new TreeMap<>(), request, lang, messages));
	}

	public static Result box(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.environment.browser.box.render(new TreeMap<>(), request, lang, messages));
	}

	public static Result position(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.environment.browser.position.render(new TreeMap<>(), request, lang, messages));
	}

	public static Result event(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.environment.browser.event.render(new TreeMap<>(), request, lang, messages));
	}
}
