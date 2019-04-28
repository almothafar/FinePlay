package common.core;

import java.util.HashMap;
import java.util.Objects;
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

		final String requestUrl = getRequestURI(req);

		return redirect(controllers.user.routes.User.index())//
				.withSession(new Session(new HashMap<String, String>() {
					{//
						put(SessionKeys.REQUEST_URL, requestUrl);//
					}
				}));
	}

	private String getRequestURI(Request req) {

		String requestUrl = req.uri();
		if (Objects.isNull(requestUrl)) {

			requestUrl = "/";
		}
		return requestUrl;
	}
}
