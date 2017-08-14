package common.system;

import javax.inject.Inject;

import common.core.image.Image;
import play.inject.Injector;

public class System {

	private static Injector INJECTOR;

	@Deprecated
	public static Injector getInjector() {

		return INJECTOR;
	}

	@Deprecated
	public static void setInjector(final Injector injector) {

		INJECTOR = injector;
	}

	@Inject
	private static Image signInImage;

	public static Image getImage() {

		return signInImage;
	}
}
