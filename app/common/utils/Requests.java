package common.utils;

import javax.annotation.Nonnull;

import play.mvc.Http;
import play.mvc.Http.Request;

public class Requests {

	private Requests() {
	}

	public static final boolean isAjax(@Nonnull final Request request) {

		// Call of jQuery only.
		final boolean isAJax = request.getHeaders().contains(Http.HeaderNames.X_REQUESTED_WITH) && "XMLHttpRequest".equals(request.getHeaders().get(Http.HeaderNames.X_REQUESTED_WITH).get());
		return isAJax;
	}
}
