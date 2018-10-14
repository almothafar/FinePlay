package controllers.user;

import javax.persistence.EntityManager;
import javax.security.auth.login.AccountException;

import com.google.inject.ImplementedBy;

import play.i18n.Lang;

@ImplementedBy(DefaultUserAuthenticator.class)
interface UserAuthenticator {

	models.user.User signIn(EntityManager manager, Lang lang, String userId, String password) throws AccountException;

	models.user.User confirm(EntityManager manager, Lang lang, String userId, String password) throws AccountException;

	models.user.User signOut(EntityManager manager, Lang lang, String userId) throws AccountException;
}