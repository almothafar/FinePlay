package common.tools;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class SQLToScriptConverter {

	public static void main(String[] args) throws IOException {

		final List<Path> paths = createSQLPaths();

		for (final Path path : paths) {

			if (!Files.exists(path)) {

				throw new RuntimeException(": " + path);
			}

			final List<String> sQLLines = Files.readAllLines(path, StandardCharsets.UTF_8);

			makeToSQLScript(path, sQLLines);
		}
	}

	private static List<Path> createSQLPaths() {

		final ArrayList<Path> sQLPaths = new ArrayList<>();
		//
		sQLPaths.add(Paths.get(".", "prod", "create.sql"));
		sQLPaths.add(Paths.get(".", "prod", "drop.sql"));
		sQLPaths.add(Paths.get(".", "prod", "load.sql"));

		return Collections.unmodifiableList(sQLPaths);
	}

	private static void makeToSQLScript(final Path sQLPath, final List<String> sQLLines) throws IOException {

		final Path sQLScriptPath = sQLPath.getParent().resolve(sQLPath.getFileName().toString().replaceFirst("\\.sql$", "-script.sql"));
		final List<String> sQLScriptLines = sQLLines.stream().map(sQLLine -> sQLLine + ";").collect(Collectors.toList());

		Files.write(sQLScriptPath, sQLScriptLines, StandardCharsets.UTF_8);
	}
}
