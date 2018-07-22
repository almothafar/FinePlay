package models.setting.user.changeuser;

import java.util.function.BiConsumer;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import models.base.EntityDao;

public class ChangeUserDao implements EntityDao<ChangeUser> {

	@Override
	public void create(@Nonnull EntityManager manager, @Nonnull ChangeUser changeUser) {

		EntityDao.super.create(manager, changeUser);
	}

	public ChangeUser read(@Nonnull EntityManager manager, @Nonnull BiConsumer<CriteriaBuilder, CriteriaQuery<ChangeUser>> condition) {

		return EntityDao.super.read(manager, ChangeUser.class, condition);
	}

	@Override
	public void delete(@Nonnull EntityManager manager, @Nonnull ChangeUser registUser) {

		EntityDao.super.delete(manager, registUser);
	}
}
