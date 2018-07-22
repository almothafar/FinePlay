package models.resetuser;

import java.util.function.BiConsumer;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import models.base.EntityDao;

public class ResetUserDao implements EntityDao<ResetUser> {

	@Override
	public void create(@Nonnull EntityManager manager, @Nonnull ResetUser resetUser) {

		EntityDao.super.create(manager, resetUser);
	}

	public ResetUser read(@Nonnull EntityManager manager, @Nonnull BiConsumer<CriteriaBuilder, CriteriaQuery<ResetUser>> condition) {

		return EntityDao.super.read(manager, ResetUser.class, condition);
	}

	@Override
	public void delete(@Nonnull EntityManager manager, @Nonnull ResetUser resetUser) {

		EntityDao.super.delete(manager, resetUser);
	}
}
