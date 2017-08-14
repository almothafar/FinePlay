package common.core.module;

import com.google.inject.AbstractModule;

import common.core.Global;

public class GlobalModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(Global.class).asEagerSingleton();

		requestStaticInjection(common.system.System.class);
		requestStaticInjection(common.utils.PDFs.class);
	}
}
