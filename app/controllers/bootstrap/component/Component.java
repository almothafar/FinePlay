package controllers.bootstrap.component;

import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;

@PermissionsAllowed
public class Component extends Controller {

	public Result index(String component) {

		switch (component) {
		case "button":

			return button();
		case "linkbutton":

			return linkbutton();
		case "badge":

			return badge();
		case "dropdown":

			return dropdown();
		case "buttongroup":

			return buttongroup();
		case "listgroup":

			return listgroup();
		case "nav":

			return nav();
		case "alerts":

			return alerts();
		case "progress":

			return progress();
		case "pagination":

			return pagination();
		case "image":

			return image();
		case "carousel":

			return carousel();
		case "table":

			return table();
		case "card":

			return card();
		default:

			return etc();
		}
	}

	public static Result button() {

		return ok(views.html.bootstrap.component.button.render());
	}

	public static Result linkbutton() {

		return ok(views.html.bootstrap.component.linkbutton.render());
	}

	public static Result badge() {

		return ok(views.html.bootstrap.component.badge.render());
	}

	public static Result dropdown() {

		return ok(views.html.bootstrap.component.dropdown.render());
	}

	public static Result buttongroup() {

		return ok(views.html.bootstrap.component.buttongroup.render());
	}

	public static Result listgroup() {

		return ok(views.html.bootstrap.component.listgroup.render());
	}

	public static Result nav() {

		return ok(views.html.bootstrap.component.nav.render());
	}

	public static Result alerts() {

		return ok(views.html.bootstrap.component.alerts.render());
	}

	public static Result progress() {

		return ok(views.html.bootstrap.component.progress.render());
	}

	public static Result pagination() {

		return ok(views.html.bootstrap.component.pagination.render());
	}

	public static Result image() {

		return ok(views.html.bootstrap.component.image.render());
	}

	public static Result carousel() {

		return ok(views.html.bootstrap.component.carousel.render());
	}

	public static Result table() {

		return ok(views.html.bootstrap.component.table.render());
	}

	public static Result card() {

		return ok(views.html.bootstrap.component.card.render());
	}

	public static Result etc() {

		return ok(views.html.bootstrap.component.etc.render());
	}
}
