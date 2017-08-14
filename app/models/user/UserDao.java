package models.user;

import java.util.function.BiConsumer;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import models.base.EntityDao;

public class UserDao implements EntityDao<User> {

	public long count(final EntityManager manager, final BiConsumer<CriteriaBuilder, CriteriaQuery<Long>> condition) {

		return EntityDao.super.count(manager, User.class, condition);
	}

	public User read(final EntityManager manager, final BiConsumer<CriteriaBuilder, CriteriaQuery<User>> condition) {

		return EntityDao.super.read(manager, User.class, condition);
	}
}
