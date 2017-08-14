package common.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

public class Strings {

	private Strings() {
	}

	public static final String REGEX_NEW_LINE = "\\r\\n|\\r|\\n";

	@SuppressWarnings("null")
	@Nonnull
	public static List<String> splitNewLine(@Nonnull final String string) {

		Objects.requireNonNull(string);

		final List<String> list = Arrays.asList(string.split(REGEX_NEW_LINE)).stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
		return list;
	}

	@SuppressWarnings("null")
	@Nonnull
	public static String removeNewLine(@Nonnull final String string) {

		Objects.requireNonNull(string);

		return string.replaceAll(REGEX_NEW_LINE, "");
	}

	@SuppressWarnings("null")
	@Nonnull
	public static String randomAscii(final int count) {

		final RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('!', '~').build();
		return generator.generate(count);
	}

	@SuppressWarnings("null")
	@Nonnull
	public static String randomAlphanumeric(final int count) {

		final RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('0', 'z').filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS).build();
		return generator.generate(count);
	}
}
