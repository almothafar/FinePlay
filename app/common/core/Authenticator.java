package common.core;

import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import common.system.SessionKeys;
import models.user.User_;
import play.mvc.Http.Request;
import play.mvc.Http.Session;
import play.mvc.Result;
import play.mvc.Security;

public class Authenticator extends Security.Authenticator {

	@Nullable
	@Override
	public Optional<String> getUsername(Request req) {

		final Optional<String> userIdOpt = req.session().getOptional(User_.USER_ID);

		return userIdOpt;
	}

	@SuppressWarnings("null")
	@Nonnull
	@Override
	public Result onUnauthorized(Request req) {

		String requestUrl = req.uri();
		if (requestUrl == null) {

			requestUrl = "/";
		}

		return redirect(controllers.user.routes.User.index())//
				.withSession(new Session(Map.of(//
						SessionKeys.REQUEST_URL, requestUrl//
				)));
	}
}
