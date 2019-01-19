package common.tools;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class ZoneIDListCreator {

	static final Path ZONEID_FOLDER = Paths.get(".", "conf", "zoneidlists");

	public static void main(String[] args) throws Exception {

		final Path path = getFilePath(System.getProperty("java.version"));
		final List<String> zoneIDs = getZoneIDs();

		Files.write(path, zoneIDs, StandardCharsets.UTF_8);
	}

	static List<String> getZoneIDs() {

		return ZoneId.getAvailableZoneIds().stream().sorted().collect(Collectors.toList());
	}

	static Path getFilePath(final String jdkVersion) {

		return ZONEID_FOLDER.resolve(getFileName(jdkVersion));
	}

	private static String getFileName(final String jdkVersion) {

		return "zoneidlist-" + jdkVersion + ".txt";
	}
}
