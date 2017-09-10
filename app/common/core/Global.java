package common.core;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.inject.ApplicationLifecycle;
import play.inject.Injector;

@Singleton
public class Global {

	private static final Logger LOGGER = LoggerFactory.getLogger(Global.class);

	@SuppressWarnings("deprecation")
	@Inject
	public Global(ApplicationLifecycle lifecycle, Injector injector) {

		LOGGER.info("onStart");

//		common.system.System.setInjector(injector);

		lifecycle.addStopHook(() -> {

			LOGGER.info("onStop");

			return CompletableFuture.completedFuture(null);
		});
	}
}
