package common.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import com.typesafe.config.Config;

public class Offices {

	private Offices() {
	}

	@Inject
	private static Config config;

	@Nonnull
	public static byte[] toPDF(@Nonnull final Path inputPath) {

		return toFormat("pdf", inputPath);
	}

	@Nonnull
	public static byte[] toFodt(@Nonnull final Path inputPath) {

		return toFormat("fodt", inputPath);
	}

	@SuppressWarnings("null")
	@Nonnull
	private static byte[] toFormat(@Nonnull final String extension, @Nonnull final Path inputPath) {

		Objects.requireNonNull(extension);
		Objects.requireNonNull(inputPath);

		final Path commandPath = getCommandPath();
		final Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"));
		final Path outputDirPath = tmpPath.resolve(UUID.randomUUID().toString());
		final String withoutExt = com.google.common.io.Files.getNameWithoutExtension(inputPath.getName(inputPath.getNameCount() - 1).toString());
		final Path outputPath = outputDirPath.resolve(withoutExt + "." + extension);

		final List<String> command = new ArrayList<>();
		command.add(commandPath.toString());

		command.add("--headless");
		command.add("--nologo");
		command.add("--nofirststartwizard");
		command.add("--convert-to");
		command.add(extension);
		command.add(inputPath.toString());
		command.add("--outdir");
		command.add(outputDirPath.toString());

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

				Files.deleteIfExists(outputDirPath);
			} catch (IOException e) {
			}
		}
	}

	private static Path getCommandPath() {

		String commandPathString = null;
		if (Objects.nonNull(config)) {

			commandPathString = config.getString("libreoffice.command_path");
		}

		if (Objects.isNull(commandPathString)) {

			final String os = System.getProperty("os.name").toLowerCase();
			if (os.startsWith("mac")) {

				commandPathString = "/Applications/LibreOffice.app/Contents/MacOS/soffice";
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
