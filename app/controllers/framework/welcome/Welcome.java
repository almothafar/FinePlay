package controllers.framework.welcome;

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
public class Welcome extends Controller {

	@Inject
	private MessagesApi messagesApi;

	@Authenticated(common.core.Authenticator.class)
	public Result index(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return ok(views.html.framework.welcome.index.render(request, lang, messages));
	}
}
