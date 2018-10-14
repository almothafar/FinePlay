package controllers.lab.ilike;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class ILike extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Authenticated(common.core.Authenticator.class)
	public Result index(String state, Boolean detail) {

		switch (state) {
		case "list":

			return list(detail);
		case "arrange":

			return arrange(detail);
		case "autumnboard":

			return autumnboard(detail);
		default:

			return notFound(views.html.system.pages.notfound.render(request().method(), request().uri()));
		}
	}

	private static Result list(Boolean detail) {

		return ok(views.html.lab.ilike.list.render(detail));
	}

	public static Result arrange(Boolean detail) {

		return ok(views.html.lab.ilike.arrange.render(detail));
	}

	private static Result autumnboard(Boolean detail) {

		return ok(views.html.lab.ilike.autumnboard.render(detail));
	}
}
