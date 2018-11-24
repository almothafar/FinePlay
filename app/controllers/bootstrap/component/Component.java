package controllers.bootstrap.component;

import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import play.i18n.Messages;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Http.Request;

@PermissionsAllowed
public class Component extends Controller {

	@Inject
	private MessagesApi messagesApi;

	public Result index(@Nonnull final Request request, String component) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (component) {
		case "button":

			return button(request, lang, messages);
		case "linkbutton":

			return linkbutton(request, lang, messages);
		case "badge":

			return badge(request, lang, messages);
		case "dropdown":

			return dropdown(request, lang, messages);
		case "buttongroup":

			return buttongroup(request, lang, messages);
		case "listgroup":

			return listgroup(request, lang, messages);
		case "nav":

			return nav(request, lang, messages);
		case "alerts":

			return alerts(request, lang, messages);
		case "progress":

			return progress(request, lang, messages);
		case "pagination":

			return pagination(request, lang, messages);
		case "image":

			return image(request, lang, messages);
		case "carousel":

			return carousel(request, lang, messages);
		case "table":

			return table(request, lang, messages);
		case "card":

			return card(request, lang, messages);
		case "modal":

			return modal(request, lang, messages);
		case "collapse":

			return collapse(request, lang, messages);
		case "form":

			return form(request, lang, messages);
		default:

			return etc(request, lang, messages);
		}
	}

	public static Result button(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.component.button.render(request, lang, messages));
	}

	public static Result linkbutton(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.component.linkbutton.render(request, lang, messages));
	}

	public static Result badge(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.component.badge.render(request, lang, messages));
	}

	public static Result dropdown(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.component.dropdown.render(request, lang, messages));
	}

	public static Result buttongroup(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.component.buttongroup.render(request, lang, messages));
	}

	public static Result listgroup(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.component.listgroup.render(request, lang, messages));
	}

	public static Result nav(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.component.nav.render(request, lang, messages));
	}

	public static Result alerts(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.component.alerts.render(request, lang, messages));
	}

	public static Result progress(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.component.progress.render(request, lang, messages));
	}

	public static Result pagination(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.component.pagination.render(request, lang, messages));
	}

	public static Result image(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.component.image.render(request, lang, messages));
	}

	public static Result carousel(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.component.carousel.render(request, lang, messages));
	}

	public static Result table(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.component.table.render(request, lang, messages));
	}

	public static Result card(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.component.card.render(request, lang, messages));
	}

	public static Result modal(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.component.modal.render(request, lang, messages));
	}

	public static Result collapse(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.component.collapse.render(request, lang, messages));
	}

	public static Result form(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.component.form.render(request, lang, messages));
	}

	public static Result etc(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.bootstrap.component.etc.render(request, lang, messages));
	}
}
