package common.core.image;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import org.apache.commons.lang3.RandomUtils;

import common.core.ActionCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DefaultImage implements Image {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActionCreator.class);

	private static final Pattern IMAGE = Pattern.compile(".+\\.(png|jpg|jpeg)$", Pattern.CASE_INSENSITIVE);

	@Override
	@Nonnull
	public String getName() {

		return "Sign in image";
	}

	@Override
	@Nonnull
	public URI getLink() {

		try {

			final List<Path> imagePaths = Files.list(Paths.get(".", "public", "images", "signins")).filter(path -> {

				final String fileName = path.getFileName().toString();
				return IMAGE.matcher(fileName).matches();
			}).collect(Collectors.toList());
			LOGGER.info("Sign in images: {}", imagePaths);

			if (!(1 <= imagePaths.size())) {

				throw new IllegalStateException("Sign in image not found.");
			}

			final Path path = imagePaths.get(RandomUtils.nextInt(0, imagePaths.size()));

			return new URI("/assets/images/signins/" + path.getFileName());
		} catch (IOException | URISyntaxException e) {

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

				return "hiro20v";
			}

			@Override
			@Nonnull
			public URI getLink() {

				try {

					return new URI("https://github.com/hiro20v");
				} catch (URISyntaxException e) {

					throw new RuntimeException(e);
				}
			}
		};
	}
}
