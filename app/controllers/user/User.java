package controllers.user;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.security.auth.login.AccountException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import common.system.MessageKeys;
import common.system.SessionKeys;
import common.utils.Sessions;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import models.user.PasswordFormContent;
import models.user.SignInFormContent;
import models.user.User.Theme;
import play.Application;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.Cookie;
import play.mvc.Http.Session;
import play.mvc.Result;
import javax.annotation.Nonnull;
import play.i18n.Messages;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

public class User extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private Provider<Application> applicationProvider;

	@Inject
	private views.html.user.index index;

	private static Duration TWO_WEEKS = Duration.ofDays(7 * 2);
	private static int OPERATION_TIMEOUT_MINUTES = 5;

	private static Path DEV_USERS_PATH = Paths.get(".", "dev", "users.json");

	private static class LazyHolder {

		private static String DEV_FIXED_USERS = initDevFixedUsers();
	}

	public String getFixedUsers() {

		if (applicationProvider.get().isProd()) {

			return "[]";
		} else {

			return LazyHolder.DEV_FIXED_USERS;
		}
	}

	private static String initDevFixedUsers() {

		final Function<Path, String> readFixedUsers = (path) -> {

			try {

				return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
			} catch (IOException e) {

				throw new UncheckedIOException(e);
			}
		};

		final String users = readFixedUsers.apply(DEV_USERS_PATH);
		return users;
	}

	@Inject
	private JPAApi jpaApi;

	@Inject
	private MessagesApi messagesApi;

	@Inject
	private FormFactory formFactory;

	@Inject
	private UserAuthenticator userAuthenticator;

	public Result index(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		request.session().adding(new HashMap<String, String>() {
			{//
				put(models.user.User_.THEME, Theme.DEFAULT.name());
			}
		});

		final SignInFormContent signInFormContent = new SignInFormContent();
		if (request.cookie(models.user.User_.USER_ID) != null) {

			final String userId = request.cookie(models.user.User_.USER_ID).value();
			final String decodedUserId;
			try {

				decodedUserId = URLDecoder.decode(userId, StandardCharsets.UTF_8.name());
			} catch (final UnsupportedEncodingException e) {

				throw new RuntimeException(e);
			}

			signInFormContent.setUserId(decodedUserId);
			signInFormContent.setStoreAccount(Boolean.TRUE.toString());
		}
		if (request.cookie(models.user.User.PASSWORD) != null) {

			final String password = request.cookie(models.user.User.PASSWORD).value();
			final String decodedPassword;
			try {

				decodedPassword = URLDecoder.decode(password, StandardCharsets.UTF_8.name());
			} catch (final UnsupportedEncodingException e) {

				throw new RuntimeException(e);
			}

			signInFormContent.setPassword(decodedPassword);
		}

		final Form<SignInFormContent> signInForm = formFactory.form(SignInFormContent.class).fill(signInFormContent);

		return ok(index.render(signInForm, getFixedUsers(), request, lang, messages));
	}

	@RequireCSRFCheck
	public Result signIn(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return jpaApi.withTransaction(manager -> {

			final Form<SignInFormContent> signInForm = formFactory.form(SignInFormContent.class).bindFromRequest(request);
			if (!signInForm.hasErrors()) {

				final SignInFormContent signInFormContent = signInForm.get();

				final String userId = signInFormContent.getUserId();
				final String password = signInFormContent.getPassword();

				final models.user.User user;
				try {

					user = userAuthenticator.signIn(manager, messages, userId, password);
				} catch (final AccountException e) {

					signInFormContent.setPassword(null);

					final Form<SignInFormContent> failureSignInForm = formFactory.form(SignInFormContent.class)//
							.fill(signInFormContent)//
							// conceal a reject reason
							.withGlobalError(messages.at(MessageKeys.SYSTEM_ERROR_USER));
					return failureSignIn(failureSignInForm, request, lang, messages);
				}

				final Lang selectedLang = new Lang(user.getLocale());

				String requestUrl = request.session().getOptional(SessionKeys.REQUEST_URL).orElse(null);
				if (requestUrl == null || requestUrl.equals("") || requestUrl.equals(controllers.user.routes.User.index().absoluteURL(request))) {

					requestUrl = controllers.home.routes.Home.index().url();
				}

				Result result = redirect(requestUrl)//
						.withLang(selectedLang, messagesApi)//
						.withSession(new Session(new HashMap<String, String>() {
							{//
								put(models.user.User_.USER_ID, userId);
								put(models.user.User_.THEME, user.getTheme().name());
								put(models.user.User_.ZONE_ID, user.getZoneId().getId());
								put(models.user.User_.ROLES, Sessions.toValue(new ArrayList<>(user.getRoles())));
								put(SessionKeys.OPERATION_TIMEOUT, LocalDateTime.MIN.toString());
							}
						}));
				if (Boolean.valueOf(signInFormContent.getStoreAccount())) {
					LOGGER.info("Store account");

					final String encodedUserId;
					final String encodedPassword;
					try {

						encodedUserId = URLEncoder.encode(userId, StandardCharsets.UTF_8.name());
						encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8.name());
					} catch (final UnsupportedEncodingException e) {

						throw new RuntimeException(e);
					}

					result = result.withCookies(// Fluent interface, but return different instance.
							Cookie.builder(models.user.User_.USER_ID, encodedUserId).withMaxAge(TWO_WEEKS).build(), //
							Cookie.builder(models.user.User.PASSWORD, encodedPassword).withMaxAge(TWO_WEEKS).build());
				} else {
					LOGGER.info("Not Store account");

					result = result// Fluent interface, but return different instance.
							.discardingCookie(models.user.User_.USER_ID)//
							.discardingCookie(models.user.User.PASSWORD);
				}

				return result;
			} else {

				return failureSignIn(signInForm, request, lang, messages);
			}
		});
	}

	private Result failureSignIn(final Form<SignInFormContent> signInForm, final Request request, final Lang lang, final Messages messages) {

		return badRequest(index.render(signInForm, getFixedUsers(), request, lang, messages));
	}

	@PermissionsAllowed(value = { Permission.READ })
	@Authenticated(common.core.Authenticator.class)
	public Result signOut(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		messages.lang();

		return jpaApi.withTransaction(manager -> {

			updateSignOutTime(manager, request, messages);

			Lang acceptLang = request.acceptLanguages().stream().findFirst().orElse(new Lang(Locale.US));

			return redirect(controllers.user.routes.User.index())//
					.withoutLang(messagesApi)//
					.withLang(acceptLang, messagesApi)// for Edge measures.
					.withNewSession();
		});
	}

	public void updateSignOutTime(final EntityManager manager, @Nonnull final Request request, final Messages messages) {

		try {

			userAuthenticator.signOut(manager, messages, request.session().getOptional(models.user.User_.USER_ID).get());
		} catch (final AccountException e) {

			throw new RuntimeException(e);
		}
	}

	@PermissionsAllowed(value = { Permission.READ })
	@Authenticated(common.core.Authenticator.class)
	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	@RequireCSRFCheck
	public CompletionStage<Result> confirm(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final Result result = jpaApi.withTransaction(manager -> {

			final Form<PasswordFormContent> passwordForm = formFactory.form(PasswordFormContent.class).bindFromRequest(request);
			if (!passwordForm.hasErrors()) {

				final PasswordFormContent passwordFormContent = passwordForm.get();

				final String userId = request.session().getOptional(models.user.User_.USER_ID).get();
				final String password = passwordFormContent.getPassword();

				try {

					userAuthenticator.confirm(manager, messages, userId, password);
				} catch (final AccountException e) {

					passwordFormContent.setPassword(null);

					final Form<PasswordFormContent> failurePasswordForm = formFactory.form(PasswordFormContent.class).fill(passwordFormContent);
					failurePasswordForm.withGlobalError(e.getLocalizedMessage());
					return failureConfirm(failurePasswordForm, request, lang, messages);
				}

				final ObjectMapper mapper = new ObjectMapper();
				final ObjectNode response = mapper.createObjectNode();
				response.put("status", "success");

				return ok(response).addingToSession(request, new HashMap<String, String>() {
					{//
						put(SessionKeys.OPERATION_TIMEOUT, LocalDateTime.now().plusMinutes(OPERATION_TIMEOUT_MINUTES).toString());//
					}
				});
			} else {

				return failureConfirm(passwordForm, request, lang, messages);
			}
		});

		return CompletableFuture.supplyAsync(() -> {

			return result;
		});
	}

	private Result failureConfirm(final Form<PasswordFormContent> passwordForm, final Request request, final Lang lang, final Messages messages) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();
		result.put("status", "error");
		if (passwordForm.hasGlobalErrors()) {

			throw new IllegalStateException();
		}

		final ObjectNode errorsNode = mapper.createObjectNode();
		passwordForm.errors().stream().forEach(error -> {

			final String property = error.key();

			final ArrayNode propertyErrorNode = mapper.createArrayNode();
			error.messages().forEach(message -> propertyErrorNode.add(messages.at(message)));

			errorsNode.set(property, propertyErrorNode);
		});
		result.set("errors", errorsNode);

		return badRequest(result);
	}
}
