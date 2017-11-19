package controllers.development.javascript;

import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;

@PermissionsAllowed
public class JavaScript extends Controller {

	public Result index(String item) {

		switch (item) {
		case "test":

			return test();
		default:

			return notFound(views.html.system.pages.notfound.render(request().method(), request().uri()));
		}
	}

	public static Result test() {

		return ok(views.html.development.javascript.test.render());
	}

	public Result test(String testCase) {

		switch (testCase) {
		case "commonJs":

			return commonJs();
		case "stringsJs":

			return stringsJs();
		default:

			return notFound(views.html.system.pages.notfound.render(request().method(), request().uri()));
		}
	}

	public static Result commonJs() {

		return ok(views.html.development.javascript.testpage.commonJsTest.render());
	}

	public static Result stringsJs() {

		return ok(views.html.development.javascript.testpage.stringsJsTest.render());
	}
}
