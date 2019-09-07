package controllers.setting.user;

import java.lang.invoke.MethodHandles;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.Root;
import javax.security.auth.login.AccountException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import common.system.MessageKeys;
import controllers.user.UserService;
import models.setting.user.changeuser.ChangeFormContent;
import models.setting.user.changeuser.ChangeUserDao;
import models.setting.user.changeuser.ChangeUser_;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.api.PlayException;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Lang;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.twirl.api.Html;

@PermissionsAllowed(value = { Permission.READ, Permission.WRITE })
public class ChangeUser extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static long ONE_DAY = Duration.ofDays(1).toDays();

	public static final String CID_LOGO = "logo";

	@Inject
	private MessagesApi messagesApi;

	@Inject
	private JPAApi jpaApi;

	@Inject
	private Config config;

	@Inject
	private FormFactory formFactory;

	@Inject
	private MailerClient mailer;

	@Inject
	private UserService userService;

	@Inject
	private ChangeUserDao changeUserDao;

	@Authenticated(common.core.Authenticator.class)
	public Result index(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final ChangeFormContent changeFormContent = new ChangeFormContent();
		final Form<ChangeFormContent> changeForm = formFactory.form(ChangeFormContent.class).fill(changeFormContent);

		return ok(views.html.setting.user.changeuser.change.render(changeForm, request, lang, messages));
	}

	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result apply(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final Result result = jpaApi.withTransaction(manager -> {

			final Form<ChangeFormContent> changeForm = formFactory.form(ChangeFormContent.class).bindFromRequest(request);
			if (!changeForm.hasErrors()) {

				final ChangeFormContent changeFormContent = changeForm.get();

				final String newUserId = changeFormContent.getNewUserId();

				final models.setting.user.changeuser.ChangeUser changeUser;
				try {

					if (userService.isExist(manager, messages, newUserId)) {

						changeFormContent.setNewUserId(null);
						throw new AccountException(messages.at(MessageKeys.SYSTEM_ERROR_USERID_EXIST));
					}

					changeUser = new models.setting.user.changeuser.ChangeUser();
					changeUser.setUserId(request.session().get(models.user.User_.USER_ID).get());
					changeUser.setNewUserId(newUserId);
					changeUser.setCode();
					changeUser.setExpireDateTime(LocalDateTime.now().plusDays(ONE_DAY));

					try {

						changeUserDao.create(manager, changeUser);
					} catch (final EntityExistsException e) {

						throw new RuntimeException(e);
					}
				} catch (final AccountException e) {

					final Form<ChangeFormContent> failureChangeForm = formFactory.form(ChangeFormContent.class).fill(changeFormContent);
					failureChangeForm.withGlobalError(e.getLocalizedMessage());
					return failureApply(failureChangeForm, request, lang, messages);
				}

				sendReserveEmail(changeUser, request, lang, messages);
				return ok(views.html.setting.user.changeuser.reserve.complete.render(changeForm, request, lang, messages));
			} else {

				return failureApply(changeForm, request, lang, messages);
			}
		});

		return result;
	}

	private Result failureApply(final Form<ChangeFormContent> applyForm, final Request request, final Lang lang, final Messages messages) {

		return badRequest(views.html.setting.user.changeuser.change.render(applyForm, request, lang, messages));
	}

	private String createDecisionURL(final String code) {

		return createSystemURL() + controllers.setting.user.routes.ChangeUser.decision(code).url();
	}

	private void sendReserveEmail(final models.setting.user.changeuser.ChangeUser changeUser, final Request request, final Lang lang, final Messages messages) {

		final String decisionURL = createDecisionURL(changeUser.getCode());

		final Email email = new Email()//
				.setSubject("[" + messages.at(MessageKeys.SYSTEM_NAME) + "] " + messages.at(MessageKeys.CHANGEUSER_RESERVE_MAIL_SUBJECT))//
				.setFrom("Mister FROM <from@email.com>")//
				.addTo("Miss TO <" + changeUser.getUserId() + ">")//
				.addAttachment("image.jpg", Paths.get("public", "images", lang.code(), "logo.png").toFile(), CID_LOGO)//
				.setBodyHtml(createReserveHtmlMailBody(changeUser, decisionURL, request, lang, messages).body().trim());

		mailer.send(email);
	}

	private Html createReserveHtmlMailBody(final models.setting.user.changeuser.ChangeUser user, final String decisionURL, final Request request, final Lang lang, final Messages messages) {

		switch (lang.code()) {
		case "ja-JP":

			return views.html.setting.user.changeuser.reserve.mail.body.render(user, decisionURL, createInquiryURL(), request, lang, messages);
		default:

			return views.html.setting.user.changeuser.reserve.mail.body.render(user, decisionURL, createInquiryURL(), request, lang, messages);
		}
	}

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	public Result reserveHtmlMail(@Nonnull final Request request, final String langString) {

		final Messages messages = messagesApi.preferred(request);

		final models.setting.user.changeuser.ChangeUser dummyChangeUser = new models.setting.user.changeuser.ChangeUser();
		dummyChangeUser.setUserId("client@company.com");
		dummyChangeUser.setCode();
		return ok(createReserveHtmlMailBody(dummyChangeUser, createDecisionURL(dummyChangeUser.getCode()), request, Lang.forCode(langString), messages));
	}

	@PermissionsAllowed(value = { Permission.READ })
	public Result decision(@Nonnull final Request request, final String code) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final Result result = jpaApi.withTransaction(manager -> {

			models.setting.user.changeuser.ChangeUser changeUser;
			models.user.User user;
			try {

				try {

					changeUser = changeUserDao.read(manager, (builder, query) -> {

						final Root<models.setting.user.changeuser.ChangeUser> root = query.from(models.setting.user.changeuser.ChangeUser.class);
						query.where(builder.and(builder.equal(root.get(ChangeUser_.code), code), builder.greaterThanOrEqualTo(root.get(ChangeUser_.expireDateTime), LocalDateTime.now())));
					});
				} catch (final NoResultException e) {

					throw new PlayException("", "", e);
				} catch (final NonUniqueResultException e) {

					throw new RuntimeException(e);
				}

				user = userService.read(manager, messages, changeUser.getUserId());
				user.setUserId(changeUser.getNewUserId());

				userService.update(manager, messages, user);

				changeUserDao.delete(manager, changeUser);
			} catch (final AccountException e) {

				throw new PlayException("", "", e);
			}

			sendDecisionEmail(user, request, lang, messages);
			return ok(views.html.setting.user.changeuser.decision.complete.render(user, request, lang, messages));
		});

		return result;
	}

	private String createSystemURL() {

		final String host = config.hasPath("system.url") ? config.getString("system.url") : "http://localhost:9000";
		return host;
	}

	private void sendDecisionEmail(final models.user.User user, final Request request, final Lang lang, final Messages messages) {

		final Email email = new Email()//
				.setSubject("[" + messages.at(MessageKeys.SYSTEM_NAME) + "] " + messages.at(MessageKeys.CHANGEUSER_DECISION_MAIL_SUBJECT))//
				.setFrom("Mister FROM <from@email.com>")//
				.addTo("Miss TO <" + user.getUserId() + ">")//
				.addAttachment("image.jpg", Paths.get("public", "images", lang.code(), "logo.png").toFile(), CID_LOGO)//
				.setBodyHtml(createDecisionHtmlMailBody(user, createSystemURL(), request, lang, messages).body().trim());

		mailer.send(email);
	}

	private Html createDecisionHtmlMailBody(final models.user.User user, final String systemURL, final Request request, final Lang lang, final Messages messages) {

		switch (lang.code()) {
		case "ja-JP":

			return views.html.setting.user.changeuser.decision.mail.body.render(user, systemURL, createInquiryURL(), request, lang, messages);
		default:

			return views.html.setting.user.changeuser.decision.mail.body.render(user, systemURL, createInquiryURL(), request, lang, messages);
		}
	}

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	public Result decisionHtmlMail(@Nonnull final Request request, final String langString) {

		final Messages messages = messagesApi.preferred(request);

		final models.user.User dummyUser = new models.user.User();
		dummyUser.setUserId("client@company.com");
		return ok(createDecisionHtmlMailBody(dummyUser, createSystemURL(), request, Lang.forCode(langString), messages));
	}

	private String createInquiryURL() {

		return createSystemURL() + controllers.inquiry.routes.Inquiry.index().url();
	}
}
