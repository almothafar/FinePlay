package controllers.lab.ui;

import play.mvc.Controller;
import models.system.System.PermissionsAllowed;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class UI extends Controller {

	@Authenticated(common.core.Authenticator.class)
	public Result index(String ui) {

		switch (ui) {
			case "drawer" :

				return drawer();
			case "field" :

				return field();
			case "checkbox" :

				return checkbox();
			case "ajaxselect" :

				return ajaxselect();
			case "select" :

				return select();
			case "unlimited" :

				return unlimited();
			case "arrange" :

				return arrange();
			case "draganddrop" :

				return draganddrop();
			case "timeline" :

				return timeline();
			case "notification" :

				return notification();
			case "nav" :

				return nav();
			case "process" :

				return process();
			case "arrow" :

				return arrow();
			case "campaign" :

				return campaign();
			case "group" :

				return group();
			case "infinity" :

				return infinity();
			case "parallax" :

				return parallax();
			case "fixedbar" :

				return fixedbar();
			case "bigtypo" :

				return bigtypo();
			case "block" :

				return block();
			case "window" :

				return window();
			default :

				return notFound(views.html.error.notfound.render(request().method(), request().uri()));
		}
	}

	public static Result drawer() {

		return ok(views.html.lab.ui.drawer.render());
	}

	public static Result field() {

		return ok(views.html.lab.ui.field.render());
	}

	public static Result checkbox() {

		return ok(views.html.lab.ui.checkbox.render());
	}

	public static Result ajaxselect() {

		return ok(views.html.lab.ui.ajaxselect.render());
	}

	public static Result select() {

		return ok(views.html.lab.ui.select.render());
	}

	public static Result unlimited() {

		return ok(views.html.lab.ui.unlimited.render());
	}

	public static Result arrange() {

		return ok(views.html.lab.ui.arrange.render());
	}

	public static Result draganddrop() {

		return ok(views.html.lab.ui.draganddrop.render());
	}

	private static Result timeline() {

		return ok(views.html.lab.ui.timeline.render());
	}

	private static Result notification() {

		return ok(views.html.lab.ui.notification.render());
	}

	private static Result nav() {

		return ok(views.html.lab.ui.nav.render());
	}

	private static Result process() {

		return ok(views.html.lab.ui.process.render());
	}

	private static Result arrow() {

		return ok(views.html.lab.ui.arrow.render());
	}

	private static Result campaign() {

		return ok(views.html.lab.ui.campaign.render());
	}

	private static Result group() {

		return ok(views.html.lab.ui.group.render());
	}

	private static Result infinity() {

		return ok(views.html.lab.ui.infinity.render());
	}

	private static Result parallax() {

		return ok(views.html.lab.ui.parallax.render());
	}

	private static Result fixedbar() {

		return ok(views.html.lab.ui.fixedbar.render());
	}

	private static Result bigtypo() {

		return ok(views.html.lab.ui.bigtypo.render());
	}

	private static Result block() {

		return ok(views.html.lab.ui.block.render());
	}

	private static Result window() {

		return ok(views.html.lab.ui.window.render());
	}
}
