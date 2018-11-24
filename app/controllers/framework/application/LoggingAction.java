package controllers.framework.application;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Action.Simple;
import play.mvc.Http.Request;
import play.mvc.Result;

class LoggingAction extends Simple {

	private final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public CompletionStage<Result> call(Request req) {

		LOGGER.info("Annotation Logging.");

		LOGGER.trace("method={} uri={} remote-address={}", req.method(), req.uri(), req.remoteAddress());
		LOGGER.debug("method={} uri={} remote-address={}", req.method(), req.uri(), req.remoteAddress());
		LOGGER.info("method={} uri={} remote-address={}", req.method(), req.uri(), req.remoteAddress());
		LOGGER.warn("method={} uri={} remote-address={}", req.method(), req.uri(), req.remoteAddress());
		LOGGER.error("method={} uri={} remote-address={}", req.method(), req.uri(), req.remoteAddress());
		return delegate.call(req);
	}
}
