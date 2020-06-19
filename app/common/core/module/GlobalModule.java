package common.core.module;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import com.google.inject.AbstractModule;
import com.typesafe.config.Config;

import common.core.Global;
import play.Environment;

public class GlobalModule extends AbstractModule {

	private final Environment environment;

	private final Config config;

	public GlobalModule(final Environment environment, final Config config) {

		this.environment = environment;
		this.config = config;
	}

	@Override
	protected void configure() {

		bind(Global.class).asEagerSingleton();

		requestStaticInjection(common.system.System.class);
		requestStaticInjection(common.utils.Themes.class);
		requestStaticInjection(common.utils.Appearances.class);

		bindClock();
	}

	private void bindClock() {

		final Instant instant = Instant.parse(config.hasPath("system.clock") ? config.getString("system.clock") : "2020-07-24T00:00:00.000Z");
		final Clock clock = environment.isTest() ? Clock.fixed(instant, ZoneOffset.UTC) : Clock.systemUTC();

		bind(Clock.class).toInstance(clock);
	}
}
