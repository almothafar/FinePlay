package controllers.development.http;

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
public class Http extends Controller {

	@Inject
	private MessagesApi messagesApi;

	public Result index(@Nonnull final Request request, String state) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (state) {
		case "ajax":

			return ajax(request, lang, messages);
		case "serialajax":

			return serialAjax(request, lang, messages);
		case "parallelajax":

			return parallelAjax(request, lang, messages);
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	public static Result ajax(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.development.http.ajax.render(request, lang, messages));
	}

	public static Result serialAjax(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.development.http.serialajax.render(request, lang, messages));
	}

	public static Result parallelAjax(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.development.http.parallelajax.render(request, lang, messages));
	}
}
