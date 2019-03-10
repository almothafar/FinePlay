package common.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

public class JavadocCreator {

	private static Path PATH_ROOT = Paths.get(".");
	private static Path PATH_DOCUMENT = PATH_ROOT.resolve("document");
	private static Path PATH_JAVADOC = PATH_DOCUMENT.resolve("javadoc");

	public static void main(String[] args) throws Exception {

		FileUtils.deleteDirectory(PATH_JAVADOC.toFile());
		Files.createDirectories(PATH_JAVADOC);

		final ProcessBuilder builder = new ProcessBuilder(getCommand());
		builder.directory(PATH_ROOT.toFile());

		final Process process = builder.start();
		final boolean isSuccess = process.waitFor(30, TimeUnit.SECONDS);
		if (isSuccess) {

		} else {

			process.destroy();
			throw new IllegalStateException("" + process.exitValue());
		}
	}

	private static List<String> getCommand() throws IOException {

		final List<String> command = new ArrayList<>();

		command.add("javadoc");

		command.add("-cp");
		command.add(getClassPaths().stream().map(Path::toString).collect(Collectors.joining(System.getProperty("path.separator"))));

		command.addAll(getSourcePaths().stream().map(Path::toString).collect(Collectors.toList()));

		command.add("-d");
		command.add(PATH_JAVADOC.toString());

		System.out.println(String.join(" ", command));
		return command;
	}

	private static List<Path> getClassPaths() {

		return Arrays.stream(System.getProperty("java.class.path").split(System.getProperty("path.separator"))).map(cp -> Paths.get(cp)).collect(Collectors.toList());
	}

	private static List<Path> getSourcePaths() throws IOException {

		final List<Stream<Path>> outer = Arrays.asList(Files.walk(Paths.get(".", "app")), Files.walk(Paths.get(".", "generate")));
		return outer.stream().flatMap(stream -> stream).filter(path -> path.getFileName().toString().endsWith(".java")).collect(Collectors.toList());
	}
}
