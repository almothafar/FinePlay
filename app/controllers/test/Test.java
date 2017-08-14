package controllers.test;

import play.mvc.Controller;
import play.mvc.Result;

public class Test extends Controller {

	public Result index() {

		return ok(views.html.test.index.render());
	}
}
