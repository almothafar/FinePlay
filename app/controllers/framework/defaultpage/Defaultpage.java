package controllers.framework.defaultpage;

import models.system.System.PermissionsAllowed;
import play.api.PlayException;
import play.mvc.Controller;
import play.mvc.Result;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import play.i18n.Messages;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;
import scala.Option;

@PermissionsAllowed
public class Defaultpage extends Controller {

	@Inject
	private MessagesApi messagesApi;

	@Authenticated(common.core.Authenticator.class)
	public Result index(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return ok(views.html.framework.defaultpage.index.render(request, lang, messages));
	}

	@Authenticated(common.core.Authenticator.class)
	public Result page(@Nonnull final Request request, String item) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (item) {
		case "notfound":

			return notFoundPage(request, lang, messages);
		case "badrequest":

			return badRequestPage(request, lang, messages);
		case "unauthorized":

			return unauthorizedPage(request, lang, messages);
		case "deverror":

			return devError(request, lang, messages);
		case "devnotfound":

			return devNotFound(request, lang, messages);
		case "error":

			return error(request, lang, messages);
		case "todo":

			return todo(request, lang, messages);
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	public static Result notFoundPage(final Request request, final Lang lang, final Messages messages) {

		return notFound(views.html.defaultpages.notFound.render("arg0", "arg1", request.asScala()));
	}

	public static Result badRequestPage(final Request request, final Lang lang, final Messages messages) {

		return badRequest(views.html.defaultpages.badRequest.render("arg0", "arg1", "arg2", request.asScala()));
	}

	public static Result unauthorizedPage(final Request request, final Lang lang, final Messages messages) {

		return unauthorized(views.html.defaultpages.unauthorized.render(request.asScala()));
	}

	public static Result devError(final Request request, final Lang lang, final Messages messages) {

		// Dev
		return ok(views.html.defaultpages.devError.render(Option.apply(null), new PlayException("title", "description", new Exception("message")), request.asScala()));
	}

	public static Result devNotFound(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.defaultpages.devNotFound.render("arg0", "arg1", Option.apply(null), request.asScala()));
	}

	public static Result error(final Request request, final Lang lang, final Messages messages) {

		// Prod
		return ok(views.html.defaultpages.error.render(new PlayException("title", "description", new Exception("message")), request.asScala()));
	}

	public static Result todo(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.defaultpages.todo.render(request.asScala()));
	}
}
