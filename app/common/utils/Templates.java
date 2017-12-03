package common.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.io.Writer;

import javax.annotation.Nonnull;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class Templates {

	private Templates() {
	}

	@SuppressWarnings("null")
	@Nonnull
	public static String fill(@Nonnull final String template, @Nonnull final Object replace) {

		try (final Writer writer = new StringWriter()) {

			final MustacheFactory factory = new DefaultMustacheFactory();
			final Mustache mustache = factory.compile(template);
			mustache.execute(writer, replace);

			return writer.toString();
		} catch (final IOException e) {

			throw new UncheckedIOException(e);
		}
	}

	@SuppressWarnings("null")
	@Nonnull
	public static String fill(@Nonnull final Reader templateReader, @Nonnull final Object replace) {

		try (final Writer writer = new StringWriter()) {

			final MustacheFactory factory = new DefaultMustacheFactory();
			final Mustache mustache = factory.compile(templateReader, "resourceName");
			mustache.execute(writer, replace);

			return writer.toString();
		} catch (final IOException e) {

			throw new UncheckedIOException(e);
		}
	}
}
