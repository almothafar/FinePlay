package common.tools;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;

class ImageMetadataRemover {

	public static void main(String[] args) throws Exception {

		Files.list(Paths.get(".", "public", "images", "signins")).forEach(path -> {

			try {

				System.out.println("Start remove...: " + path.toString());
				removeMetadata(path);
				System.out.println("Metadata removed.");
			} catch (IOException e) {

				throw new UncheckedIOException(e);
			}
		});
	}

	private static void removeMetadata(@Nonnull final Path path) throws IOException {

		final String name = path.getName(path.getNameCount() - 1).toString();
		if (".DS_Store".equals(name)) {

			return;
		}
		final BufferedImage image = ImageIO.read(path.toFile());
		final String ext = com.google.common.io.Files.getFileExtension(path.getName(path.getNameCount() - 1).toString());
		ImageIO.write(image, ext, path.toFile());
	}
}
