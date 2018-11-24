package controllers.lab.page;

import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import play.i18n.Messages;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Page extends Controller {

	@Inject
	private MessagesApi messagesApi;

	@Authenticated(common.core.Authenticator.class)
	public Result index(@Nonnull final Request request, String page) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (page) {
		case "landing":

			return landing(request, lang, messages);
		case "fork":

			return fork(request, lang, messages);
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	public static Result landing(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.page.landing.render(request, lang, messages));
	}

	public static Result fork(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.page.fork.render(request, lang, messages));
	}
}
