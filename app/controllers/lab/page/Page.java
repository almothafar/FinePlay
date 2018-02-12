package controllers.lab.page;

import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Page extends Controller {

	@Authenticated(common.core.Authenticator.class)
	public Result index(String page) {

		switch (page) {
		case "landing":

			return landing();
		case "ilike":

			return ilike();
		case "fork":

			return fork();
		default:

			return notFound(views.html.system.pages.notfound.render(request().method(), request().uri()));
		}
	}

	public static Result landing() {

		return ok(views.html.lab.page.landing.render());
	}

	public static Result ilike() {

		return ok(views.html.lab.page.ilike.render());
	}

	public static Result fork() {

		return ok(views.html.lab.page.fork.render());
	}
}
