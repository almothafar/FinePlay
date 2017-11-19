package controllers.fontawesome.style;

import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Style extends Controller {

	@Authenticated(common.core.Authenticator.class)
	public Result index(String example) {

		switch (example) {
		case "squareicon":

			return squareicon();
		case "largericon":

			return largericon();
		default:

			return notFound(views.html.system.pages.notfound.render(request().method(), request().uri()));
		}
	}

	public Result squareicon() {

		return ok(views.html.fontawesome.style.squareicon.render());
	}

	public Result largericon() {

		return ok(views.html.fontawesome.style.largericon.render());
	}
}
