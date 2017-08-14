package models.registuser;

import java.util.function.BiConsumer;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import models.base.EntityDao;

public class RegistUserDao implements EntityDao<RegistUser> {

	@Override
	public void create(EntityManager manager, RegistUser registUser) {

		EntityDao.super.create(manager, registUser);
	}

	public RegistUser read(EntityManager manager, BiConsumer<CriteriaBuilder, CriteriaQuery<RegistUser>> condition) {

		return EntityDao.super.read(manager, RegistUser.class, condition);
	}

	@Override
	public void delete(EntityManager manager, RegistUser registUser) {

		EntityDao.super.delete(manager, registUser);
	}
}
