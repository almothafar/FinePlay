package common.system;

import javax.inject.Inject;
import javax.inject.Provider;

import common.core.image.Image;
import play.Application;
import play.inject.Injector;

public class System {

	@Inject
	private static Provider<Application> applicationProvider;

	@Deprecated
	public static Injector getInjector() {

		final Application application = applicationProvider.get();
		return application.injector();
	}

	@Inject
	private static Image signInImage;

	public static Image getImage() {

		return signInImage;
	}
}
