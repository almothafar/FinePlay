package common.tools.temp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.UncheckedIOException;

/**
 * Template of migrator
 *
 */
class TemplateOfMigrator {

	private static final Path VIEWS = Paths.get(".", "app", "views");
	private static final Path JAVASCRIPTS = Paths.get(".", "public", "javascripts");

	private static final Path[] REPLACE_PATHS = new Path[] { VIEWS, JAVASCRIPTS };

	private static String[] CHANGES = { //
			"oldSequence,sequence", //
			"oldSequence,sequence" };

	public static void main(String[] args) throws Exception {

		final List<Change> changes = parseChanges();

		replace(changes);

		printNotReplaced();
	}

	private static List<Change> parseChanges() {

		final List<Change> changes = Arrays.stream(CHANGES).map(change -> {
			final String[] columns = change.split(",");
			return new Change(columns[0], columns[1]);
		}).sorted(Comparator.reverseOrder()).collect(Collectors.toList());

		System.out.println("Process order:");
		System.out.println(changes);

		return changes;
	}

	private static void replace(final List<Change> changes) {

		System.out.println("Replacing...");
		Arrays.stream(REPLACE_PATHS).forEach(processPath -> {

			System.out.println("Replacing path :" + processPath);
			try {

				Files.walk(processPath).filter(processFile -> !Files.isDirectory(processFile)).forEach(processFile -> {

					System.out.println("\tReplacing : " + processFile);
					try {

						final String text = new String(Files.readAllBytes(processFile), StandardCharsets.UTF_8);
						String replacedText = text;
						for (final Change change : changes) {

							replacedText = replacedText.replaceAll(change.getOldSequence(), change.getSequence());
						}

						Files.write(processFile, replacedText.getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
					} catch (IOException e) {

						throw new UncheckedIOException(e);
					}
				});
			} catch (IOException e) {

				throw new UncheckedIOException(e);
			}
		});
	}

	private static void printNotReplaced() {

		System.out.println("Checking...");
		Arrays.stream(REPLACE_PATHS).forEach(processPath -> {

			System.out.println("Check path :" + processPath);
			try {

				Files.walk(processPath).filter(processFile -> !Files.isDirectory(processFile)).forEach(processFile -> {

					try {

						final String text = new String(Files.readAllBytes(processFile), StandardCharsets.UTF_8);
						if (text.contains("fa fa-")) {

							System.out.println("\tNot replaced :" + processFile);
						}
					} catch (IOException e) {

						throw new UncheckedIOException(e);
					}
				});
			} catch (IOException e) {

				throw new UncheckedIOException(e);
			}
		});
	}

	private static class Change implements Comparable<Change> {

		private final String oldSequence;
		private final String sequence;

		public Change(final String oldSequence, final String sequence) {

			this.oldSequence = oldSequence;
			this.sequence = sequence;
		}

		public String getOldSequence() {

			return oldSequence;
		}

		public String getSequence() {

			return sequence;
		}

		@Override
		public int compareTo(Change c) {

			int result = Integer.compare(this.getOldSequence().length(), c.getSequence().length());
			if (0 != result) {

				return result;
			}

			return this.getOldSequence().compareTo(c.getOldSequence());
		}

		@Override
		public String toString() {

			return "Change [oldSequence=" + oldSequence + ", sequence=" + sequence + "]";
		}
	}
}
