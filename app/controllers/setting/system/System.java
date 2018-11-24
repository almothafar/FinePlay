package controllers.setting.system;

import models.system.System.Permission;
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

@PermissionsAllowed(value = { Permission.READ, Permission.WRITE })
public class System extends Controller {

	@Inject
	private MessagesApi messagesApi;

	@Authenticated(common.core.Authenticator.class)
	public Result index(@Nonnull final Request request, String item) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (item) {
		case "general":

			return general(request, lang, messages);
		case "font":

			return font(request, lang, messages);
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	public static Result general(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.setting.system.general.render(request, lang, messages));
	}

	public static Result font(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.setting.system.font.render(request, lang, messages));
	}
}
