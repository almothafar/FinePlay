package controllers.fontawesome.style;

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
public class Style extends Controller {

	@Inject
	private MessagesApi messagesApi;

	@Authenticated(common.core.Authenticator.class)
	public Result index(@Nonnull final Request request, String example) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (example) {
		case "squareicon":

			return squareicon(request, lang, messages);
		case "largericon":

			return largericon(request, lang, messages);
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	public Result squareicon(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.fontawesome.style.squareicon.render(request, lang, messages));
	}

	public Result largericon(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.fontawesome.style.largericon.render(request, lang, messages));
	}
}
