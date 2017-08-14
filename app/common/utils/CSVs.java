package common.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import play.api.PlayException.ExceptionSource;

public class CSVs {

	private CSVs() {
	}

	@SuppressWarnings({"serial", "null"})
	@Nonnull
	public static String toCSV(//
			@Nonnull final String[] headers, //
			@Nonnull final CellProcessor[] processors, //
			@Nonnull final List<?> beans) {

		final String csv;
		try (final StringWriter stringWriter = new StringWriter(); final ICsvBeanWriter writer = new CsvBeanWriter(stringWriter, CsvPreference.STANDARD_PREFERENCE)) {

			stringWriter.write('\uFEFF');
			writer.writeHeader(headers);
			for (int i = 0; i < beans.size(); i++) {

				final Object bean = beans.get(i);

				try {

					writer.write(bean, headers, processors);
				} catch (final SuperCsvException e) {

					throw new ExceptionSource("CSV write error", e.toString(), e) {

						@Override
						public String sourceName() {

							return bean.getClass().getName() + " beans";
						}

						@Override
						public Integer position() {

							return e.getCsvContext().getColumnNumber();
						}

						@Override
						public Integer line() {

							return e.getCsvContext().getLineNumber() + 1;
						}

						@Override
						public String input() {

							return beans.toString();
						}
					};
				}
			}

			writer.flush();
			csv = stringWriter.toString();
		} catch (final IOException e) {

			throw new UncheckedIOException(e);
		}

		return csv;
	}

	@SuppressWarnings({"serial", "null"})
	@Nonnull
	public static <BEAN> List<BEAN> toBeans(//
			@Nonnull final String[] headers, //
			@Nonnull final CellProcessor[] processors, //
			@Nonnull final String csv, //
			@Nonnull final Class<BEAN> clazz) {

		final List<BEAN> beans = new ArrayList<>();
		try (final StringReader stringReader = new StringReader(csv); final ICsvBeanReader reader = new CsvBeanReader(stringReader, CsvPreference.STANDARD_PREFERENCE)) {

			reader.getHeader(true);
			while (true) {

				try {

					final BEAN bean = reader.read(clazz, headers, processors);

					if (bean == null) {

						break;
					}

					beans.add(bean);
				} catch (final SuperCsvException e) {

					throw new ExceptionSource("CSV read error", e.toString(), e) {

						@Override
						public String sourceName() {

							return "Text of csv";
						}

						@Override
						public Integer position() {

							return e.getCsvContext().getColumnNumber();
						}

						@Override
						public Integer line() {

							return e.getCsvContext().getLineNumber() + 1;
						}

						@Override
						public String input() {

							return csv;
						}
					};
				}
			}
		} catch (final IOException e) {

			throw new UncheckedIOException(e);
		}

		return Collections.unmodifiableList(beans);
	}
}
