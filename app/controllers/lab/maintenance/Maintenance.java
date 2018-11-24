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
import javax.annotation.Nonnull;
import play.i18n.Messages;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Maintenance extends Controller {

	@Inject
	private MessagesApi messagesApi;

	@Inject
	private FormFactory formFactory;

	@Authenticated(common.core.Authenticator.class)
	public Result master(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return ok(views.html.lab.maintenance.master.render(request, lang, messages));
	}

	@Authenticated(common.core.Authenticator.class)
	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	@RequireCSRFCheck
	public Result detail(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final DynamicForm targetForm = formFactory.form().bindFromRequest(request);

		final Map<String, String> data = targetForm.rawData();
		return ok(views.html.lab.maintenance.detail.render(data.get("target"), data.get("task"), request, lang, messages));
	}
}
