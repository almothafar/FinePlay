package common.tools;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class MessagesCreator {

	private static final int HEAD_ROW_NUM = 0;
	private static List<MessagesInfo> MESSAGES_INFO_LIST = new ArrayList<>();

	public static void main(String[] args) throws InvalidFormatException, IOException {

		final Path messagesSourcePath = Paths.get(".", "conf", "messages.xlsx");
		if (!Files.exists(messagesSourcePath)) {

			throw new RuntimeException("Not exists messages.xlsx : " + messagesSourcePath);
		}

		// https://bz.apache.org/bugzilla/show_bug.cgi?id=62187
		try (final Workbook workbook = new XSSFWorkbook(messagesSourcePath.toFile());) {

			final Sheet sheet = workbook.getSheet("messages");
			if (Objects.isNull(sheet)) {

				throw new RuntimeException("Not exists messages sheet.");
			}

			parseHead(sheet);
			parseBody(sheet);

			MESSAGES_INFO_LIST.forEach(messagesInfo -> messagesInfo.createFile());
		}
	}

	private static void parseHead(final Sheet sheet) {

		final Row headRow = sheet.getRow(HEAD_ROW_NUM);

		final int keyHeadCellNum = 0;
		final int defaultHeadCellNum = keyHeadCellNum + 1;
		final int firstLangHeadCellNum = defaultHeadCellNum + 1;
		final int lastLangHeadCellNum = headRow.getLastCellNum() - 1;

		MESSAGES_INFO_LIST.add(new MessagesInfo(""));

		IntStream.rangeClosed(firstLangHeadCellNum, lastLangHeadCellNum).forEach(langHeadCellNum -> {

			final Cell langHeadCell = headRow.getCell(langHeadCellNum);

			Objects.requireNonNull(langHeadCell);

			final String langName = langHeadCell.getStringCellValue();
			MESSAGES_INFO_LIST.add(new MessagesInfo(langName));
		});
	}

	private static void parseBody(final Sheet sheet) {

		final int firstRowNum = HEAD_ROW_NUM + 1;
		final int lastRowNum = sheet.getLastRowNum();

		final int keyCellNum = 0;
		final int defaultCellNum = keyCellNum + 1;
		final int firstLangCellNum = defaultCellNum + 1;
		final int lastLangCellNum = defaultCellNum + MESSAGES_INFO_LIST.size() - 1;

		IntStream.rangeClosed(firstRowNum, lastRowNum).forEach(rowNum -> {

			final Row row = sheet.getRow(rowNum);

			if (Objects.isNull(row)) {

				MESSAGES_INFO_LIST.stream().forEach(info -> info.addLine(""));
			} else {

				final Cell keyCell = row.getCell(keyCellNum);
				if (Objects.isNull(keyCell)) {

					MESSAGES_INFO_LIST.stream().forEach(info -> info.addLine(""));
				} else {

					final String key;
					switch (keyCell.getCellTypeEnum()) {
					case BLANK:

						key = "";
						break;
					case STRING:

						key = keyCell.getStringCellValue().trim();
						break;
					default:

						throw new IllegalStateException("Cell type is not STRING: " + toCellInfoString(keyCell));
					}

					final MessagesInfo defaultMessagesInfo = MESSAGES_INFO_LIST.get(defaultCellNum - 1);
					final Cell defaultCell = row.getCell(defaultCellNum);

					parseValue(defaultMessagesInfo, key, defaultCell);

					IntStream.rangeClosed(firstLangCellNum, lastLangCellNum).forEach(langCellNum -> {

						final MessagesInfo messagesInfo = MESSAGES_INFO_LIST.get(langCellNum - 1);
						final Cell langCell = row.getCell(langCellNum);

						parseValue(messagesInfo, key, langCell);
					});
				}
			}
		});

	}

	private static void parseValue(@Nonnull final MessagesInfo messagesInfo, @Nonnull final String key, @Nonnull final Cell langCell) {

		final boolean isEmptyRow = key.isEmpty();

		if (isEmptyRow) {

			messagesInfo.addLine("");
			return;
		}

		final boolean isComment = key.startsWith("#");

		final String value;
		if (Objects.isNull(langCell)) {

			value = null;
		} else {

			switch (langCell.getCellTypeEnum()) {
			case BLANK:

				value = "";
				break;
			case STRING:

				value = langCell.getStringCellValue().trim();
				break;
			default:

				throw new IllegalStateException("Cell type is not STRING: " + toCellInfoString(langCell));
			}
		}

		if (isComment && Objects.isNull(value)) {

			messagesInfo.addLine(key);
			return;
		}

		if (Objects.isNull(value)) {

			if ("".equals(messagesInfo.getLangName())) {

				messagesInfo.addLine(key, "");
			} else {

				messagesInfo.addLine("");
			}
		} else {

			messagesInfo.addLine(key, value);
		}
	}

	private static String toCellInfoString(final Cell cell) {

		return "CellInfo [Row=" + cell.getRowIndex() + ", Column=" + cell.getColumnIndex() + ", Type=" + cell.getCellTypeEnum() + "]";
	}

	private static final class MessagesInfo {

		private final String langName;
		private final List<String> lines = new ArrayList<>();

		MessagesInfo(@Nonnull final String langName) {

			this.langName = langName;
		}

		String getLangName() {

			return langName;
		}

		void addLine(@Nonnull final String line) {

			Objects.requireNonNull(line);
			lines.add(line);
		}

		void addLine(@Nonnull final String key, @Nonnull final String value) {

			Objects.requireNonNull(key);
			if (key.isEmpty()) {

				throw new IllegalArgumentException("key is empty.");
			}

			Objects.requireNonNull(value);

			lines.add(key + " = " + value);
		}

		void createFile() {

			final String extension = langName.isEmpty() ? "" : "." + langName;
			final String fileName = "messages" + extension;

			final Path messages = Paths.get(".", "conf", fileName);
			System.out.println(messages);
			try {

				Files.write(messages, lines, StandardCharsets.UTF_8);
				System.out.println(String.join("\n", lines));
			} catch (IOException e) {

				throw new UncheckedIOException(e);
			}
		}
	}
}
