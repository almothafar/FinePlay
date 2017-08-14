package custom.core.module;

import common.core.image.Image;
import custom.core.image.UnsplashItImage;

public class GlobalModule extends common.core.module.GlobalModule {

	@Override
	protected void configure() {

		super.configure();

		bind(Image.class).to(UnsplashItImage.class);
	}
}
