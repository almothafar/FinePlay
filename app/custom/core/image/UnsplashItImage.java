package custom.core.image;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import common.core.image.Image;

@Singleton
public class UnsplashItImage implements Image {

	@Override
	@Nonnull
	public String getName() {

		return "Unsplash It";
	}

	@Override
	@Nonnull
	public URI getLink() {

		try {

			return new URI("https://unsplash.it/1600/1200/?random");
		} catch (URISyntaxException e) {

			throw new RuntimeException(e);
		}
	}

	@Override
	@Nonnull
	public Credit getCredit() {

		return new Credit() {

			@Override
			@Nonnull
			public String getName() {

				return "DigitalOcean";
			}

			@Override
			@Nonnull
			public URI getLink() {

				try {

					return new URI("https://unsplash.it");
				} catch (URISyntaxException e) {

					throw new RuntimeException(e);
				}
			}
		};
	}
}
