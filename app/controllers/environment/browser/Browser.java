package controllers.environment.browser;

import java.util.TreeMap;

import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;

@PermissionsAllowed
public class Browser extends Controller {

	public Result index(String item) {

		switch (item) {
		case "navigator":

			return navigator();
		case "window":

			return window();
		case "document":

			return document();
		case "box":

			return box();
		case "position":

			return position();
		default:

			return notFound(views.html.system.pages.notfound.render(request().method(), request().uri()));
		}
	}

	public static Result navigator() {

		return ok(views.html.environment.browser.navigator.render(new TreeMap<>()));
	}

	public static Result window() {

		return ok(views.html.environment.browser.window.render(new TreeMap<>()));
	}

	public static Result document() {

		return ok(views.html.environment.browser.document.render(new TreeMap<>()));
	}

	public static Result box() {

		return ok(views.html.environment.browser.box.render(new TreeMap<>()));
	}

	public static Result position() {

		return ok(views.html.environment.browser.position.render(new TreeMap<>()));
	}
}
