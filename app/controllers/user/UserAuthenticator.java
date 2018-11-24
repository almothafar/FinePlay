package controllers.user;

import javax.persistence.EntityManager;
import javax.security.auth.login.AccountException;

import com.google.inject.ImplementedBy;

import play.i18n.Messages;

@ImplementedBy(DefaultUserAuthenticator.class)
interface UserAuthenticator {

	models.user.User signIn(EntityManager manager, Messages messages, String userId, String password) throws AccountException;

	models.user.User confirm(EntityManager manager, Messages messages, String userId, String password) throws AccountException;

	models.user.User signOut(EntityManager manager, Messages messages, String userId) throws AccountException;
}