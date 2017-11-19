package controllers.setting.user;

import java.time.ZoneId;
import java.util.Locale;

import javax.inject.Inject;
import javax.security.auth.login.AccountException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.utils.Locales;
import controllers.user.UserService;
import models.setting.user.EditFormContent;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import models.user.User.Theme;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed(value = { Permission.READ, Permission.WRITE })
public class User extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

	@Inject
	private MessagesApi messages;

	@Inject
	private JPAApi jpaApi;

	@Inject
	private FormFactory formFactory;

	@Inject
	private UserService userService;

	@Authenticated(common.core.Authenticator.class)
	public Result index() {

		final EditFormContent editFormContent = new EditFormContent();
		final String locale = lang().code();
		// final String locale = session(models.user.User.LOCALE);
		// final String locale =
		// request().cookie(messages.langCookieName()).value();
		final String zoneId = session(models.user.User.ZONEID);
		final String theme = session(models.user.User.THEME);

		editFormContent.setLocale(locale);
		editFormContent.setZoneId(zoneId);
		editFormContent.setTheme(theme);

		final Form<EditFormContent> editForm = formFactory.form(EditFormContent.class).fill(editFormContent);

		return ok(views.html.setting.user.general.render(editForm));
	}

	@Authenticated(common.core.Authenticator.class)
	@Transactional()
	@RequireCSRFCheck
	public Result update() {

		final Form<EditFormContent> editForm = formFactory.form(EditFormContent.class).bindFromRequest();
		if (!editForm.hasErrors()) {

			final Locale locale = Lang.forCode(lang().code()).toLocale();
			final ZoneId zoneId = ZoneId.of(session(models.user.User.ZONEID));
			final Theme theme = Theme.valueOf(session(models.user.User.THEME));

			final EditFormContent editFormContent = editForm.get();
			final Locale updatedLocale = Lang.forCode(editFormContent.getLocale()).toLocale();
			final ZoneId updatedZoneId = ZoneId.of(editFormContent.getZoneId());
			final Theme updatedTheme = Theme.valueOf(editFormContent.getTheme());

			boolean isUpdated = false;
			if (!locale.equals(updatedLocale)) {

				final Lang updatedLang = Locales.toLang(updatedLocale);
				changeLang(updatedLang);

				isUpdated = true;
			}
			if (!zoneId.equals(updatedZoneId)) {

				session(models.user.User.ZONEID, updatedZoneId.getId());
				isUpdated = true;
			}
			if (!theme.equals(updatedTheme)) {

				session(models.user.User.THEME, updatedTheme.name());
				isUpdated = true;
			}

			if (isUpdated) {

				final models.user.User user;
				try {

					try {

						user = userService.read(jpaApi.em(), session(models.user.User.USERID));
					} catch (final AccountException e) {

						throw e;
					}

					user.setLocale(updatedLocale);
					user.setZoneId(updatedZoneId);
					user.setTheme(updatedTheme);

					userService.update(jpaApi.em(), user);
				} catch (final AccountException e) {

					final Form<EditFormContent> failureEditForm = formFactory.form(EditFormContent.class)//
							.fill(editFormContent)//
							.withGlobalError(e.getLocalizedMessage());
					return failureEdit(failureEditForm);
				}
			}

			return ok(views.html.setting.user.general.render(editForm));
		} else {

			return failureEdit(editForm);
		}
	}

	private Result failureEdit(final Form<EditFormContent> editForm) {

		return badRequest(views.html.setting.user.general.render(editForm));
	}
}
