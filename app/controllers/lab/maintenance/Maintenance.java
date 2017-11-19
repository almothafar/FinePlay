package controllers.lab.maintenance;

import java.util.Map;

import javax.inject.Inject;

import models.system.System.PermissionsAllowed;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Maintenance extends Controller {

	@Inject
	private FormFactory formFactory;

	@Authenticated(common.core.Authenticator.class)
	public Result master() {

		return ok(views.html.lab.maintenance.master.render());
	}

	@Authenticated(common.core.Authenticator.class)
	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	@RequireCSRFCheck
	public Result detail() {

		final DynamicForm targetForm = formFactory.form().bindFromRequest();

		final Map<String, String> data = targetForm.rawData();
		return ok(views.html.lab.maintenance.detail.render(data.get("target"), data.get("task")));
	}
}
