package controllers.test;

import play.mvc.Controller;
import play.mvc.Result;
import javax.annotation.Nonnull;
import play.mvc.Http.Request;

public class Test extends Controller {

	public Result index(@Nonnull final Request request) {

		return ok(views.html.test.index.render());
	}
}
