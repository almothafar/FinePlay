package controllers.user;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.Root;
import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;

import models.user.User;
import models.user.UserDao;
import models.user.User_;
import play.i18n.MessagesApi;
import play.mvc.Controller;

@Singleton
public class DefaultUserService implements UserService {

	@Inject
	private MessagesApi messages;

	@Inject
	private UserDao userDao;

	@Override
	public boolean isExist(EntityManager manager, String userId) {

		Objects.requireNonNull(userId);

		final long count = userDao.count(manager, (builder, query) -> {

			final Root<User> root = query.from(User.class);
			query.select(builder.count(root));
			query.where(builder.equal(root.get(User_.userId), userId));
		});

		if (!(count <= 1)) {

			throw new RuntimeException(userId);
		}

		return 1 == count;
	}

	@Override
	public void create(EntityManager manager, User user) throws AccountException {

		try {

			userDao.create(manager, user);
		} catch (final EntityExistsException e) {

			throw new AccountException(e.getLocalizedMessage());
		}
	}

	@Override
	public User read(EntityManager manager, String userId) throws AccountException {

		Objects.requireNonNull(userId);

		final User user;
		try {

			user = userDao.read(manager, (builder, query) -> {

				final Root<User> root = query.from(User.class);
				query.where(builder.equal(root.get(User_.userId), userId));
			});
		} catch (final NoResultException e) {

			throw new AccountNotFoundException(messages.get(Controller.ctx().lang(), "system.error.userid"));
		}

		return user;
	}

	@Override
	public User update(EntityManager manager, User user) throws AccountException {

		return userDao.update(manager, user);
	}

	@Override
	public void delete(EntityManager manager, User user) throws AccountException {

		userDao.delete(manager, user);
	}
}
