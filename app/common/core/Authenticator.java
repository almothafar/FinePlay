package common.core;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import common.system.SessionKeys;
import models.user.User_;
import play.mvc.Http.Context;
import play.mvc.Http.Session;
import play.mvc.Result;
import play.mvc.Security;

public class Authenticator extends Security.Authenticator {

	@Nullable
	@Override
	public String getUsername(@Nonnull final Context ctx) {

		final String userId = ctx.request().session().get(User_.USER_ID);

		return userId;
	}

	@SuppressWarnings("null")
	@Nonnull
	@Override
	public Result onUnauthorized(@Nonnull final Context ctx) {

		String requestUrl = ctx.request().uri();
		if (requestUrl == null) {

			requestUrl = "/";
		}

		return redirect(controllers.user.routes.User.index())//
				.withSession(new Session(Map.of(//
						SessionKeys.REQUEST_URL, requestUrl//
				)));
	}
}
