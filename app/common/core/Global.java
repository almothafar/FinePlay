package common.core;

import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.jberet.operations.JobOperatorImpl;
import org.jberet.spi.JobOperatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.core.batch.BatchEnvironment;
import play.inject.ApplicationLifecycle;

@Singleton
public class Global {

	private static final Logger LOGGER = LoggerFactory.getLogger(Global.class);

	@Inject
	public Global(ApplicationLifecycle lifecycle) {

		LOGGER.info("onStart");

		LOGGER.info(createGlobalMessage());

		final String format = StringUtils.leftPad("Server TimeZone(UTC)", 25, ' ') + ": {}";
		final boolean isUTC = ZoneId.of("UTC").equals(ZoneId.systemDefault());
		final Runnable logging = isUTC ? () -> LOGGER.info(format, isUTC) : () -> LOGGER.error(format, isUTC);
		logging.run();

		JobOperatorContext.setJobOperatorContextSelector(() -> JobOperatorContext.create(new JobOperatorImpl(new BatchEnvironment())));

		lifecycle.addStopHook(() -> {

			LOGGER.info("onStop");

			return CompletableFuture.completedFuture(null);
		});
	}

	private static String createGlobalMessage() {

		final StringBuilder builder = new StringBuilder();

		builder.append("---------- Global").append("\n");

		final Map<String, Object> info = new HashedMap<>();
		info.put("SecurityManager", System.getSecurityManager());

		final String globalContents = info.entrySet().stream().map(entry -> {

			final String key = StringUtils.leftPad(entry.getKey(), 25, ' ');
			final String value = "" + entry.getValue();

			return key + ": " + value;
		}).collect(Collectors.joining("\n"));
		builder.append(globalContents);

		return builder.toString();
	}
}
