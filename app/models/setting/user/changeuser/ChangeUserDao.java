package models.setting.user.changeuser;

import java.util.function.BiConsumer;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import models.base.EntityDao;

public class ChangeUserDao implements EntityDao<ChangeUser> {

	@Override
	public void create(EntityManager manager, ChangeUser changeUser) {

		EntityDao.super.create(manager, changeUser);
	}

	public ChangeUser read(EntityManager manager, BiConsumer<CriteriaBuilder, CriteriaQuery<ChangeUser>> condition) {

		return EntityDao.super.read(manager, ChangeUser.class, condition);
	}

	@Override
	public void delete(EntityManager manager, ChangeUser registUser) {

		EntityDao.super.delete(manager, registUser);
	}
}
