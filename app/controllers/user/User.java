package controllers.user;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import javax.inject.Inject;
import javax.security.auth.login.AccountException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import common.system.MessageKeys;
import common.system.SessionKeys;
import common.utils.Locales;
import common.utils.Sessions;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import models.user.PasswordFormContent;
import models.user.SignInFormContent;
import models.user.User.Theme;
import play.Mode;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.Cookie;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

public class User extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

	@Inject
	private views.html.user.index index;

	private static Duration TWO_WEEKS = Duration.ofDays(7 * 2);
	private static int OPERATION_TIMEOUT_MINUTES = 5;

	private static Path DEV_USERS_PATH = Paths.get(".", "dev", "users.json");

	private static class LazyHolder {

		private static String FIXED_USERS = initFixedUsers();
	}

	public static String getFixedUsers() {

		return LazyHolder.FIXED_USERS;
	}

	private static String initFixedUsers() {

		final Function<Path, String> readFixedUsers = (path) -> {

			try {

				return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
			} catch (IOException e) {

				throw new UncheckedIOException(e);
			}
		};
		final Mode mode = play.Environment.simple().mode();
		System.out.println("Application mode :" + mode);
		final String users;
		switch (mode) {
			case PROD :

				users = "[]";
				break;
			case DEV :
			case TEST :

				// TEST mode ? for Play2.6.0 〜 2.6.2
				users = readFixedUsers.apply(DEV_USERS_PATH);
				break;
			default :

				users = "[]";
				break;
		}

		return users;
	}

	@Inject
	private JPAApi jpaApi;

	@Inject
	private MessagesApi messages;

	@Inject
	private FormFactory formFactory;

	@Inject
	private UserAuthenticator userAuthenticator;

	public Result index() {

		session(models.user.User.THEME, Theme.DEFAULT.name());

		final SignInFormContent signInFormContent = new SignInFormContent();
		if (request().cookie(models.user.User.USERID) != null) {

			final String userId = request().cookie(models.user.User.USERID).value();
			final String decodedUserId;
			try {

				decodedUserId = URLDecoder.decode(userId, StandardCharsets.UTF_8.name());
			} catch (final UnsupportedEncodingException e) {

				throw new RuntimeException(e);
			}

			signInFormContent.setUserId(decodedUserId);
			signInFormContent.setStoreAccount(Boolean.TRUE.toString());
		}
		if (request().cookie(models.user.User.PASSWORD) != null) {

			final String password = request().cookie(models.user.User.PASSWORD).value();
			final String decodedPassword;
			try {

				decodedPassword = URLDecoder.decode(password, StandardCharsets.UTF_8.name());
			} catch (final UnsupportedEncodingException e) {

				throw new RuntimeException(e);
			}

			signInFormContent.setPassword(decodedPassword);
		}

		final Form<SignInFormContent> signInForm = formFactory.form(SignInFormContent.class).fill(signInFormContent);

		return ok(index.render(signInForm, getFixedUsers()));
	}

	@Transactional()
	@RequireCSRFCheck
	public Result signIn() {

		final Form<SignInFormContent> signInForm = formFactory.form(SignInFormContent.class).bindFromRequest();
		if (!signInForm.hasErrors()) {

			final SignInFormContent signInFormContent = signInForm.get();

			final String userId = signInFormContent.getUserId();
			final String password = signInFormContent.getPassword();

			final models.user.User user;
			try {

				user = userAuthenticator.signIn(lang(), userId, password);
			} catch (final AccountException e) {

				signInFormContent.setPassword(null);

				final Form<SignInFormContent> failureSignInForm = formFactory.form(SignInFormContent.class)//
						.fill(signInFormContent)//
						// conceal a reject reason
						.withGlobalError(messages.get(lang(), MessageKeys.SYSTEM_ERROR_USER));
				return failureSignIn(failureSignInForm);
			}

			if (Boolean.valueOf(signInFormContent.getStoreAccount())) {

				final String encodedUserId;
				final String encodedPassword;
				try {

					encodedUserId = URLEncoder.encode(userId, StandardCharsets.UTF_8.name());
					encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8.name());
				} catch (final UnsupportedEncodingException e) {

					throw new RuntimeException(e);
				}

				response().setCookie(Cookie.builder(models.user.User.USERID, encodedUserId).withMaxAge(TWO_WEEKS).build());
				response().setCookie(Cookie.builder(models.user.User.PASSWORD, encodedPassword).withMaxAge(TWO_WEEKS).build());
			} else {

				response().discardCookie(models.user.User.USERID);
				response().discardCookie(models.user.User.PASSWORD);
			}

			session(models.user.User.THEME, user.getTheme().name());

			final Lang selectedLang = Locales.toLang(user.getLocale());
			changeLang(selectedLang);

			session(models.user.User.ZONEID, user.getZoneId().getId());

			session(models.user.User.USERID, userId);
			session(models.user.User.ROLES, Sessions.toValue(new ArrayList<>(user.getRoles())));
			session(SessionKeys.OPERATION_TIMEOUT, LocalDateTime.MIN.toString());

			String requestUrl = ctx().session().get(SessionKeys.REQUEST_URL);
			if (requestUrl == null || requestUrl.equals("") || requestUrl.equals(controllers.user.routes.User.index().absoluteURL(request()))) {

				requestUrl = controllers.home.routes.Home.index().url();
			}
			return redirect(requestUrl);
		} else {

			return failureSignIn(signInForm);
		}
	}

	private Result failureSignIn(final Form<SignInFormContent> signInForm) {

		return badRequest(index.render(signInForm, getFixedUsers()));
	}

	@PermissionsAllowed(value = {Permission.READ})
	@Authenticated(common.core.Authenticator.class)
	@Transactional()
	public Result signOut() {

		updateSignOutTime();
		session().clear();
		clearLang();

		// for Edge measures.
		Lang acceptLang = request().acceptLanguages().stream().findFirst().orElse(Locales.toLang(Locale.US));
		changeLang(acceptLang);

		return redirect(controllers.user.routes.User.index());
	}

	public void updateSignOutTime() {

		final models.user.User user;
		try {

			user = userAuthenticator.signOut(lang(), session(models.user.User.USERID));
		} catch (final AccountException e) {

			throw new RuntimeException(e);
		}
	}

	@PermissionsAllowed(value = {Permission.READ})
	@Authenticated(common.core.Authenticator.class)
	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	@Transactional(readOnly = true)
	@RequireCSRFCheck
	public CompletionStage<Result> confirm() {

		final Result result = jpaApi.withTransaction(manager -> {

			final Form<PasswordFormContent> passwordForm = formFactory.form(PasswordFormContent.class).bindFromRequest();
			if (!passwordForm.hasErrors()) {

				final PasswordFormContent passwordFormContent = passwordForm.get();

				final String userId = session().get(models.user.User.USERID);
				final String password = passwordFormContent.getPassword();

				final models.user.User user;
				try {

					user = userAuthenticator.confirm(lang(), userId, password);
				} catch (final AccountException e) {

					passwordFormContent.setPassword(null);

					final Form<PasswordFormContent> failurePasswordForm = formFactory.form(PasswordFormContent.class).fill(passwordFormContent);
					failurePasswordForm.withGlobalError(e.getLocalizedMessage());
					return failureConfirm(failurePasswordForm);
				}

				session(SessionKeys.OPERATION_TIMEOUT, LocalDateTime.now().plusMinutes(OPERATION_TIMEOUT_MINUTES).toString());

				final ObjectMapper mapper = new ObjectMapper();
				final ObjectNode response = mapper.createObjectNode();
				response.put("status", "success");

				return ok(response);
			} else {

				return failureConfirm(passwordForm);
			}
		});

		return CompletableFuture.supplyAsync(() -> {

			return result;
		});
	}

	private Result failureConfirm(final Form<PasswordFormContent> passwordForm) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();
		result.put("status", "error");
		if (passwordForm.hasGlobalErrors()) {

			throw new IllegalStateException();
		}

		final ObjectNode errorsNode = mapper.createObjectNode();
		passwordForm.allErrors().stream().forEach(error -> {

			final String property = error.key();

			final ArrayNode propertyErrorNode = mapper.createArrayNode();
			error.messages().forEach(message -> propertyErrorNode.add(messages.get(lang(), message)));

			errorsNode.set(property, propertyErrorNode);
		});
		result.set("errors", errorsNode);

		return badRequest(result);
	}
}
