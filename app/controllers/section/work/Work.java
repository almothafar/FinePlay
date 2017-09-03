package controllers.section.work;

import java.util.Map;

import javax.inject.Inject;

import common.system.MessageKeys;
import play.mvc.Controller;
import models.system.System.PermissionsAllowed;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.MessagesApi;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Work extends Controller {

	// Task

	@Inject
	private MessagesApi messages;

	@Authenticated(common.core.Authenticator.class)
	public Result task() {

		return ok(views.html.section.work.task.render(messages.get(lang(), MessageKeys.WELCOME)));
	}

	// Task2

	@Inject
	private FormFactory formFactory;

	@Authenticated(common.core.Authenticator.class)
	public Result task2() {

		return ok(views.html.section.work.task2.render(""));
	}

	@Authenticated(common.core.Authenticator.class)
	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	@RequireCSRFCheck
	public Result task2Request() {

		final DynamicForm targetForm = formFactory.form().bindFromRequest();

		final Map<String, String> data = targetForm.rawData();
		return ok(views.html.section.work.task2.render(data.getOrDefault("name", "")));
	}

	// Task3

	@Authenticated(common.core.Authenticator.class)
	public Result task3() {

		return ok(views.html.section.work.task3.render());
	}
}
