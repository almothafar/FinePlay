package controllers.lab.application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import akka.NotUsed;
import akka.actor.Status;
import akka.stream.OverflowStrategy;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import common.system.MessageKeys;
import common.utils.PDFs;
import play.mvc.Controller;
import models.system.System.PermissionsAllowed;
import play.api.PlayException;
import play.i18n.MessagesApi;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Download extends Controller {

	@Inject
	private MessagesApi messages;

	@Authenticated(common.core.Authenticator.class)
	public Result index() {

		return ok(views.html.lab.application.download.render());
	}

	@Authenticated(common.core.Authenticator.class)
	public Result file() {

		return ok(Paths.get("file.txt").toFile()).as(Http.MimeTypes.BINARY);
	}

	@Authenticated(common.core.Authenticator.class)
	public Result stream() {

		final InputStream stream = new ByteArrayInputStream("(｀・ω・´)\r\n(*´ω｀*)\r\n(´･ω･`)\r\n".getBytes(StandardCharsets.UTF_8));
		return ok(stream).as(Http.MimeTypes.BINARY);
	}

	@Authenticated(common.core.Authenticator.class)
	public Result chunks() {

		final Source<ByteString, ?> source = Source.<ByteString>actorRef(256, OverflowStrategy.dropNew()).mapMaterializedValue(sourceActor -> {
			sourceActor.tell(ByteString.fromString("(｀・ω・´)\r\n"), null);
			sourceActor.tell(ByteString.fromString("(*´ω｀*)\r\n"), null);
			sourceActor.tell(ByteString.fromString("(´･ω･`)\r\n"), null);
			sourceActor.tell(new Status.Success(NotUsed.getInstance()), null);
			return NotUsed.getInstance();
		});

		return ok().chunked(source).as(Http.MimeTypes.BINARY);
	}

	@Authenticated(common.core.Authenticator.class)
	public Result excelStream() {

		try (final Workbook workbook = new XSSFWorkbook(); //
				final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			final Sheet sheet = workbook.createSheet();
			final Row row0 = sheet.createRow(0);
			final Cell cell0 = row0.createCell(0);
			cell0.setCellValue("(｀・ω・´)");
			final Row row1 = sheet.createRow(1);
			final Cell cell1 = row1.createCell(0);
			cell1.setCellValue("(*´ω｀*)");
			final Row row2 = sheet.createRow(2);
			final Cell cell2 = row2.createCell(0);
			cell2.setCellValue("(´･ω･`)");

			workbook.write(outputStream);
			return ok(outputStream.toByteArray()).as(Http.MimeTypes.BINARY);
		} catch (IOException e) {

			throw new UncheckedIOException(e);
		}
	}

	@Authenticated(common.core.Authenticator.class)
	public Result pdfStream() {

		try (final PDDocument document = new PDDocument(); final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			final PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);

			try (final PDPageContentStream stream = new PDPageContentStream(document, page)) {

				final Path imagePath = Paths.get(".", "public", "images", lang().code(), "logo.png");
				final BufferedImage bufferedImage = ImageIO.read(imagePath.toFile());
				final int imageHeight = 38;
				final float reduceRate = (float) imageHeight / bufferedImage.getHeight();
				final int imageWidth = (int) ((float) bufferedImage.getWidth() * reduceRate);

				final PDImageXObject image = PDImageXObject.createFromFileByContent(imagePath.toFile(), document);
				float y = PDRectangle.A4.getHeight() - imageHeight;
				stream.drawImage(image, 0, y, imageWidth, imageHeight);

				stream.beginText();

				final int fontSize = 36;
				final PDFont pdFont = PDType0Font.load(document, getFontPath().toFile());
				stream.setFont(pdFont, fontSize);

				y = y - fontSize;
				stream.newLineAtOffset(0, y);
				stream.showText(messages.get(lang(), MessageKeys.WELCOME));

				stream.newLineAtOffset(0, -fontSize);
				stream.showText(messages.get(lang(), lang().toLocale().getDisplayName(lang().toLocale())));

				stream.endText();
			}

			document.save(outputStream);

			return ok(outputStream.toByteArray()).as(Http.MimeTypes.BINARY);
		} catch (IOException e) {

			throw new UncheckedIOException(e);
		}
	}

	private Path getFontPath() {

		final String os = System.getProperty("os.name").toLowerCase();
		if (os.startsWith("mac")) {

			return Paths.get("/", "/Library", "Fonts", "Microsoft", "MS Gothic.ttf");
		} else if (os.startsWith("linux")) {

			throw new PlayException("Font error.", "This server(Linux) is unsupported.");
		} else if (os.startsWith("windows")) {

			throw new PlayException("Font error.", "This server(Windows) is unsupported.");
		} else {

			throw new PlayException("Font error.", "This server OS is unsupported.");
		}
	}

	@Authenticated(common.core.Authenticator.class)
	public CompletionStage<Result> reportStream() {

		final URI reportPage = URI.create("http://getbootstrap.com");
		final List<String> arguments = Arrays.asList("--zoom", "2");

		return CompletableFuture.supplyAsync(() -> {

			byte[] bytes;
			try {

				bytes = PDFs.toPDF(reportPage, arguments);
				return ok(bytes).as(Http.MimeTypes.BINARY);
			} catch (Exception e) {

				throw e;
			}
		});
	}
}
