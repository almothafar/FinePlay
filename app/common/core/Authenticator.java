package common.core;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import common.system.SessionKeys;
import models.user.User;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class Authenticator extends Security.Authenticator {

	@Nullable
	@Override
	public String getUsername(@Nonnull final Context ctx) {

		final String userId = ctx.session().get(User.USERID);

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

		ctx.session().put(SessionKeys.REQUEST_URL, requestUrl);

		return redirect(controllers.user.routes.User.index());
	}
}
