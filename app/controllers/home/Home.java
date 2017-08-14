package controllers.home;

import javax.inject.Inject;

import play.mvc.Controller;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed(value = {Permission.READ})
public class Home extends Controller {

	@Inject
	private views.html.home.intro intro;

	@Authenticated(common.core.Authenticator.class)
	public Result index() {

		return ok(intro.render());
	}

	@Authenticated(common.core.Authenticator.class)
	public Result video() {

		return ok(views.html.home.video.render());
	}
}
