package controllers.lab.fork;

import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;

@PermissionsAllowed
public class Fork extends Controller {

	public Result index() {

		return ok(views.html.lab.fork.index.render());
	}
}
