package controllers.framework.welcome;

import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Welcome extends Controller {

	@Authenticated(common.core.Authenticator.class)
	public Result index() {

		return ok(views.html.framework.welcome.index.render());
	}
}
