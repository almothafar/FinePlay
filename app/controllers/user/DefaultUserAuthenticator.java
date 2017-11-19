package controllers.user;

import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.AccountNotFoundException;

import common.system.MessageKeys;
import play.db.jpa.JPAApi;
import play.i18n.Lang;
import play.i18n.MessagesApi;

class DefaultUserAuthenticator implements UserAuthenticator {

	@Inject
	private JPAApi jpaApi;

	@Inject
	private MessagesApi messages;

	@Inject
	private UserService userService;

	/*
	 * (非 Javadoc)
	 * 
	 * @see controllers.user.UserAuthenticator#signIn(play.i18n.Lang,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public models.user.User signIn(final Lang lang, final String userId, final String password) throws AccountException {

		final models.user.User user;
		try {

			user = userService.read(jpaApi.em(), userId);
		} catch (final AccountNotFoundException e) {

			throw e;
		}

		if (!user.isValidPassword(password)) {

			throw new AccountNotFoundException(messages.get(lang, MessageKeys.SYSTEM_ERROR_PASSWORD));
		}

		if (!user.isValidExpireTime()) {

			throw new AccountExpiredException(messages.get(lang, MessageKeys.SYSTEM_ERROR_EXPIRETIME_PAST));
		}

		user.setSignInDateTime(LocalDateTime.now());

		userService.update(jpaApi.em(), user);

		return user;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see controllers.user.UserAuthenticator#confirm(play.i18n.Lang,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public models.user.User confirm(final Lang lang, final String userId, final String password) throws AccountException {

		final models.user.User user;

		try {

			user = userService.read(jpaApi.em(), userId);
		} catch (final AccountNotFoundException e) {

			throw e;
		}

		if (!user.isValidPassword(password)) {

			throw new AccountNotFoundException(messages.get(lang, MessageKeys.SYSTEM_ERROR_PASSWORD));
		}

		return user;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see controllers.user.UserAuthenticator#signOut(play.i18n.Lang,
	 * java.lang.String)
	 */
	@Override
	public models.user.User signOut(final Lang lang, final String userId) throws AccountException {

		final models.user.User user;

		user = userService.read(jpaApi.em(), userId);
		user.setSignOutDateTime(LocalDateTime.now());

		userService.update(jpaApi.em(), user);

		return user;
	}
}
