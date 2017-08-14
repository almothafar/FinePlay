package controllers.framework.defaultpage;

import play.mvc.Controller;
import models.system.System.PermissionsAllowed;
import play.api.PlayException;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import scala.Option;

@PermissionsAllowed
public class Defaultpage extends Controller {

	@Authenticated(common.core.Authenticator.class)
	public Result index() {

		return ok(views.html.framework.defaultpage.index.render());
	}

	@Authenticated(common.core.Authenticator.class)
	public Result page(String item) {

		switch (item) {
			case "notfound" :

				return notFoundPage();
			case "badrequest" :

				return badRequestPage();
			case "unauthorized" :

				return unauthorizedPage();
			case "deverror" :

				return devError();
			case "devnotfound" :

				return devNotFound();
			case "error" :

				return error();
			case "todo" :

				return todo();
			default :

				return notFound(views.html.error.notfound.render(request().method(), request().uri()));
		}
	}

	public static Result notFoundPage() {

		return notFound(views.html.defaultpages.notFound.render("arg0", "arg1"));
	}

	public static Result badRequestPage() {

		return badRequest(views.html.defaultpages.badRequest.render("arg0", "arg1", "arg2"));
	}

	public static Result unauthorizedPage() {

		return unauthorized(views.html.defaultpages.unauthorized.render());
	}

	public static Result devError() {

		// Dev
		return ok(views.html.defaultpages.devError.render(Option.apply(null), new PlayException("title", "description", new Exception("message"))));
	}

	public static Result devNotFound() {

		return ok(views.html.defaultpages.devNotFound.render("arg0", "arg1", Option.apply(null)));
	}

	public static Result error() {

		// Prod
		return ok(views.html.defaultpages.error.render(new PlayException("title", "description", new Exception("message"))));
	}

	public static Result todo() {

		return ok(views.html.defaultpages.todo.render());
	}
}
