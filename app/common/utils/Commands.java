package common.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

public class Commands {

	private Commands() {
	}

	public static void exec(@Nonnull final Path execPath, @Nonnull final List<String> command, @Nonnull final Duration wait) {

		exec(Collections.emptyMap(), execPath, command, wait);
	}

	private static void exec(@Nonnull final Map<String, String> environment, @Nonnull final Path execPath, @Nonnull final List<String> command, @Nonnull final Duration wait) {

		final ProcessBuilder builder = new ProcessBuilder(command);
		builder.environment().putAll(environment);
		builder.directory(execPath.toFile());

		try {

			final Process process = builder.start();
			final boolean isSuccess = process.waitFor(wait.toSeconds(), TimeUnit.SECONDS);
			if (!isSuccess) {

				process.destroy();
				throw new IllegalStateException("" + process.exitValue());
			}
		} catch (IOException e) {

			throw new UncheckedIOException(e);
		} catch (InterruptedException e) {

			throw new RuntimeException(e);
		}
	}
}