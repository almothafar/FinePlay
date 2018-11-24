package controllers.bootstrap.layout;

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
public class Layout extends Controller {

	@Inject
	private MessagesApi messagesApi;

	public Result index(@Nonnull final Request request, String component) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (component) {
		case "responsive":

			return responsive(request, lang, messages);
		case "grid":

			return grid(request, lang, messages);
		case "table":

			return table(request, lang, messages);
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	public static Result responsive(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.layout.responsive.render(request, lang, messages));
	}

	private static Result grid(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.layout.grid.render(request, lang, messages));
	}

	private static Result table(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.layout.table.render(request, lang, messages));
	}
}
