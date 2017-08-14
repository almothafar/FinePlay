package common.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import com.typesafe.config.Config;

public class PDFs {

	private PDFs() {
	}

	@Inject
	private static Config config;

	@SuppressWarnings("null")
	@Nonnull
	public static byte[] toPDF(@Nonnull final Path inputPath, @Nonnull final List<String> arguments) {

		return toPDF(inputPath.toString(), arguments);
	}

	@SuppressWarnings("null")
	@Nonnull
	public static byte[] toPDF(@Nonnull final URI inputUri, @Nonnull final List<String> arguments) {

		return toPDF(inputUri.toString(), arguments);
	}

	@SuppressWarnings("null")
	@Nonnull
	private static byte[] toPDF(@Nonnull final String input, @Nonnull final List<String> arguments) {

		final Path commandPath = getCommandPath();
		final Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"));
		final Path outputPath = tmpPath.resolve(Strings.randomAlphanumeric(16) + ".pdf");

		final List<String> command = new ArrayList<>();
		command.add(commandPath.toString());
		command.addAll(arguments);
		command.add(input);
		command.add(outputPath.getFileName().toString());

		final ProcessBuilder builder = new ProcessBuilder(command);
		builder.directory(tmpPath.toFile());

		try {

			final Process process = builder.start();
			final boolean isSuccess = process.waitFor(30, TimeUnit.SECONDS);
			if (isSuccess) {

				final byte[] bytes = Files.readAllBytes(outputPath);
				return bytes;
			} else {

				process.destroy();
				throw new IllegalStateException("" + process.exitValue());
			}
		} catch (IOException e) {

			throw new UncheckedIOException(e);
		} catch (InterruptedException e) {

			throw new RuntimeException(e);
		} finally {

			try {

				Files.deleteIfExists(outputPath);
			} catch (IOException e) {
			}
		}
	}

	private static Path getCommandPath() {

		String commandPathString = null;
		if (Objects.nonNull(config)) {

			commandPathString = config.getString("wkhtmltopdf.command_path");
		}

		if (Objects.isNull(commandPathString)) {

			final String os = System.getProperty("os.name").toLowerCase();
			if (os.startsWith("mac")) {

				commandPathString = "/usr/local/bin/wkhtmltopdf";
			} else if (os.startsWith("linux")) {

				throw new RuntimeException(":" + os);
			} else if (os.startsWith("windows")) {

				throw new RuntimeException(":" + os);
			} else {

				throw new RuntimeException(":" + os);
			}
		}

		final Path commandPath = Paths.get(commandPathString);

		Objects.requireNonNull(commandPath);

		return commandPath;
	}
}
