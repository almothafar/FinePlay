package custom.core.module;

import com.typesafe.config.Config;

import common.core.image.Image;
import custom.core.image.LoremPicsumImage;
import play.Environment;

public class GlobalModule extends common.core.module.GlobalModule {

	public GlobalModule(final Environment environment, final Config config) {

		super(environment, config);
	}

	@Override
	protected void configure() {

		super.configure();

		bind(Image.class).to(LoremPicsumImage.class);
	}
}
