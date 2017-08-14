package controllers.user;

import javax.security.auth.login.AccountException;

import com.google.inject.ImplementedBy;

import play.i18n.Lang;

@ImplementedBy(DefaultUserAuthenticator.class)
interface UserAuthenticator {

	models.user.User signIn(Lang lang, String userId, String password) throws AccountException;

	models.user.User confirm(Lang lang, String userId, String password) throws AccountException;

	models.user.User signOut(Lang lang, String userId) throws AccountException;
}