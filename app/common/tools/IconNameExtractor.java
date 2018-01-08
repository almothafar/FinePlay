package common.tools;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class IconNameExtractor {

	public static void main(String[] args) throws Exception {

		final List<String> fontAwesomeIconNames = getFontAwesomeIconNames();
		final Path fontAwesome = Paths.get(".", "conf", "resources", "development", "design", "icon", "font-awesome.txt");
		Files.write(fontAwesome, fontAwesomeIconNames, StandardCharsets.UTF_8);
		System.out.println("Created :" + fontAwesome);

		final List<String> materialDesignIconNames = getMaterialDesignIconNames();
		final Path materialDesignIcons = Paths.get(".", "conf", "resources", "development", "design", "icon", "material-design.txt");
		Files.write(materialDesignIcons, materialDesignIconNames, StandardCharsets.UTF_8);
		System.out.println("Created :" + materialDesignIcons);

		final List<String> icoFontNames = getIcoFontNames();
		final Path icoFont = Paths.get(".", "conf", "resources", "development", "design", "icon", "icofont.txt");
		Files.write(icoFont, icoFontNames, StandardCharsets.UTF_8);
		System.out.println("Created :" + icoFont);

		final List<String> emojiOneAwesomeIconNames = getEmojiOneAwesomeIconNames();
		final Path emojiOneAwesome = Paths.get(".", "conf", "resources", "development", "design", "icon", "emojione.txt");
		Files.write(emojiOneAwesome, emojiOneAwesomeIconNames, StandardCharsets.UTF_8);
		System.out.println("Created :" + emojiOneAwesome);
	}

	private static final Pattern PATTERN_FONTAWESOME = Pattern.compile("\\.fa-(?<iconName>.*):before.*");

	private static List<String> getFontAwesomeIconNames() throws IOException {

		final Path path = Paths.get(".", "target", "web", "public", "main", "lib", "fontawesome-free", "web-fonts-with-css", "css", "fontawesome-all.css");

		if (!Files.exists(path)) {

			throw new RuntimeException(": " + path);
		}

		final List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		final List<String> faLines = lines.stream().filter(line -> line.startsWith(".fa-")).collect(Collectors.toList());
		final int firstIconIndex = faLines.indexOf(".fa-500px:before {");
		final List<String> faIconLines = faLines.subList(firstIconIndex, faLines.size());
		final List<String> iconNames = faIconLines.stream().map(line -> {

			final Matcher matcher = PATTERN_FONTAWESOME.matcher(line);
			if (!matcher.matches()) {

				throw new IllegalStateException("");
			}

			final String iconName = matcher.group("iconName");

			return iconName;
		}).collect(Collectors.toList());

		return iconNames;
	}

	private static List<String> getMaterialDesignIconNames() throws IOException {

		final Path path = Paths.get(".", "target", "web", "public", "main", "lib", "material-design-icons", "codepoints");
		if (!Files.exists(path)) {

			throw new RuntimeException(": " + path);
		}

		final List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		final List<String> iconNames = lines.stream().map(line -> line.split("\\s")[0]).collect(Collectors.toList());

		return iconNames;
	}

	private static final Pattern PATTERN_ICOFONT = Pattern.compile("\\.icofont-(?<iconName>.*):before.*");

	private static List<String> getIcoFontNames() throws IOException {

		final Path path = Paths.get(".", "public", "lib", "icofont", "css", "icofont.css");
		if (!Files.exists(path)) {

			throw new RuntimeException(": " + path);
		}

		final List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		final List<String> icoFontLines = lines.stream().filter(line -> line.startsWith(".icofont-")).collect(Collectors.toList());
		final int firstIconIndex = icoFontLines.indexOf(".icofont-angry-monster:before {");
		final int lastIconIndex = icoFontLines.indexOf(".icofont-brand-xiaomi:before {");
		final List<String> icoFontIconLines = icoFontLines.subList(firstIconIndex, lastIconIndex + 1);
		final List<String> iconNames = icoFontIconLines.stream().map(line -> {

			final Matcher matcher = PATTERN_ICOFONT.matcher(line);
			if (!matcher.matches()) {

				throw new IllegalStateException("");
			}

			final String iconName = matcher.group("iconName");

			return iconName;
		}).collect(Collectors.toList());

		return iconNames;
	}

	private static final Pattern PATTERN_EMOJIONEAWESOME = Pattern.compile("\\.e1a-(?<iconName>.*)\\s.*");

	private static List<String> getEmojiOneAwesomeIconNames() throws IOException {

		final Path path = Paths.get(".", "target", "web", "public", "main", "lib", "emojione", "extras", "css", "emojione-awesome.css");

		if (!Files.exists(path)) {

			throw new RuntimeException(": " + path);
		}

		final List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		final List<String> e1aLines = lines.stream().filter(line -> line.startsWith(".e1a-")).collect(Collectors.toList());
		final int firstIconIndex = e1aLines.indexOf(".e1a-interrobang {");
		final List<String> e1aIconLines = e1aLines.subList(firstIconIndex, e1aLines.size());
		final List<String> iconNames = e1aIconLines.stream().map(line -> {

			final Matcher matcher = PATTERN_EMOJIONEAWESOME.matcher(line);
			if (!matcher.matches()) {

				throw new IllegalStateException("");
			}

			final String iconName = matcher.group("iconName");

			return iconName;
		}).collect(Collectors.toList());

		return iconNames;
	}
}
