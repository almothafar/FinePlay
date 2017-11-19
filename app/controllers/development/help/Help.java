package controllers.development.help;

import common.utils.Helps;
import common.utils.Requests;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.i18n.Lang;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.twirl.api.Html;

public class Help extends Controller {

	@PermissionsAllowed(value = { Permission.READ })
	@Authenticated(common.core.Authenticator.class)
	public Result typography() {

		return ok(views.html.development.help.typography.render());
	}

	@PermissionsAllowed(value = { Permission.READ })
	@Authenticated(common.core.Authenticator.class)
	public Result code() {

		return ok(views.html.development.help.code.render());
	}

	@PermissionsAllowed(value = { Permission.READ })
	@Authenticated(common.core.Authenticator.class)
	public Result images() {

		return ok(views.html.development.help.images.render());
	}

	@PermissionsAllowed(value = { Permission.READ })
	@Authenticated(common.core.Authenticator.class)
	public Result tables() {

		return ok(views.html.development.help.tables.render());
	}

	@PermissionsAllowed(value = { Permission.READ })
	@Authenticated(common.core.Authenticator.class)
	public Result figures() {

		return ok(views.html.development.help.figures.render());
	}

	public Result help() {

		if (Requests.isAjax(request())) {

			return ok(createHelpContent(lang()));
		} else {

			return ok(createHelp(lang()));
		}
	}

	private Html createHelpContent(final Lang lang) {

		final Html html = createHelp(lang);

		return Helps.prepare(html);
	}

	private Html createHelp(final Lang lang) {

		final Html html;
		switch (lang.code()) {
		case "ja-JP":

			html = views.html.development.help.help.help_ja_JP.render();
			break;
		default:

			html = views.html.development.help.help.help_en_US.render();
			break;
		}

		return html;
	}
}
