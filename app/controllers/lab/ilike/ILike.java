package controllers.lab.ilike;

import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;

@PermissionsAllowed
public class ILike extends Controller {

	public Result index() {

		return ok(views.html.lab.ilike.index.render());
	}
}
