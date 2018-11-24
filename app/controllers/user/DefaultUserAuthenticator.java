package controllers.user;

import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.AccountNotFoundException;

import common.system.MessageKeys;
import play.i18n.Messages;

class DefaultUserAuthenticator implements UserAuthenticator {

	@Inject
	private UserService userService;

	@Override
	public models.user.User signIn(final EntityManager manager, final Messages messages, final String userId, final String password) throws AccountException {

		final models.user.User user;
		try {

			user = userService.read(manager, messages, userId);
		} catch (final AccountNotFoundException e) {

			throw e;
		}

		if (!user.isValidPassword(password)) {

			throw new AccountNotFoundException(messages.at(MessageKeys.SYSTEM_ERROR_PASSWORD));
		}

		if (!user.isValidExpireTime()) {

			throw new AccountExpiredException(messages.at(MessageKeys.SYSTEM_ERROR_EXPIRETIME_PAST));
		}

		user.setSignInDateTime(LocalDateTime.now());

		userService.update(manager, messages, user);

		return user;
	}

	@Override
	public models.user.User confirm(final EntityManager manager, final Messages messages, final String userId, final String password) throws AccountException {

		final models.user.User user;

		try {

			user = userService.read(manager, messages, userId);
		} catch (final AccountNotFoundException e) {

			throw e;
		}

		if (!user.isValidPassword(password)) {

			throw new AccountNotFoundException(messages.at(MessageKeys.SYSTEM_ERROR_PASSWORD));
		}

		return user;
	}

	@Override
	public models.user.User signOut(final EntityManager manager, final Messages messages, final String userId) throws AccountException {

		final models.user.User user;

		user = userService.read(manager, messages, userId);
		user.setSignOutDateTime(LocalDateTime.now());

		userService.update(manager, messages, user);

		return user;
	}
}
