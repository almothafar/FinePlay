package controllers.system;

import play.mvc.Controller;
import play.mvc.Result;
import play.routing.JavaScriptReverseRouter;

public class System extends Controller {

	public Result routes() {

		return ok(JavaScriptReverseRouter.create("Routes", //
				apis.system.routes.javascript.Logger.log(), //
				apis.character.routes.javascript.Character.character(), //
				apis.company.routes.javascript.Company.companies(), //
				apis.development.http.routes.javascript.Http.ajaxdata(), //
				apis.transrator.routes.javascript.Transrator.translate(), //
				controllers.home.routes.javascript.Home.index(), //
				controllers.user.routes.javascript.User.index(), //
				controllers.setting.user.routes.javascript.ChangeUser.index(), //
				controllers.manage.user.routes.javascript.Read.index(), //
				controllers.manage.company.routes.javascript.Read.index(), //
				controllers.manage.company.organization.list.routes.javascript.Read.index(), //
				controllers.manage.company.organization.tree.routes.javascript.Read.index(), //
				controllers.framework.defaultpage.routes.javascript.Defaultpage.page(), //
				controllers.framework.application.routes.javascript.Application.synccache(), //
				controllers.framework.application.routes.javascript.Application.asynccache(), //
				controllers.lab.application.routes.javascript.WebSocket.connect(), //
				controllers.lab.maintenance.routes.javascript.Maintenance.master(), //
				controllers.lab.maintenance.routes.javascript.Maintenance.detail())//
		).as("text/javascript");
	}
}
