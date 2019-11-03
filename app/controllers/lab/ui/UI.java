package controllers.lab.ui;

import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import play.i18n.Messages;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class UI extends Controller {

	@Inject
	private MessagesApi messagesApi;

	@Authenticated(common.core.Authenticator.class)
	public Result index(@Nonnull final Request request, String ui) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (ui) {
		case "drawer":

			return drawer(request, lang, messages);
		case "field":

			return field(request, lang, messages);
		case "checkbox":

			return checkbox(request, lang, messages);
		case "ajaxselect":

			return ajaxselect(request, lang, messages);
		case "select":

			return select(request, lang, messages);
		case "unlimited":

			return unlimited(request, lang, messages);
		case "draganddrop":

			return draganddrop(request, lang, messages);
		case "timeline":

			return timeline(request, lang, messages);
		case "balloon":

			return balloon(request, lang, messages);
		case "alert":

			return alert(request, lang, messages);
		case "notification":

			return notification(request, lang, messages);
		case "nav":

			return nav(request, lang, messages);
		case "process":

			return process(request, lang, messages);
		case "arrow":

			return arrow(request, lang, messages);
		case "summary":

			return summary(request, lang, messages);
		case "campaign":

			return campaign(request, lang, messages);
		case "detailimage":

			return detailimage(request, lang, messages);
		case "group":

			return group(request, lang, messages);
		case "infinity":

			return infinity(request, lang, messages);
		case "parallax":

			return parallax(request, lang, messages);
		case "fixedbar":

			return fixedbar(request, lang, messages);
		case "bigtypo":

			return bigtypo(request, lang, messages);
		case "block":

			return block(request, lang, messages);
		case "pay":

			return pay(request, lang, messages);
		case "securekeyboard":

			return securekeyboard(request, lang, messages);
		case "loadbutton":

			return loadbutton(request, lang, messages);
		case "glance":

			return glance(request, lang, messages);
		case "reverse":

			return reverse(request, lang, messages);
		case "window":

			return window(request, lang, messages);
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	public static Result drawer(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.drawer.render(request, lang, messages));
	}

	public static Result field(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.field.render(request, lang, messages));
	}

	public static Result checkbox(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.checkbox.render(request, lang, messages));
	}

	public static Result ajaxselect(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.ajaxselect.render(request, lang, messages));
	}

	public static Result select(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.select.render(request, lang, messages));
	}

	public static Result unlimited(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.unlimited.render(request, lang, messages));
	}

	public static Result draganddrop(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.draganddrop.render(request, lang, messages));
	}

	private static Result timeline(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.timeline.render(request, lang, messages));
	}

	private static Result balloon(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.balloon.render(request, lang, messages));
	}

	private static Result alert(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.alert.render(request, lang, messages));
	}

	private static Result notification(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.notification.render(request, lang, messages));
	}

	private static Result nav(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.nav.render(request, lang, messages));
	}

	private static Result process(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.process.render(request, lang, messages));
	}

	private static Result arrow(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.arrow.render(request, lang, messages));
	}

	private static Result summary(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.summary.render(request, lang, messages));
	}

	private static Result campaign(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.campaign.render(request, lang, messages));
	}

	private static Result detailimage(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.detailimage.render(request, lang, messages));
	}

	private static Result group(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.group.render(request, lang, messages));
	}

	private static Result infinity(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.infinity.render(request, lang, messages));
	}

	private static Result parallax(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.parallax.render(request, lang, messages));
	}

	private static Result fixedbar(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.fixedbar.render(request, lang, messages));
	}

	private static Result bigtypo(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.bigtypo.render(request, lang, messages));
	}

	private static Result block(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.block.render(request, lang, messages));
	}

	private static Result pay(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.pay.render(request, lang, messages));
	}

	private static Result securekeyboard(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.securekeyboard.render(request, lang, messages));
	}

	private static Result loadbutton(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.loadbutton.render(request, lang, messages));
	}

	private static Result glance(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.glance.render(request, lang, messages));
	}

	private static Result reverse(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.reverse.render(request, lang, messages));
	}

	private static Result window(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ui.window.render(request, lang, messages));
	}
}
