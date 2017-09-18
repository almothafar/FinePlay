package controllers.framework.application;

import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Action.Simple;
import play.mvc.Result;
import play.mvc.Http.Context;
import play.mvc.Http.Request;

class LoggingAction extends Simple {

	private final Logger LOGGER = LoggerFactory.getLogger("access");

	@Override
	public CompletionStage<Result> call(Context ctx) {

		LOGGER.info("Annotation Logging.");

		final Request request = ctx.request();
		LOGGER.trace("method={} uri={} remote-address={}", request.method(), request.uri(), request.remoteAddress());
		LOGGER.debug("method={} uri={} remote-address={}", request.method(), request.uri(), request.remoteAddress());
		LOGGER.info("method={} uri={} remote-address={}", request.method(), request.uri(), request.remoteAddress());
		LOGGER.warn("method={} uri={} remote-address={}", request.method(), request.uri(), request.remoteAddress());
		LOGGER.error("method={} uri={} remote-address={}", request.method(), request.uri(), request.remoteAddress());
		return delegate.call(ctx);
	}
}
