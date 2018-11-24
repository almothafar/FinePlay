package controllers.home;

import javax.inject.Inject;

import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Result;
import javax.annotation.Nonnull;
import play.i18n.Messages;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

@PermissionsAllowed(value = { Permission.READ })
public class Home extends Controller {

	@Inject
	private MessagesApi messagesApi;

	@Authenticated(common.core.Authenticator.class)
	public Result index(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return ok(views.html.home.intro.render(request, lang, messages));
	}

	@Authenticated(common.core.Authenticator.class)
	public Result video(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return ok(views.html.home.video.render(request, lang, messages));
	}
}
