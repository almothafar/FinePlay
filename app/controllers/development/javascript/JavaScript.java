package controllers.development.javascript;

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
public class JavaScript extends Controller {

	@Inject
	private MessagesApi messagesApi;

	public Result index(@Nonnull final Request request, String item) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (item) {
		case "test":

			return test(request, lang, messages);
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	public static Result test(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.development.javascript.test.render(request, lang, messages));
	}

	public Result test(@Nonnull final Request request, String testCase) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (testCase) {
		case "commonJs":

			return commonJs(request, lang, messages);
		case "stringsJs":

			return stringsJs(request, lang, messages);
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	public static Result commonJs(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.development.javascript.testpage.commonJsTest.render(request, lang, messages));
	}

	public static Result stringsJs(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.development.javascript.testpage.stringsJsTest.render(request, lang, messages));
	}
}
