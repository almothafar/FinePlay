package common.tools;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class ZoneIDModifyChecker {

	private static final String BEFORE_JDK = "11.0.4";

	public static void main(String[] args) throws Exception {

		final Path path = ZoneIDListCreator.getFilePath(BEFORE_JDK);
		final List<String> beforeZoneIDs = Files.readAllLines(path, StandardCharsets.UTF_8);
		final List<String> currentZoneIDs = ZoneIDListCreator.getZoneIDs();

		final List<String> deletedZoneIDs = beforeZoneIDs.stream().filter(beforeZoneID -> -1 == currentZoneIDs.indexOf(beforeZoneID)).collect(Collectors.toList());
		System.out.println("========== Deleted =========");
		System.out.println(String.join("\n", deletedZoneIDs));
		if (0 != deletedZoneIDs.size()) {

			System.out.println("USERS.zoneId column update is required.");
		}

		final List<String> addedZoneIDs = currentZoneIDs.stream().filter(currentZoneID -> -1 == beforeZoneIDs.indexOf(currentZoneID)).collect(Collectors.toList());
		System.out.println("========== Added =========");
		System.out.println(String.join("\n", addedZoneIDs));
	}
}
