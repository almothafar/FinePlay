package controllers.lab.application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.annotation.Nonnull;
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
import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.NotUsed;
import akka.actor.Status;
import akka.stream.OverflowStrategy;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import common.system.MessageKeys;
import common.utils.Locales;
import common.utils.PDFs;
import common.utils.Reports;
import common.utils.ZIPs;
import models.base.EntityDao;
import models.system.System.PermissionsAllowed;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import play.api.PlayException;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Download extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private MessagesApi messages;

	@Inject
	private JPAApi jpaApi;

	private final EntityDao<models.user.User> userDao = new EntityDao<models.user.User>() {
	};

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
	public Result wordStream() {

		try (final XWPFDocument document = new XWPFDocument(); //
				final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			final XWPFParagraph header = document.createParagraph();
			header.setAlignment(ParagraphAlignment.CENTER);
			final XWPFRun run = header.createRun();
			run.setText(messages.get(lang(), MessageKeys.WELCOME));

			final XWPFParagraph paragraph0 = document.createParagraph();
			final XWPFRun run0 = paragraph0.createRun();
			run0.setText("(｀・ω・´)");
			final XWPFParagraph paragraph1 = document.createParagraph();
			final XWPFRun run1 = paragraph1.createRun();
			run1.setText("(*´ω｀*)");
			final XWPFParagraph paragraph2 = document.createParagraph();
			final XWPFRun run2 = paragraph2.createRun();
			run2.setText("(´･ω･`)");

			final XWPFTable table = document.createTable(1, 3);
			final XWPFTableCell cell0 = table.getRow(0).getCell(0);
			final XWPFParagraph cellParagraph0 = cell0.getParagraphs().get(0);
			final XWPFRun cellRun0 = cellParagraph0.createRun();
			cellRun0.setText("Plain");
			cellRun0.setColor("ff0000");
			final XWPFTableCell cell1 = table.getRow(0).getCell(1);
			final XWPFParagraph cellParagraph1 = cell1.getParagraphs().get(0);
			final XWPFRun cellRun1 = cellParagraph1.createRun();
			cellRun1.setText("Bold");
			cellRun1.setBold(true);
			cellRun1.setColor("00ff00");
			final XWPFTableCell cell2 = table.getRow(0).getCell(2);
			final XWPFParagraph cellParagraph2 = cell2.getParagraphs().get(0);
			final XWPFRun cellRun2 = cellParagraph2.createRun();
			cellRun2.setText("Italic");
			cellRun2.setItalic(true);
			cellRun2.setColor("0000ff");

			document.write(outputStream);
			return ok(outputStream.toByteArray()).as(Http.MimeTypes.BINARY);
		} catch (IOException e) {

			throw new UncheckedIOException(e);
		}
	}

	@Authenticated(common.core.Authenticator.class)
	public Result powerPointStream() {

		try (XMLSlideShow slideShow = new XMLSlideShow(); //
				final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			slideShow.createSlide();

			final XSLFSlideMaster master = slideShow.getSlideMasters().get(0);

			final XSLFSlideLayout layout1 = master.getLayout(SlideLayout.TITLE);
			final XSLFSlide slide1 = slideShow.createSlide(layout1);
			final XSLFTextShape[] ph1 = slide1.getPlaceholders();
			final XSLFTextShape titlePlaceholder1 = ph1[0];
			titlePlaceholder1.setText(messages.get(lang(), MessageKeys.WELCOME));
			final XSLFTextShape subtitlePlaceholder1 = ph1[1];
			subtitlePlaceholder1.setText(messages.get(lang(), MessageKeys.SYSTEM_NAME));

			final XSLFSlideLayout layout2 = master.getLayout(SlideLayout.TITLE_AND_CONTENT);
			final XSLFSlide slide2 = slideShow.createSlide(layout2);
			final XSLFTextShape[] ph2 = slide2.getPlaceholders();
			final XSLFTextShape titlePlaceholder2 = ph2[0];
			titlePlaceholder2.setText(messages.get(lang(), MessageKeys.WELCOME));
			final XSLFTextShape bodyPlaceholder = ph2[1];

			bodyPlaceholder.clearText();
			final XSLFTextParagraph p1 = bodyPlaceholder.addNewTextParagraph();
			p1.setIndentLevel(0);
			p1.addNewTextRun().setText("(｀・ω・´)");
			final XSLFTextParagraph p2 = bodyPlaceholder.addNewTextParagraph();
			p2.setIndentLevel(1);
			p2.addNewTextRun().setText("(*´ω｀*)");
			final XSLFTextParagraph p3 = bodyPlaceholder.addNewTextParagraph();
			p3.setIndentLevel(2);
			p3.addNewTextRun().setText("(´･ω･`)");

			slideShow.write(outputStream);
			return ok(outputStream.toByteArray()).as(Http.MimeTypes.BINARY);
		} catch (IOException e) {

			throw new UncheckedIOException(e);
		}
	}

	@Authenticated(common.core.Authenticator.class)
	public Result pdfboxStream() {

		try (final PDDocument document = new PDDocument(); final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			final PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);

			try (final PDPageContentStream stream = new PDPageContentStream(document, page)) {

				final Path imagePath = Paths.get(".", "public", "images", lang().code(), "logo.png");
				final BufferedImage bufferedImage = ImageIO.read(imagePath.toFile());
				final int imageHeight = 38;
				final float reduceRate = (float) imageHeight / bufferedImage.getHeight();
				final int imageWidth = (int) (bufferedImage.getWidth() * reduceRate);

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

			return Paths.get("/", "Library", "Fonts", "Microsoft", "MS Gothic.ttf");
		} else if (os.startsWith("linux")) {

			throw new PlayException("Font error.", "This server(Linux) is unsupported.");
		} else if (os.startsWith("windows")) {

			throw new PlayException("Font error.", "This server(Windows) is unsupported.");
		} else {

			throw new PlayException("Font error.", "This server OS is unsupported.");
		}
	}

	@SuppressWarnings("null")
	@Authenticated(common.core.Authenticator.class)
	public CompletionStage<Result> wkhtmltopdfStream() {

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

	@FunctionalInterface
	private static interface Reporter {

		byte[] toReport(@Nonnull final InputStream templateStream, @Nonnull final Map<String, Object> parameters, @Nonnull final JRDataSource dataSource);
	}

	@Transactional(readOnly = true)
	@Authenticated(common.core.Authenticator.class)
	public CompletionStage<Result> reportPdfStream() {

		final Reporter reporter = (templateStream, parameters, dataSource) -> Reports.toPDF(templateStream, parameters, dataSource);
		return reportStream(reporter);
	}

	@Transactional(readOnly = true)
	@Authenticated(common.core.Authenticator.class)
	public CompletionStage<Result> reportWordStream() {

		final Reporter reporter = (templateStream, parameters, dataSource) -> Reports.toDocx(templateStream, parameters, dataSource);
		return reportStream(reporter);
	}

	@Transactional(readOnly = true)
	@Authenticated(common.core.Authenticator.class)
	public CompletionStage<Result> reportExcelStream() {

		final Reporter reporter = (templateStream, parameters, dataSource) -> Reports.toXlsx(templateStream, parameters, dataSource);
		return reportStream(reporter);
	}

	@Transactional(readOnly = true)
	@Authenticated(common.core.Authenticator.class)
	public CompletionStage<Result> reportPowerpointStream() {

		final Reporter reporter = (templateStream, parameters, dataSource) -> Reports.toPptx(templateStream, parameters, dataSource);
		return reportStream(reporter);
	}

	@SuppressWarnings("null")
	private CompletionStage<Result> reportStream(final Reporter reporter) {

		final Map<String, Object> parameters = new HashMap<>();
		parameters.put(MessageKeys.USER_USERID, messages.get(lang(), MessageKeys.USER_USERID));
		parameters.put("bookland", "9784003101");
		parameters.put("barcode", "4512223683107");
		parameters.put("qrcode", "https://www.playframework.com");
		parameters.put(MessageKeys.REPORT, messages.get(lang(), MessageKeys.REPORT));
		parameters.put(MessageKeys.WELCOME, messages.get(lang(), MessageKeys.WELCOME));

		final List<models.user.User> users = userDao.readList(jpaApi.em(), models.user.User.class);
		final JRDataSource dataSource = new JRBeanCollectionDataSource(users);

		return CompletableFuture.supplyAsync(() -> {

			try (final InputStream templateStream = play.Environment.simple().resourceAsStream("resources/lab/application/report.jasper")) {

				final byte[] bytes = reporter.toReport(templateStream, parameters, dataSource);
				return ok(bytes).as(Http.MimeTypes.BINARY);
			} catch (IOException e) {

				throw new UncheckedIOException(e);
			}
		});
	}

	@Transactional(readOnly = true)
	@Authenticated(common.core.Authenticator.class)
	public CompletionStage<Result> paperPdfStream() {

		final Reporter reporter = (templateStream, parameters, dataSource) -> Reports.toPDF(templateStream, parameters, dataSource);
		return paperStream(reporter);
	}

	@SuppressWarnings("null")
	private CompletionStage<Result> paperStream(final Reporter reporter) {

		final Map<String, Object> parameters = new HashMap<>();
		parameters.put(MessageKeys.USER_USERID, messages.get(lang(), MessageKeys.USER_USERID));

		final JRDataSource dataSource = new JREmptyDataSource();

		return CompletableFuture.supplyAsync(() -> {

			try (final InputStream templateStream = play.Environment.simple().resourceAsStream("resources/lab/application/paper.jasper")) {

				final byte[] bytes = reporter.toReport(templateStream, parameters, dataSource);
				return ok(bytes).as(Http.MimeTypes.BINARY);
			} catch (IOException e) {

				throw new UncheckedIOException(e);
			}
		});
	}

	@Authenticated(common.core.Authenticator.class)
	public CompletionStage<Result> zipStream() {

		final Path path = createExampleArchivePath();

		return CompletableFuture.supplyAsync(() -> {

			final byte[] bytes = ZIPs.archive(path);

			final Path extractPath = ZIPs.extract(bytes);
			System.out.println(extractPath);

			return ok(bytes).as(Http.MimeTypes.BINARY);
		});
	}

	@Authenticated(common.core.Authenticator.class)
	public CompletionStage<Result> encryptZipStream() {

		final Path path = createExampleArchivePath();

		return CompletableFuture.supplyAsync(() -> {

			final byte[] bytes = ZIPs.archive("password", path);

			final Path extractPath = ZIPs.extract("password", bytes);
			System.out.println(extractPath);

			return ok(bytes).as(Http.MimeTypes.BINARY);
		});
	}

	@Nonnull
	private static Path createExampleArchivePath() {

		try {

			final Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"));
			final Path arcPath = tmpPath.resolve(UUID.randomUUID().toString());
			Files.createDirectories(arcPath);

			final Path parentPath = arcPath.resolve(Paths.get("parent.txt"));
			Files.write(parentPath, "(ꈍ ◡ ꈍ)".getBytes(StandardCharsets.UTF_8));

			final Path childrenPath = arcPath.resolve(Paths.get("children"));
			Files.createDirectories(childrenPath);

			final Path child0Path = childrenPath.resolve(Paths.get("child0.txt"));
			Files.write(child0Path, "(｀・ω・´)".getBytes(StandardCharsets.UTF_8));

			final Path child1Path = childrenPath.resolve(Paths.get("child1.txt"));
			Files.write(child1Path, "(*´ω｀*)".getBytes(StandardCharsets.UTF_8));

			final Path child2Path = childrenPath.resolve(Paths.get("child2.txt"));
			Files.write(child2Path, "(´･ω･`)".getBytes(StandardCharsets.UTF_8));

			return arcPath;
		} catch (IOException e) {

			LOGGER.error(e.getLocalizedMessage());
			throw new UncheckedIOException(e);
		}
	}

	@Authenticated(common.core.Authenticator.class)
	public Result fileName() {

		final String fileName = messages.get(Locales.toLang(Locale.US), MessageKeys.FILE_NAME) + ".txt";
		final String userFileName;
		try {

			userFileName = URLEncoder.encode(messages.get(lang(), MessageKeys.FILE_NAME).replaceAll("\\s", "_"), StandardCharsets.UTF_8.name()) + ".txt";
		} catch (UnsupportedEncodingException e) {

			throw new UncheckedIOException(e);
		}

		final StringBuilder builder = new StringBuilder("attachment;");
		builder.append("filename=\"").append(fileName).append("\";");
		builder.append("filename*=UTF-8''").append(userFileName);

		return ok("(｀・ω・´)\r\n(*´ω｀*)\r\n(´･ω･`)\r\n".getBytes(StandardCharsets.UTF_8))//
				.as(Http.MimeTypes.BINARY)//
				.withHeader(Http.HeaderNames.CONTENT_DISPOSITION, builder.toString());
	}

	@Authenticated(common.core.Authenticator.class)
	public Result urlFileName(final String urlFileName) {

		return ok("(｀・ω・´)\r\n(*´ω｀*)\r\n(´･ω･`)\r\n".getBytes(StandardCharsets.UTF_8)).as(Http.MimeTypes.BINARY);
	}

	@Authenticated(common.core.Authenticator.class)
	public CompletionStage<Result> ivdPdfStream(final long executionId) {

		return CompletableFuture.supplyAsync(() -> {

			final Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"));
			final Path tmpBatchsPath = tmpPath.resolve("batchs");
			final Path tmpBatchExecutionPath = tmpBatchsPath.resolve(String.valueOf(executionId));
			final Path tmpBatchExecutionPdfPath = tmpBatchExecutionPath.resolve("ivd.pdf");

			byte[] bytes;
			try {

				bytes = Files.readAllBytes(tmpBatchExecutionPdfPath);
				Files.deleteIfExists(tmpBatchExecutionPdfPath);
			} catch (IOException e) {

				LOGGER.error(e.getLocalizedMessage());
				throw new UncheckedIOException(e);
			}

			return ok(bytes).as(Http.MimeTypes.BINARY);
		});
	}
}
