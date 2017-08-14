package controllers.user;

import javax.persistence.EntityManager;
import javax.security.auth.login.AccountException;

import com.google.inject.ImplementedBy;

import models.user.User;

@ImplementedBy(DefaultUserService.class)
public interface UserService {

	boolean isExist(EntityManager manager, String userId);

	void create(EntityManager manager, User user) throws AccountException;

	User read(EntityManager manager, String userId) throws AccountException;

	User update(EntityManager manager, User user) throws AccountException;

	void delete(EntityManager manager, User user) throws AccountException;
}