package common.system;

import javax.inject.Inject;
import javax.inject.Provider;

import common.core.image.Image;
import play.Application;
import play.inject.Injector;

public class System {

	@Inject
	private static Provider<Application> applicationProvider;
//
//	private static Injector INJECTOR;

	@Deprecated
	public static Injector getInjector() {

		final Application application = applicationProvider.get();
		return application.injector();
//		return INJECTOR;
	}
//
//	@Deprecated
//	public static void setInjector(final Injector injector) {
//
//		INJECTOR = injector;
//	}

	@Inject
	private static Image signInImage;

	public static Image getImage() {

		return signInImage;
	}
}
