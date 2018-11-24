package controllers.setting.user;

import java.lang.invoke.MethodHandles;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.security.auth.login.AccountException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controllers.user.UserService;
import models.setting.user.EditFormContent;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import models.user.User.Theme;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Result;
import javax.annotation.Nonnull;
import play.i18n.Messages;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

@PermissionsAllowed(value = { Permission.READ, Permission.WRITE })
public class User extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private MessagesApi messagesApi;

	@Inject
	private JPAApi jpaApi;

	@Inject
	private FormFactory formFactory;

	@Inject
	private UserService userService;

	@Authenticated(common.core.Authenticator.class)
	public Result index(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final EditFormContent editFormContent = new EditFormContent();
		final String locale = lang.code();
		final String zoneId = request.session().getOptional(models.user.User_.ZONE_ID).get();
		final String theme = request.session().getOptional(models.user.User_.THEME).get();

		editFormContent.setLocale(locale);
		editFormContent.setZoneId(zoneId);
		editFormContent.setTheme(theme);

		final Form<EditFormContent> editForm = formFactory.form(EditFormContent.class).fill(editFormContent);

		return ok(views.html.setting.user.general.render(editForm, request, lang, messages));
	}

	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result update(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return jpaApi.withTransaction(manager -> {

			final Form<EditFormContent> editForm = formFactory.form(EditFormContent.class).bindFromRequest(request);
			if (!editForm.hasErrors()) {

				final Locale locale = Lang.forCode(lang.code()).toLocale();
				final ZoneId zoneId = ZoneId.of(request.session().getOptional(models.user.User_.ZONE_ID).get());
				final Theme theme = Theme.valueOf(request.session().getOptional(models.user.User_.THEME).get());

				final EditFormContent editFormContent = editForm.get();
				final Locale updatedLocale = Lang.forCode(editFormContent.getLocale()).toLocale();
				final ZoneId updatedZoneId = ZoneId.of(editFormContent.getZoneId());
				final Theme updatedTheme = Theme.valueOf(editFormContent.getTheme());

				boolean isUpdated = false;
				if (!locale.equals(updatedLocale)) {

					isUpdated = true;
				}
				if (!zoneId.equals(updatedZoneId)) {

					isUpdated = true;
				}
				if (!theme.equals(updatedTheme)) {

					isUpdated = true;
				}

				if (isUpdated) {

					final models.user.User user;
					try {

						try {

							user = userService.read(manager, messages, request.session().getOptional(models.user.User_.USER_ID).get());
						} catch (final AccountException e) {

							throw e;
						}

						user.setLocale(updatedLocale);
						user.setZoneId(updatedZoneId);
						user.setTheme(updatedTheme);

						userService.update(manager, messages, user);
					} catch (final AccountException e) {

						final Form<EditFormContent> failureEditForm = formFactory.form(EditFormContent.class)//
								.fill(editFormContent)//
								.withGlobalError(e.getLocalizedMessage());
						return failureEdit(failureEditForm, request, lang, messages);
					}
				}

				return redirect(controllers.setting.user.routes.User.index())//
						.withLang(new Lang(updatedLocale), messagesApi)//
						.addingToSession(request, Map.of(//
								models.user.User_.ZONE_ID, updatedZoneId.getId(), //
								models.user.User_.THEME, updatedTheme.name()//
				));
			} else {

				return failureEdit(editForm, request, lang, messages);
			}
		});
	}

	private Result failureEdit(final Form<EditFormContent> editForm, final Request request, final Lang lang, final Messages messages) {

		return badRequest(views.html.setting.user.general.render(editForm, request, lang, messages));
	}
}
