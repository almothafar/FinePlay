package controllers.development.help;

import common.utils.Helps;
import common.utils.Requests;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.i18n.Lang;
import play.mvc.Controller;
import play.mvc.Result;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;
import play.twirl.api.Html;

public class Help extends Controller {

	@Inject
	private MessagesApi messagesApi;

	@PermissionsAllowed(value = { Permission.READ })
	@Authenticated(common.core.Authenticator.class)
	public Result typography(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return ok(views.html.development.help.typography.render(request, lang, messages));
	}

	@PermissionsAllowed(value = { Permission.READ })
	@Authenticated(common.core.Authenticator.class)
	public Result code(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return ok(views.html.development.help.code.render(request, lang, messages));
	}

	@PermissionsAllowed(value = { Permission.READ })
	@Authenticated(common.core.Authenticator.class)
	public Result images(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return ok(views.html.development.help.images.render(request, lang, messages));
	}

	@PermissionsAllowed(value = { Permission.READ })
	@Authenticated(common.core.Authenticator.class)
	public Result tables(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return ok(views.html.development.help.tables.render(request, lang, messages));
	}

	@PermissionsAllowed(value = { Permission.READ })
	@Authenticated(common.core.Authenticator.class)
	public Result figures(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return ok(views.html.development.help.figures.render(request, lang, messages));
	}

	public Result help(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		if (Requests.isAjax(request)) {

			return ok(createHelpContent(request, lang, messages));
		} else {

			return ok(createHelp(request, lang, messages));
		}
	}

	private Html createHelpContent(final Request request, final Lang lang, final Messages messages) {

		final Html html = createHelp(request, lang, messages);

		return Helps.prepare(html);
	}

	private Html createHelp(final Request request, final Lang lang, final Messages messages) {

		final Html html;
		switch (lang.code()) {
		case "ja-JP":

			html = views.html.development.help.help.help_ja_JP.render(request, lang, messages);
			break;
		default:

			html = views.html.development.help.help.help_en_US.render(request, lang, messages);
			break;
		}

		return html;
	}
}
