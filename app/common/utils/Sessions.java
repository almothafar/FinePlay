package common.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import models.user.User.Role;

public class Sessions {

	private Sessions() {
	}

	@SuppressWarnings("null")
	@Nonnull
	public static List<String> toList(@Nonnull final String value) {

		Objects.requireNonNull(value);

		if (value.isEmpty()) {

			return Collections.emptyList();
		}

		try (final StringReader stringReader = new StringReader(value); final ICsvListReader reader = new CsvListReader(stringReader, CsvPreference.STANDARD_PREFERENCE)) {

			final List<String> list = reader.read();

			return Collections.unmodifiableList(list);
		} catch (final IOException e) {

			throw new UncheckedIOException(e);
		}
	}

	@SuppressWarnings("null")
	@Nonnull
	public static <T> String toValue(@Nonnull final List<T> list) {

		Objects.requireNonNull(list);

		if (list.isEmpty()) {

			return "";
		}

		try (final StringWriter stringWriter = new StringWriter(); final ICsvListWriter writer = new CsvListWriter(stringWriter, CsvPreference.STANDARD_PREFERENCE)) {

			writer.write(list);
			writer.flush();
			;

			final String value = stringWriter.toString();

			return value;
		} catch (final IOException e) {

			throw new UncheckedIOException(e);
		}
	}

	@SuppressWarnings("null")
	@Nonnull
	public static Set<Role> toRoles(@Nonnull final String value) {

		final Set<Role> roles = toList(value).stream().map(roleValue -> Role.valueOf(roleValue)).collect(Collectors.toCollection(() -> EnumSet.noneOf(Role.class)));

		return Collections.unmodifiableSet(roles);
	}
}
