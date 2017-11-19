package controllers.development.http;

import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;

@PermissionsAllowed
public class Http extends Controller {

	public Result index(String state) {

		switch (state) {
		case "ajax":

			return ajax();
		case "serialajax":

			return serialAjax();
		case "parallelajax":

			return parallelAjax();
		default:

			return notFound(views.html.system.pages.notfound.render(request().method(), request().uri()));
		}
	}

	public static Result ajax() {

		return ok(views.html.development.http.ajax.render());
	}

	public static Result serialAjax() {

		return ok(views.html.development.http.serialajax.render());
	}

	public static Result parallelAjax() {

		return ok(views.html.development.http.parallelajax.render());
	}
}
