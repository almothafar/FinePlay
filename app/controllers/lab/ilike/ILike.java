package controllers.lab.ilike;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ILike extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private MessagesApi messagesApi;

	@Authenticated(common.core.Authenticator.class)
	public Result index(@Nonnull final Request request, String state, Boolean detail) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (state) {
		case "list":

			return list(detail, request, lang, messages);
		case "arrange":

			return arrange(detail, request, lang, messages);
		case "component":

			return component(detail, request, lang, messages);
		case "autumnboard":

			return autumnboard(detail, request, lang, messages);
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	private static Result list(Boolean detail, final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ilike.list.render(detail, request, lang, messages));
	}

	public static Result arrange(Boolean detail, final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ilike.arrange.render(detail, request, lang, messages));
	}

	public static Result component(Boolean detail, final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ilike.component.render(detail, request, lang, messages));
	}

	private static Result autumnboard(Boolean detail, final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.ilike.autumnboard.render(detail, request, lang, messages));
	}
}
