package controllers.resetuser;

import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.Root;
import javax.security.auth.login.AccountException;

import com.typesafe.config.Config;

import common.data.validation.groups.Read;
import common.data.validation.groups.Update;
import common.system.MessageKeys;
import controllers.user.UserService;
import models.resetuser.ResetFormContent;
import models.resetuser.ResetUserDao;
import models.resetuser.ResetUser_;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.api.PlayException;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;

public class ResetUser extends Controller {

	private static long ONE_DAY = Duration.ofDays(1).toDays();

	public static final String CID_LOGO = "logo";

	@Inject
	private MessagesApi messages;

	@Inject
	private JPAApi jpaApi;

	@Inject
	private Config config;

	@Inject
	private FormFactory formFactory;

	@Inject
	private MailerClient mailerClient;

	@Inject
	private UserService userService;

	@Inject
	private ResetUserDao resetUserDao;

	public Result index() {

		final ResetFormContent resetFormContent = new ResetFormContent();
		final Form<ResetFormContent> resetForm = formFactory.form(ResetFormContent.class, Read.class).fill(resetFormContent);

		return ok(views.html.resetuser.reset.render(resetForm));
	}

	@Transactional()
	@RequireCSRFCheck
	public Result apply() {

		final Form<ResetFormContent> resetForm = formFactory.form(ResetFormContent.class, Read.class).bindFromRequest();
		if (!resetForm.hasErrors()) {

			final ResetFormContent resetFormContent = resetForm.get();

			final String userId = resetFormContent.getUserId();

			final models.resetuser.ResetUser resetUser;
			try {

				if (!userService.isExist(jpaApi.em(), userId)) {

					resetFormContent.setUserId(null);
					throw new AccountException(messages.get(lang(), MessageKeys.SYSTEM_ERROR_USERID_NOTEXIST));
				}

				resetUser = new models.resetuser.ResetUser();
				resetUser.setUserId(userId);
				resetUser.setCode();
				resetUser.setExpireDateTime(LocalDateTime.now().plusDays(ONE_DAY));

				try {

					resetUserDao.create(jpaApi.em(), resetUser);
				} catch (final EntityExistsException e) {

					throw new RuntimeException(e);
				}
			} catch (final AccountException e) {

				final Form<ResetFormContent> failureResetForm = formFactory.form(ResetFormContent.class)//
						.fill(resetFormContent)//
						.withGlobalError(e.getLocalizedMessage());
				return failureReset(failureResetForm);
			}

			sendResetEmail(resetUser);
			return ok(views.html.resetuser.request.complete.render(resetForm));
		} else {

			return failureReset(resetForm);
		}
	}

	private Result failureReset(final Form<ResetFormContent> resetForm) {

		return badRequest(views.html.resetuser.reset.render(resetForm));
	}

	private String createChangeURL(final String code) {

		return createSystemURL() + controllers.resetuser.routes.ResetUser.owner(code).url();
	}

	private void sendResetEmail(final models.resetuser.ResetUser resetUser) {

		final String changeURL = createChangeURL(resetUser.getCode());

		final Email email = new Email()//
				.setSubject("[" + messages.get(lang(), MessageKeys.SYSTEM_NAME) + "] " + messages.get(lang(), MessageKeys.RESETUSER_REQUEST_MAIL_SUBJECT))//
				.setFrom("Mister FROM <from@email.com>")//
				.addTo("Miss TO <" + resetUser.getUserId() + ">")//
				.addAttachment("image.jpg", Paths.get("public", "images", lang().code(), "logo.png").toFile(), CID_LOGO)//
				.setBodyHtml(createRequestHtmlMailBody(resetUser, changeURL, lang()).body().trim());

		mailerClient.send(email);
	}

	private Html createRequestHtmlMailBody(final models.resetuser.ResetUser user, final String changeURL, final Lang lang) {

		switch (lang.code()) {
		case "ja-JP":

			return views.html.resetuser.request.mail.body.render(user, changeURL, createInquiryURL());
		default:

			return views.html.resetuser.request.mail.body.render(user, changeURL, createInquiryURL());
		}
	}

	@PermissionsAllowed(value = { Permission.MANAGE })
	public Result requestHtmlMail(final String langString) {

		final models.resetuser.ResetUser dummyResetUser = new models.resetuser.ResetUser();
		dummyResetUser.setUserId("client@company.com");
		dummyResetUser.setCode();
		return ok(createRequestHtmlMailBody(dummyResetUser, createChangeURL(dummyResetUser.getCode()), Lang.forCode(langString)));
	}

	@Transactional()
	public Result owner(final String code) {

		models.resetuser.ResetUser resetUser;
		try {

			resetUser = resetUserDao.read(jpaApi.em(), (builder, query) -> {

				final Root<models.resetuser.ResetUser> root = query.from(models.resetuser.ResetUser.class);
				query.where(builder.and(builder.equal(root.get(ResetUser_.code), code), builder.greaterThanOrEqualTo(root.get(ResetUser_.expireDateTime), LocalDateTime.now())));
			});
		} catch (final NoResultException e) {

			throw new PlayException("", "", e);
		} catch (final NonUniqueResultException e) {

			throw new RuntimeException(e);
		}

		final ResetFormContent resetFormContent = new ResetFormContent();
		resetFormContent.setUserId(resetUser.getUserId());
		final Form<ResetFormContent> resetForm = formFactory.form(ResetFormContent.class).fill(resetFormContent);

		return ok(views.html.resetuser.change.change.render(resetForm));
	}

	@Transactional()
	public Result change() {

		final Result result = jpaApi.withTransaction(manager -> {

			final Form<ResetFormContent> resetForm = formFactory.form(ResetFormContent.class, Update.class).bindFromRequest();
			if (!resetForm.hasErrors()) {

				final ResetFormContent resetFormContent = resetForm.get();

				final String userId = resetFormContent.getUserId();
				final String password = resetFormContent.getPassword();
				final String rePassword = resetFormContent.getRePassword();

				final models.resetuser.ResetUser resetUser;
				final models.user.User user;
				try {

					if (!password.equals(rePassword)) {

						resetFormContent.setPassword(null);
						resetFormContent.setRePassword(null);
						throw new AccountException(messages.get(lang(), MessageKeys.SYSTEM_ERROR_PASSWORD_NOTEQUAL));
					}

					try {

						user = userService.read(jpaApi.em(), userId);
					} catch (final AccountException e) {

						throw e;
					}

					try {

						resetUser = resetUserDao.read(jpaApi.em(), (builder, query) -> {

							final Root<models.resetuser.ResetUser> root = query.from(models.resetuser.ResetUser.class);
							query.where(builder.and(builder.equal(root.get(ResetUser_.userId), userId)));
						});
					} catch (final NoResultException e) {

						throw new RuntimeException(e);
					} catch (final NonUniqueResultException e) {

						throw new RuntimeException(e);
					}

					user.setPassword(password);

					userService.update(jpaApi.em(), user);

					resetUserDao.delete(jpaApi.em(), resetUser);
				} catch (final AccountException e) {

					final Form<ResetFormContent> failureResetForm = formFactory.form(ResetFormContent.class).fill(resetFormContent);
					failureResetForm.withGlobalError(e.getLocalizedMessage());
					return failureChange(failureResetForm);
				}

				sendChangeEmail(user);
				return ok(views.html.resetuser.change.complete.render(user));
			} else {

				return failureChange(resetForm);
			}
		});

		return result;
	}

	private Result failureChange(final Form<ResetFormContent> resetForm) {

		return badRequest(views.html.resetuser.change.change.render(resetForm));
	}

	private String createSystemURL() {

		final String host = config.hasPath("system.url") ? config.getString("system.url") : "http://localhost:9000";
		return host;
	}

	private void sendChangeEmail(final models.user.User user) {

		final Email email = new Email()//
				.setSubject("[" + messages.get(lang(), MessageKeys.SYSTEM_NAME) + "] " + messages.get(lang(), MessageKeys.RESETUSER_CHANGE_MAIL_SUBJECT))//
				.setFrom("Mister FROM <from@email.com>")//
				.addTo("Miss TO <" + user.getUserId() + ">")//
				.addAttachment("image.jpg", Paths.get("public", "images", lang().code(), "logo.png").toFile(), CID_LOGO)//
				.setBodyHtml(createChangeHtmlMailBody(user, createSystemURL(), lang()).body().trim());

		mailerClient.send(email);
	}

	private Html createChangeHtmlMailBody(final models.user.User user, final String systemURL, final Lang lang) {

		switch (lang.code()) {
		case "ja-JP":

			return views.html.resetuser.change.mail.body.render(user, systemURL, createInquiryURL());
		default:

			return views.html.resetuser.change.mail.body.render(user, systemURL, createInquiryURL());
		}
	}

	@PermissionsAllowed(value = { Permission.MANAGE })
	public Result changeHtmlMail(final String langString) {

		final models.user.User dummyUser = new models.user.User();
		dummyUser.setUserId("client@company.com");
		return ok(createChangeHtmlMailBody(dummyUser, createSystemURL(), Lang.forCode(langString)));
	}

	private String createInquiryURL() {

		return createSystemURL() + controllers.inquiry.routes.Inquiry.index().url();
	}
}
