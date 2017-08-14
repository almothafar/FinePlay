package test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Captures {

	public static Path getFolderPath() {

		final StackTraceElement element = Thread.currentThread().getStackTrace()[2];

		final List<String> pathNames = new ArrayList<>();
		pathNames.add("test-screenshot");
		pathNames.add(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
		pathNames.addAll(Arrays.asList(element.getClassName().split("\\.")));
		pathNames.add(element.getMethodName());

		return Paths.get("target", pathNames.toArray(new String[0]));
	}
}
