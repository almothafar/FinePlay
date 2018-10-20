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
import common.utils.Locales;
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
import play.twirl.api.Html;

public class RegistUser extends Controller {

	private static long ONE_DAY = Duration.ofDays(1).toDays();

	public static final String CID_LOGO = "logo";

	@Inject
	private Langs langs;

	@Inject
	private MessagesApi messages;

	@Inject
	private JPAApi jpa;

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

	public Result index() {

		final RegistFormContent registFormContent = new RegistFormContent();
		final Form<RegistFormContent> registForm = formFactory.form(RegistFormContent.class).fill(registFormContent);

		return ok(views.html.registuser.regist.render(registForm));
	}

	@RequireCSRFCheck
	public Result apply() {

		return jpa.withTransaction(manager -> {

			final Form<RegistFormContent> registForm = formFactory.form(RegistFormContent.class).bindFromRequest();
			if (!registForm.hasErrors()) {

				final RegistFormContent registFormContent = registForm.get();

				final String userId = registFormContent.getUserId();
				final String password = registFormContent.getPassword();
				final String rePassword = registFormContent.getRePassword();
				final Locale locale = getInferencedLocale();
				final ZoneId zoneId = getInferencedZoneId(registFormContent);

				final models.registuser.RegistUser registUser;
				try {

					if (!password.equals(rePassword)) {

						registFormContent.setPassword(null);
						registFormContent.setRePassword(null);
						throw new AccountException(messages.get(lang(), MessageKeys.SYSTEM_ERROR_PASSWORD_NOTEQUAL));
					}

					if (userService.isExist(manager, userId)) {

						registFormContent.setUserId(null);
						throw new AccountException(messages.get(lang(), MessageKeys.SYSTEM_ERROR_USERID_EXIST));
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
					return failureApply(failureRegistForm);
				}

				sendProvisionalEmail(registUser);
				return ok(views.html.registuser.provisional.complete.render(registForm))//
						.withLang(Locales.toLang(locale), messages);
			} else {

				return failureApply(registForm);
			}
		});
	}

	@Nonnull
	private Locale getInferencedLocale() {

		Lang useLang = lang();
		if (!useLang.country().isEmpty()) {

			useLang = Locales.toLang(normalizeLocale(useLang));
		}

		if (!langs.availables().contains(useLang)) {

			final String useLangLanguage = useLang.language();
			final Optional<Lang> langOptional = langs.availables().stream().filter(lang -> lang.language().equals(useLangLanguage)).findFirst();
			if (!langOptional.isPresent()) {

				useLang = Locales.toLang(Locale.US);
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

	private Result failureApply(final Form<RegistFormContent> applyForm) {

		return badRequest(views.html.registuser.regist.render(applyForm));
	}

	private String createRegularURL(final String code) {

		return createSystemURL() + controllers.registuser.routes.RegistUser.regular(code).url();
	}

	private void sendProvisionalEmail(final models.registuser.RegistUser registUser) {

		final String regularURL = createRegularURL(registUser.getCode());

		final Email email = new Email()//
				.setSubject("[" + messages.get(lang(), MessageKeys.SYSTEM_NAME) + "] " + messages.get(lang(), MessageKeys.REGISTUSER_PROVISIONAL_MAIL_SUBJECT))//
				.setFrom("Mister FROM <from@email.com>")//
				.addTo("Miss TO <" + registUser.getUserId() + ">")//
				.addAttachment("image.jpg", Paths.get("public", "images", lang().code(), "logo.png").toFile(), CID_LOGO)//
				.setBodyHtml(createProvisionalHtmlMailBody(registUser, regularURL, lang()).body().trim());

		mailer.send(email);
	}

	private Html createProvisionalHtmlMailBody(final models.registuser.RegistUser user, final String regularURL, final Lang lang) {

		switch (lang.code()) {
		case "ja-JP":

			return views.html.registuser.provisional.mail.body_ja_JP.render(user, regularURL, createInquiryURL());
		default:

			return views.html.registuser.provisional.mail.body_en_US.render(user, regularURL, createInquiryURL());
		}
	}

	@PermissionsAllowed(value = { Permission.MANAGE })
	public Result provisionalHtmlMail(final String langString) {

		final models.registuser.RegistUser dummyRegistUser = new models.registuser.RegistUser();
		dummyRegistUser.setUserId("client@company.com");
		dummyRegistUser.setCode();
		return ok(createProvisionalHtmlMailBody(dummyRegistUser, createRegularURL(dummyRegistUser.getCode()), Lang.forCode(langString)));
	}

	public Result regular(final String code) {

		return jpa.withTransaction(manager -> {

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
			user.setSalt(registUser.getSalt());
			user.setHashedPassword(registUser.getHashedPassword());
			user.setRoles(EnumSet.of(Role.CUSTOMER));
			user.setTheme(Theme.DEFAULT);
			user.setLocale(registUser.getLocale());
			user.setZoneId(registUser.getZoneId());
			user.setExpireDateTime(LocalDateTime.now().plusYears(1000));

			try {

				userService.create(manager, user);
			} catch (final AccountException e) {

				throw new PlayException("", "", e);
			}

			registUserDao.delete(manager, registUser);

			sendRegularEmail(user);
			return ok(views.html.registuser.regular.complete.render(user));
		});
	}

	private String createSystemURL() {

		final String host = config.hasPath("system.url") ? config.getString("system.url") : "http://localhost:9000";
		return host;
	}

	private void sendRegularEmail(final models.user.User user) {

		final Email email = new Email()//
				.setSubject("[" + messages.get(lang(), MessageKeys.SYSTEM_NAME) + "] " + messages.get(lang(), MessageKeys.REGISTUSER_REGULAR_MAIL_SUBJECT))//
				.setFrom("Mister FROM <from@email.com>")//
				.addTo("Miss TO <" + user.getUserId() + ">")//
				.addAttachment("image.jpg", Paths.get("public", "images", lang().code(), "logo.png").toFile(), CID_LOGO)//
				.setBodyHtml(createRegularHtmlMailBody(user, createSystemURL(), lang()).body().trim());

		mailer.send(email);
	}

	private Html createRegularHtmlMailBody(final models.user.User user, final String systemURL, final Lang lang) {

		switch (lang.code()) {
		case "ja-JP":

			return views.html.registuser.regular.mail.body.render(user, systemURL, createInquiryURL());
		default:

			return views.html.registuser.regular.mail.body.render(user, systemURL, createInquiryURL());
		}
	}

	@PermissionsAllowed(value = { Permission.MANAGE })
	public Result regularHtmlMail(final String langString) {

		final models.user.User dummyUser = new models.user.User();
		dummyUser.setUserId("client@company.com");
		return ok(createRegularHtmlMailBody(dummyUser, createSystemURL(), Lang.forCode(langString)));
	}

	private String createInquiryURL() {

		return createSystemURL() + controllers.inquiry.routes.Inquiry.index().url();
	}
}
