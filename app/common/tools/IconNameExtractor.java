package common.tools;

import java.awt.Font;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class IconNameExtractor {

	public static void main(String[] args) throws Exception {

		final List<String> fontAwesomeSolidIconNames = getFontAwesomeIconNames("fas");
		final Path fontAwesomeSolid = Paths.get(".", "conf", "resources", "development", "design", "icon", "font-awesome-solid.txt");
		Files.write(fontAwesomeSolid, fontAwesomeSolidIconNames, StandardCharsets.UTF_8);
		System.out.println("Created :" + fontAwesomeSolid);

		final List<String> fontAwesomeRegularIconNames = getFontAwesomeIconNames("far");
		final Path fontAwesomeRegular = Paths.get(".", "conf", "resources", "development", "design", "icon", "font-awesome-regular.txt");
		Files.write(fontAwesomeRegular, fontAwesomeRegularIconNames, StandardCharsets.UTF_8);
		System.out.println("Created :" + fontAwesomeRegular);

		final List<String> fontAwesomeBrandsRegularIconNames = getFontAwesomeIconNames("fab");
		final Path fontAwesomeBrandsRegular = Paths.get(".", "conf", "resources", "development", "design", "icon", "font-awesome-brands-regular.txt");
		Files.write(fontAwesomeBrandsRegular, fontAwesomeBrandsRegularIconNames, StandardCharsets.UTF_8);
		System.out.println("Created :" + fontAwesomeBrandsRegular);

		final List<String> materialDesignIconNames = getMaterialDesignIconNames();
		final Path materialDesignIcons = Paths.get(".", "conf", "resources", "development", "design", "icon", "material-design.txt");
		Files.write(materialDesignIcons, materialDesignIconNames, StandardCharsets.UTF_8);
		System.out.println("Created :" + materialDesignIcons);

		final List<String> ioniconsNames = getIonicons();
		final Path ionicons = Paths.get(".", "conf", "resources", "development", "design", "icon", "ionicons.txt");
		Files.write(ionicons, ioniconsNames, StandardCharsets.UTF_8);
		System.out.println("Created :" + ionicons);

		final List<String> icoFontNames = getIcoFontNames();
		final Path icoFont = Paths.get(".", "conf", "resources", "development", "design", "icon", "icofont.txt");
		Files.write(icoFont, icoFontNames, StandardCharsets.UTF_8);
		System.out.println("Created :" + icoFont);

		final List<String> twemojiNames = getTwemojiNames();
		final Path twemojis = Paths.get(".", "conf", "resources", "development", "design", "icon", "twemoji-awesome.txt");
		Files.write(twemojis, twemojiNames, StandardCharsets.UTF_8);
		System.out.println("Created :" + twemojis);
	}

	private static final Pattern PATTERN_FONTAWESOME = Pattern.compile("\\.fa-(?<iconName>.*):before.*");
	private static final Pattern PATTERN_FONTAWESOME_HEX = Pattern.compile(".*content:.*\"\\\\(?<iconHex>.*)\".*");

	private static List<String> getFontAwesomeIconNames(final String style) throws IOException {

		final Path path = Paths.get(".", "target", "web", "public", "main", "lib", "font-awesome", "css", "all.css");

		if (!Files.exists(path)) {

			throw new RuntimeException(": " + path);
		}

		final Font font = createFont(style);

		String tmp = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
		tmp = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL).matcher(tmp).replaceAll("");
		tmp = tmp.replaceAll("\\n", "");
		tmp = tmp.replaceAll("\\{", "{\n");
		tmp = tmp.replaceAll("\\}", "}\n");
		final List<String> lines = Arrays.asList(tmp.split("\n"));
		final List<String> iconNames = new ArrayList<>();
		boolean isFirstIcon = false;
		for (int i = 0; i < lines.size(); i++) {

			final String line = lines.get(i);

			if (!isFirstIcon) {

				isFirstIcon = ".fa-500px:before{".equals(line);

				if (!isFirstIcon) {

					continue;
				}
			}

			if (line.startsWith(".fa-")) {

				final Matcher matcher = PATTERN_FONTAWESOME.matcher(line);
				if (!matcher.matches()) {

					throw new IllegalStateException(line);
				}
				final String iconName = matcher.group("iconName");

				final String nextLine = lines.get(i + 1);
				final Matcher nextMatcher = PATTERN_FONTAWESOME_HEX.matcher(nextLine);
				if (!nextMatcher.matches()) {

					throw new IllegalStateException(nextLine);
				}
				final String iconHex = nextMatcher.group("iconHex");
				final int codePoint = Integer.valueOf(iconHex, 16);

				if (font.canDisplay(codePoint)) {

					iconNames.add(iconName);
				}
			}
		}

		return iconNames;
	}

	private static Font createFont(final String style) {

		final String name;
		switch (style) {
		case "fas":

			name = "FontAwesome5FreeSolid";
			break;
		case "far":

			name = "FontAwesome5FreeRegular";
			break;
		case "fab":

			name = "FontAwesome5BrandsRegular";
			break;
		default:

			throw new RuntimeException(": " + style);
		}

		// need install.
		return new Font(name, Font.PLAIN, 16);
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

	private static final Pattern PATTERN_IONICONS = Pattern.compile("\\.ion-(?<iconName>.*):before.*");

	private static List<String> getIonicons() throws IOException {

		final Path path = Paths.get(".", "target", "web", "public", "main", "lib", "ionicons", "dist", "css", "ionicons-core.css");
		if (!Files.exists(path)) {

			throw new RuntimeException(": " + path);
		}

		final List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		final List<String> ioniconsLines = lines.stream().filter(line -> line.startsWith(".ion-")).collect(Collectors.toList());
		final List<String> iconNames = ioniconsLines.stream().map(line -> {

			final Matcher matcher = PATTERN_IONICONS.matcher(line);
			if (!matcher.matches()) {

				throw new IllegalStateException(line);
			}

			final String iconName = matcher.group("iconName");

			return iconName;
		}).collect(Collectors.toList());

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

				throw new IllegalStateException(line);
			}

			final String iconName = matcher.group("iconName");

			return iconName;
		}).collect(Collectors.toList());

		return iconNames;
	}

	private static final Pattern PATTERN_TWEMOJI = Pattern.compile("\\.ta-(?<iconName>.*)\\s.*");

	private static List<String> getTwemojiNames() throws IOException {

		final Path path = Paths.get(".", "public", "lib", "twemoji-awesome", "twemojiawesome.css");
		if (!Files.exists(path)) {

			throw new RuntimeException(": " + path);
		}

		final List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		final List<String> twemojiLines = lines.stream().filter(line -> line.startsWith(".ta-")).collect(Collectors.toList());
		final List<String> iconNames = twemojiLines.stream().map(line -> {

			final Matcher matcher = PATTERN_TWEMOJI.matcher(line);
			if (!matcher.matches()) {

				throw new IllegalStateException(line);
			}

			final String iconName = matcher.group("iconName");

			return iconName;
		}).collect(Collectors.toList());

		return iconNames;
	}
}
