package controllers.section.work;

import java.util.Map;

import javax.inject.Inject;

import common.system.MessageKeys;
import models.system.System.PermissionsAllowed;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import javax.annotation.Nonnull;
import play.i18n.Messages;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Work extends Controller {

	// Task

	@Inject
	private MessagesApi messagesApi;

	@Authenticated(common.core.Authenticator.class)
	public Result task(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return ok(views.html.section.work.task.render(messages.at(MessageKeys.WELCOME), request, lang, messages));
	}

	// Task2

	@Inject
	private FormFactory formFactory;

	@Authenticated(common.core.Authenticator.class)
	public Result task2(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return ok(views.html.section.work.task2.render("", request, lang, messages));
	}

	@Authenticated(common.core.Authenticator.class)
	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	@RequireCSRFCheck
	public Result task2Request(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final DynamicForm targetForm = formFactory.form().bindFromRequest(request);

		final Map<String, String> data = targetForm.rawData();
		return ok(views.html.section.work.task2.render(data.getOrDefault("name", ""), request, lang, messages));
	}

	// Task3

	@Authenticated(common.core.Authenticator.class)
	public Result task3(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return ok(views.html.section.work.task3.render(request, lang, messages));
	}
}
