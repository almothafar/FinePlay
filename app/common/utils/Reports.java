package common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.jboss.weld.exceptions.IllegalStateException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.DocxExporterConfiguration;
import net.sf.jasperreports.export.DocxReportConfiguration;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.PdfReportConfiguration;
import net.sf.jasperreports.export.PptxExporterConfiguration;
import net.sf.jasperreports.export.PptxReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.XlsxExporterConfiguration;
import net.sf.jasperreports.export.XlsxReportConfiguration;

public class Reports {

	private Reports() {
	}

	@Nonnull
	public static byte[] toPDF(@Nonnull final InputStream jasperStream, @Nonnull final Map<String, Object> parameters, @Nonnull final JRDataSource dataSource) {

		final Exporter<ExporterInput, PdfReportConfiguration, PdfExporterConfiguration, OutputStreamExporterOutput> exporter = new JRPdfExporter();
		return toReport(exporter, jasperStream, parameters, dataSource);
	}

	@Nonnull
	public static byte[] toDocx(@Nonnull final InputStream jasperStream, @Nonnull final Map<String, Object> parameters, @Nonnull final JRDataSource dataSource) {

		final Exporter<ExporterInput, DocxReportConfiguration, DocxExporterConfiguration, OutputStreamExporterOutput> exporter = new JRDocxExporter();
		return toReport(exporter, jasperStream, parameters, dataSource);
	}

	@Nonnull
	public static byte[] toXlsx(@Nonnull final InputStream jasperStream, @Nonnull final Map<String, Object> parameters, @Nonnull final JRDataSource dataSource) {

		final Exporter<ExporterInput, XlsxReportConfiguration, XlsxExporterConfiguration, OutputStreamExporterOutput> exporter = new JRXlsxExporter();
		return toReport(exporter, jasperStream, parameters, dataSource);
	}

	@Nonnull
	public static byte[] toPptx(@Nonnull final InputStream jasperStream, @Nonnull final Map<String, Object> parameters, @Nonnull final JRDataSource dataSource) {

		final Exporter<ExporterInput, PptxReportConfiguration, PptxExporterConfiguration, OutputStreamExporterOutput> exporter = new JRPptxExporter();
		return toReport(exporter, jasperStream, parameters, dataSource);
	}

	@SuppressWarnings("null")
	@Nonnull
	private static byte[] toReport(@Nonnull final Exporter<ExporterInput, ?, ?, OutputStreamExporterOutput> exporter, @Nonnull final InputStream jasperStream, @Nonnull final Map<String, Object> parameters, @Nonnull final JRDataSource dataSource) {

		Objects.requireNonNull(jasperStream);
		Objects.requireNonNull(parameters);
		Objects.requireNonNull(dataSource);

		try (final ByteArrayOutputStream stream = new ByteArrayOutputStream()) {

			final JasperPrint print = JasperFillManager.fillReport(jasperStream, parameters, dataSource);

			exporter.setExporterInput(new SimpleExporterInput(print));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(stream));

			exporter.exportReport();
			final byte[] bytes = stream.toByteArray();
			return bytes;
		} catch (IOException e) {

			throw new UncheckedIOException(e);
		} catch (JRException e) {

			throw new IllegalStateException(e);
		}
	}
}
