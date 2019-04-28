package controllers.registuser;

import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.Root;
import javax.security.auth.login.AccountException;

import com.typesafe.config.Config;

import common.system.MessageKeys;
import controllers.user.UserService;
import models.registuser.RegistFormContent;
import models.registuser.RegistUserDao;
import models.registuser.RegistUser_;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import models.user.User.Role;
import models.user.User.Theme;
import play.api.PlayException;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Lang;
import play.i18n.Langs;
import play.i18n.MessagesApi;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import play.mvc.Controller;
import play.mvc.Result;
import play.i18n.Messages;
import play.mvc.Http.Request;
import play.twirl.api.Html;

public class RegistUser extends Controller {

	private static long ONE_DAY = Duration.ofDays(1).toDays();

	public static final String CID_LOGO = "logo";

	@Inject
	private Langs langs;

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
	private RegistUserDao registUserDao;

	public Result index(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final RegistFormContent registFormContent = new RegistFormContent();
		final Form<RegistFormContent> registForm = formFactory.form(RegistFormContent.class).fill(registFormContent);

		return ok(views.html.registuser.regist.render(registForm, request, lang, messages));
	}

	@RequireCSRFCheck
	public Result apply(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return jpaApi.withTransaction(manager -> {

			final Form<RegistFormContent> registForm = formFactory.form(RegistFormContent.class).bindFromRequest(request);
			if (!registForm.hasErrors()) {

				final RegistFormContent registFormContent = registForm.get();

				final String userId = registFormContent.getUserId();
				final String password = registFormContent.getPassword();
				final String rePassword = registFormContent.getRePassword();
				final Locale locale = getInferencedLocale(lang);
				final ZoneId zoneId = getInferencedZoneId(registFormContent);

				final models.registuser.RegistUser registUser;
				try {

					if (!password.equals(rePassword)) {

						registFormContent.setPassword(null);
						registFormContent.setRePassword(null);
						throw new AccountException(messages.at(MessageKeys.SYSTEM_ERROR_PASSWORD_NOTEQUAL));
					}

					if (userService.isExist(manager, messages, userId)) {

						registFormContent.setUserId(null);
						throw new AccountException(messages.at(MessageKeys.SYSTEM_ERROR_USERID_EXIST));
					}

					registUser = new models.registuser.RegistUser();
					registUser.setUserId(userId);
					registUser.setPassword(password);
					registUser.setLocale(locale);
					registUser.setZoneId(zoneId);
					registUser.setCode();
					registUser.setExpireDateTime(LocalDateTime.now().plusDays(ONE_DAY));

					try {

						registUserDao.create(manager, registUser);
					} catch (final EntityExistsException e) {

						throw new RuntimeException(e);
					}
				} catch (final AccountException e) {

					final Form<RegistFormContent> failureRegistForm = formFactory.form(RegistFormContent.class)//
							.fill(registFormContent)//
							.withGlobalError(e.getLocalizedMessage());
					return failureApply(failureRegistForm, request, lang, messages);
				}

				sendProvisionalEmail(registUser, request, lang, messages);
				return ok(views.html.registuser.provisional.complete.render(registForm, request, lang, messages))//
						.withLang(new Lang(locale), messagesApi);
			} else {

				return failureApply(registForm, request, lang, messages);
			}
		});
	}

	@Nonnull
	private Locale getInferencedLocale(Lang useLang) {

		if (!useLang.country().isEmpty()) {

			useLang = new Lang(normalizeLocale(useLang));
		}

		if (!langs.availables().contains(useLang)) {

			final String useLangLanguage = useLang.language();
			final Optional<Lang> langOptional = langs.availables().stream().filter(lang -> lang.language().equals(useLangLanguage)).findFirst();
			if (!langOptional.isPresent()) {

				useLang = new Lang(Locale.US);
			}
		}

		return useLang.toLocale();
	}

	private static Locale normalizeLocale(final Lang useLang) {

		switch (useLang.language()) {
		case "ja":

			return Locale.JAPAN;
		case "zh":

			switch (useLang.country()) {
			case "TW":

				return Locale.US;
			// return Locale.TRADITIONAL_CHINESE;
			case "CN":
			default:

				return Locale.US;
			// return Locale.SIMPLIFIED_CHINESE;
			}
		case "ko":

			return Locale.US;
		// return Locale.KOREA;
		case "en":
		default:

			return Locale.US;
		}
	}

	@Nonnull
	private ZoneId getInferencedZoneId(@Nonnull final RegistFormContent registFormContent) {

		final String offsetSecond = registFormContent.getOffsetSecond();
		if (offsetSecond.isEmpty()) {

			throw new RuntimeException("Not received offsetSecond. :" + offsetSecond);
		}

		ZoneId zoneId = ZoneOffset.ofTotalSeconds(Integer.parseInt(offsetSecond));

		final String shortZoneId = registFormContent.getShortZoneId();
		if (ZoneId.SHORT_IDS.containsKey(shortZoneId)) {

			zoneId = ZoneId.of(shortZoneId, ZoneId.SHORT_IDS);
		}

		return zoneId;
	}

	private Result failureApply(final Form<RegistFormContent> applyForm, final Request request, final Lang lang, final Messages messages) {

		return badRequest(views.html.registuser.regist.render(applyForm, request, lang, messages));
	}

	private String createRegularURL(final String code) {

		return createSystemURL() + controllers.registuser.routes.RegistUser.regular(code).url();
	}

	private void sendProvisionalEmail(final models.registuser.RegistUser registUser, final Request request, final Lang lang, final Messages messages) {

		final String regularURL = createRegularURL(registUser.getCode());

		final Email email = new Email()//
				.setSubject("[" + messages.at(MessageKeys.SYSTEM_NAME) + "] " + messages.at(MessageKeys.REGISTUSER_PROVISIONAL_MAIL_SUBJECT))//
				.setFrom("Mister FROM <from@email.com>")//
				.addTo("Miss TO <" + registUser.getUserId() + ">")//
				.addAttachment("image.jpg", Paths.get("public", "images", lang.code(), "logo.png").toFile(), CID_LOGO)//
				.setBodyHtml(createProvisionalHtmlMailBody(registUser, regularURL, request, lang, messages).body().trim());

		mailer.send(email);
	}

	private Html createProvisionalHtmlMailBody(final models.registuser.RegistUser user, final String regularURL, final Request request, final Lang lang, final Messages messages) {

		switch (lang.code()) {
		case "ja-JP":

			return views.html.registuser.provisional.mail.body_ja_JP.render(user, regularURL, createInquiryURL(), request, lang, messages);
		default:

			return views.html.registuser.provisional.mail.body_en_US.render(user, regularURL, createInquiryURL(), request, lang, messages);
		}
	}

	@PermissionsAllowed(value = { Permission.MANAGE })
	public Result provisionalHtmlMail(@Nonnull final Request request, final String langString) {

		final Messages messages = messagesApi.preferred(request);

		final models.registuser.RegistUser dummyRegistUser = new models.registuser.RegistUser();
		dummyRegistUser.setUserId("client@company.com");
		dummyRegistUser.setCode();
		return ok(createProvisionalHtmlMailBody(dummyRegistUser, createRegularURL(dummyRegistUser.getCode()), request, Lang.forCode(langString), messages));
	}

	public Result regular(@Nonnull final Request request, final String code) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return jpaApi.withTransaction(manager -> {

			models.registuser.RegistUser registUser;
			try {

				registUser = registUserDao.read(manager, (builder, query) -> {

					final Root<models.registuser.RegistUser> root = query.from(models.registuser.RegistUser.class);
					query.where(builder.and(builder.equal(root.get(RegistUser_.code), code), builder.greaterThanOrEqualTo(root.get(RegistUser_.expireDateTime), LocalDateTime.now())));
				});
			} catch (final NoResultException e) {

				throw new PlayException("", "", e);
			} catch (final NonUniqueResultException e) {

				throw new RuntimeException(e);
			}

			final models.user.User user = new models.user.User();
			user.setUserId(registUser.getUserId());
			user.setHashedPassword(registUser.getHashedPassword());
			user.setRoles(EnumSet.of(Role.CUSTOMER));
			user.setTheme(Theme.DEFAULT);
			user.setLocale(registUser.getLocale());
			user.setZoneId(registUser.getZoneId());
			user.setExpireDateTime(LocalDateTime.now().plusYears(1000));
			user.setUpdateDateTime(LocalDateTime.now());

			try {

				userService.create(manager, messages, user);
			} catch (final AccountException e) {

				throw new PlayException("", "", e);
			}

			registUserDao.delete(manager, registUser);

			sendRegularEmail(user, request, lang, messages);
			return ok(views.html.registuser.regular.complete.render(user, request, lang, messages));
		});
	}

	private String createSystemURL() {

		final String host = config.hasPath("system.url") ? config.getString("system.url") : "http://localhost:9000";
		return host;
	}

	private void sendRegularEmail(final models.user.User user, final Request request, final Lang lang, final Messages messages) {

		final Email email = new Email()//
				.setSubject("[" + messages.at(MessageKeys.SYSTEM_NAME) + "] " + messages.at(MessageKeys.REGISTUSER_REGULAR_MAIL_SUBJECT))//
				.setFrom("Mister FROM <from@email.com>")//
				.addTo("Miss TO <" + user.getUserId() + ">")//
				.addAttachment("image.jpg", Paths.get("public", "images", lang.code(), "logo.png").toFile(), CID_LOGO)//
				.setBodyHtml(createRegularHtmlMailBody(user, createSystemURL(), request, lang, messages).body().trim());

		mailer.send(email);
	}

	private Html createRegularHtmlMailBody(final models.user.User user, final String systemURL, final Request request, final Lang lang, final Messages messages) {

		switch (lang.code()) {
		case "ja-JP":

			return views.html.registuser.regular.mail.body.render(user, systemURL, createInquiryURL(), request, lang, messages);
		default:

			return views.html.registuser.regular.mail.body.render(user, systemURL, createInquiryURL(), request, lang, messages);
		}
	}

	@PermissionsAllowed(value = { Permission.MANAGE })
	public Result regularHtmlMail(@Nonnull final Request request, final String langString) {

		final Messages messages = messagesApi.preferred(request);

		final models.user.User dummyUser = new models.user.User();
		dummyUser.setUserId("client@company.com");
		return ok(createRegularHtmlMailBody(dummyUser, createSystemURL(), request, Lang.forCode(langString), messages));
	}

	private String createInquiryURL() {

		return createSystemURL() + controllers.inquiry.routes.Inquiry.index().url();
	}
}
