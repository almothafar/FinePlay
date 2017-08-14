package controllers.setting.system;

import play.mvc.Controller;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed(value = {Permission.READ, Permission.WRITE})
public class System extends Controller {

	@Authenticated(common.core.Authenticator.class)
	public Result index(String item) {

		switch (item) {
			case "general" :

				return general();
			case "font" :

				return font();
			default :

				return notFound(views.html.error.notfound.render(request().method(), request().uri()));
		}
	}

	public static Result general() {

		return ok(views.html.setting.system.general.render());
	}

	public static Result font() {

		return ok(views.html.setting.system.font.render());
	}
}
