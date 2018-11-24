package controllers.user;

import javax.persistence.EntityManager;
import javax.security.auth.login.AccountException;

import com.google.inject.ImplementedBy;

import models.user.User;
import play.i18n.Messages;

@ImplementedBy(DefaultUserService.class)
public interface UserService {

	boolean isExist(EntityManager manager, Messages messages, String userId);

	void create(EntityManager manager, Messages messages, User user) throws AccountException;

	User read(EntityManager manager, Messages messages, String userId) throws AccountException;

	User update(EntityManager manager, Messages messages, User user) throws AccountException;

	void delete(EntityManager manager, Messages messages, User user) throws AccountException;
}