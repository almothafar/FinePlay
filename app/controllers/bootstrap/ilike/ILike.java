package controllers.bootstrap.ilike;

import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;

@PermissionsAllowed
public class ILike extends Controller {

	public Result index() {

		return ok(views.html.bootstrap.ilike.index.render());
	}
}
