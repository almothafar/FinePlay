package common.core;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Provider;

import com.typesafe.config.Config;
import play.Environment;
import play.Logger;
import play.Logger.ALogger;
import play.Mode;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Action;
import play.mvc.Controller;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

public class ErrorHandler extends DefaultHttpErrorHandler {

	private static final ALogger LOGGER = Logger.of(ErrorHandler.class);

	@Inject
	private Config config;

	@Inject
	private views.html.error.error error;

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

		final String userId = Controller.session(models.user.User.USERID);
		if (userId == null) {

			return CompletableFuture.completedFuture(Action.redirect(controllers.user.routes.User.index()));
		}

		return CompletableFuture.completedFuture(Results.notFound(views.html.error.notfound.render(request.method(), request.uri())));
	}

	@Override
	public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {

		return super.onServerError(request, exception);
	}

	@Override
	protected CompletionStage<Result> onDevServerError(RequestHeader request, UsefulException exception) {

		final Mode errorHandlerMode = Mode.valueOf(config.hasPath("errorHandler.mode") ? config.getString("errorHandler.mode") : Mode.DEV.name());
		switch (errorHandlerMode) {

			case PROD :

				return onProdServerError(request, exception);

			default :

				return super.onDevServerError(request, exception);
		}
	}

	@Override
	protected CompletionStage<Result> onProdServerError(RequestHeader request, UsefulException exception) {

		final String userId = Controller.session(models.user.User.USERID);
		if (userId == null) {

			return super.onProdServerError(request, exception);
		}

		return CompletableFuture.completedFuture(Results.internalServerError(error.render(exception)));
	}
}
