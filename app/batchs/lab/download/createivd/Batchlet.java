package batchs.lab.download.createivd;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.utils.Offices;
import common.utils.Reports;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;

@Named
public class Batchlet extends AbstractBatchlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private JobContext jobCtx;

	@Override
	public String process() throws Exception {

		final long executionId = jobCtx.getExecutionId();

		final Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"));
		final Path tmpBatchsPath = tmpPath.resolve("batchs");
		final Path tmpBatchExecutionPath = tmpBatchsPath.resolve(String.valueOf(executionId));
		final Path tmpBatchExecutionPdfPath = tmpBatchExecutionPath.resolve("ivd.pdf");

		final byte[] bytes = createIVDData();
		try {

			Files.deleteIfExists(tmpBatchExecutionPdfPath);
			Files.createDirectories(tmpBatchExecutionPath);

			Files.write(tmpBatchExecutionPdfPath, bytes);
		} catch (IOException e) {

			LOGGER.error(e.getLocalizedMessage());
			throw new UncheckedIOException(e);
		}

		return null;
	}

	private byte[] createIVDData() {

		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("hiragino", "{{HIRAGINO}}");
		parameters.put("ipamjm", "{{IPAMJM}}");

		final JRDataSource dataSource = new JREmptyDataSource();

		try (final InputStream templateStream = play.Environment.simple().resourceAsStream("resources/lab/application/ivd.jrxml")) {

			final Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"));

			final byte[] pptxBytes = Reports.toPptx(templateStream, parameters, dataSource);
			final Path pptxPath = tmpPath.resolve(UUID.randomUUID() + ".pptx");
			Files.write(pptxPath, pptxBytes);
			System.out.println(pptxPath);

			byte[] fodpBytes = Offices.toFodp(pptxPath);
			fodpBytes = resolveIVD(fodpBytes);

			final Path fodpPath = tmpPath.resolve(UUID.randomUUID() + ".fodp");
			Files.write(fodpPath, fodpBytes);
			System.out.println(fodpPath);

			final byte[] bytes = Offices.toPDF(fodpPath);

			return bytes;
		} catch (IOException e) {

			throw new UncheckedIOException(e);
		}
	}

	private static byte[] resolveIVD(@Nonnull final byte[] fodpBytes) {

		String fodpXML = new String(fodpBytes, StandardCharsets.UTF_8);

		// fodpXML = resolveFont(fodpXML, "Hiragino Mincho ProN", "ヒラギノ明朝 ProN");
		fodpXML = fodpXML.replace("{{HIRAGINO}}", "芦\uDB40\uDD00芦\uDB40\uDD01");

		fodpXML = resolveFont(fodpXML, "IPAmjMincho", "IPAmj明朝");
		fodpXML = fodpXML.replace("{{IPAMJM}}", "芦\uDB40\uDD02芦\uDB40\uDD03芦\uDB40\uDD04芦\uDB40\uDD05芦\uDB40\uDD06芦\uDB40\uDD07芦\uDB40\uDD08芦\uDB40\uDD09");

		return fodpXML.getBytes(StandardCharsets.UTF_8);
	}

	private static String resolveFont(final String fodpXML, final String target, final String replacement) {

		final Matcher matcher = Pattern.compile("(svg:font-family=\"(?:&apos;)?)" + target + "((?:&apos;)?\")").matcher(fodpXML);

		final StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {

			matcher.appendReplacement(buffer, "$1" + replacement + "$2");
		}
		matcher.appendTail(buffer);

		return buffer.toString();
	}
}
