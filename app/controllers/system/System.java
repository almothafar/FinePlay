package controllers.system;

import play.mvc.Controller;
import play.mvc.Result;
import play.routing.JavaScriptReverseRouter;

public class System extends Controller {

	public Result routes() {

		return ok(JavaScriptReverseRouter.create("Routes", //
				apis.system.routes.javascript.Logger.log(), //
				apis.development.http.routes.javascript.Http.ajaxdata(), //
				controllers.home.routes.javascript.Home.index())//
		).as("text/javascript");
	}
}
