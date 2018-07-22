package models.registuser;

import java.util.function.BiConsumer;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import models.base.EntityDao;

public class RegistUserDao implements EntityDao<RegistUser> {

	@Override
	public void create(@Nonnull EntityManager manager, @Nonnull RegistUser registUser) {

		EntityDao.super.create(manager, registUser);
	}

	public RegistUser read(@Nonnull EntityManager manager, @Nonnull BiConsumer<CriteriaBuilder, CriteriaQuery<RegistUser>> condition) {

		return EntityDao.super.read(manager, RegistUser.class, condition);
	}

	@Override
	public void delete(@Nonnull EntityManager manager, @Nonnull RegistUser registUser) {

		EntityDao.super.delete(manager, registUser);
	}
}
