package common.core;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import play.Environment;
import play.Mode;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.i18n.Lang;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

public class ErrorHandler extends DefaultHttpErrorHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private Config config;

	@Inject
	private MessagesApi messagesApi;

	@Inject
	private views.html.system.pages.error error;

	@Inject
	public ErrorHandler(Config config, Environment environment, OptionalSourceMapper sourceMapper, Provider<Router> routes) {

		super(config, environment, sourceMapper, routes);
	}

	@Override
	public CompletionStage<Result> onClientError(RequestHeader request, int statusCode, String message) {

		final String path = request.path();
		if (path != null && path.endsWith("/")) {

			return CompletableFuture.completedFuture(Results.redirect(path.substring(0, path.length() - 1)));
		}

		return super.onClientError(request, statusCode, message);
	}

	@Override
	protected CompletionStage<Result> onNotFound(RequestHeader request, String message) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final Optional<String> userIdOpt = request.session().get(models.user.User_.USER_ID);
		if (!userIdOpt.isPresent()) {

			return CompletableFuture.completedFuture(Results.redirect(controllers.user.routes.User.index()));
		}

		return CompletableFuture.completedFuture(Results.notFound(views.html.system.pages.notfound.render(request.method(), request.uri(), request, lang, messages)));
	}

	@Override
	protected CompletionStage<Result> onForbidden(RequestHeader request, String message) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return CompletableFuture.completedFuture(Results.forbidden(views.html.system.pages.unauthorized.render(request, lang, messages)));
	}

	@Override
	public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {

		return super.onServerError(request, exception);
	}

	@Override
	protected CompletionStage<Result> onDevServerError(RequestHeader request, UsefulException exception) {

		final Mode errorHandlerMode = Mode.valueOf(config.hasPath("errorHandler.mode") ? config.getString("errorHandler.mode") : Mode.DEV.name());
		switch (errorHandlerMode) {

		case PROD:

			return onProdServerError(request, exception);

		default:

			return super.onDevServerError(request, exception);
		}
	}

	@Override
	protected CompletionStage<Result> onProdServerError(RequestHeader request, UsefulException exception) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final Optional<String> userIdOpt = request.session().get(models.user.User_.USER_ID);
		if (!userIdOpt.isPresent()) {

			return super.onProdServerError(request, exception);
		}

		return CompletableFuture.completedFuture(Results.internalServerError(error.render(exception, request, lang, messages)));
	}
}
