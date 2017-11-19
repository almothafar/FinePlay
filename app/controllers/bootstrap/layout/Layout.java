package controllers.bootstrap.layout;

import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;

@PermissionsAllowed
public class Layout extends Controller {

	public Result index(String component) {

		switch (component) {
		case "responsive":

			return responsive();
		case "grid":

			return grid();
		case "table":

			return table();
		default:

			return notFound(views.html.system.pages.notfound.render(request().method(), request().uri()));
		}
	}

	public static Result responsive() {

		return ok(views.html.bootstrap.layout.responsive.render());
	}

	private static Result grid() {

		return ok(views.html.bootstrap.layout.grid.render());
	}

	private static Result table() {

		return ok(views.html.bootstrap.layout.table.render());
	}
}
