package controllers.lab.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import common.utils.Barcodes;
import common.utils.Templates;
import models.components.PagingInfo;
import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Library extends Controller {

	@Authenticated(common.core.Authenticator.class)
	public Result index(String lib) {

		switch (lib) {
		case "bootbox":

			return bootbox();
		case "select2":

			return select2();
		case "pickadate":

			return pickadate();
		case "chartjs":

			return chartjs();
		case "fullcalendar":

			return fullcalendar();
		case "bootstrapslider":

			return bootstrapslider();
		case "openlayers":

			return openlayers();
		case "handsontable":

			return handsontable();
		case "highlight":

			return highlight();
		case "d3geoprojection":

			return d3geoprojection();
		case "cropperjs":

			return cropperjs();
		case "twentytwenty":

			return twentytwenty();
		case "diff2html":

			return diff2html();
		case "summernote":

			return summernote();
		case "parsley":

			return parsley();
		case "bootstrapcolorpicker":

			return bootstrapcolorpicker();
		case "frappegantt":

			return frappegantt();
		case "pdfjs":

			return pdfjs();
		case "camera":

			return camera();
		case "mustache":

			return mustache();
		case "zxing":

			return zxing();
		case "materialicons":

			return materialicons();
		case "icofont":

			return icofont();
		case "3dmol":

			return tdmol();
		default:

			return notFound(views.html.system.pages.notfound.render(request().method(), request().uri()));
		}
	}

	public static Result bootbox() {

		return ok(views.html.lab.library.bootbox.render());
	}

	public static Result select2() {

		return ok(views.html.lab.library.select2.render());
	}

	public static Result pickadate() {

		return ok(views.html.lab.library.pickadate.render());
	}

	public static Result chartjs() {

		return ok(views.html.lab.library.chartjs.render());
	}

	public static Result fullcalendar() {

		return ok(views.html.lab.library.fullcalendar.render());
	}

	public static Result bootstrapslider() {

		return ok(views.html.lab.library.bootstrapslider.render());
	}

	public static Result openlayers() {

		return ok(views.html.lab.library.openlayers.render());
	}

	public static Result handsontable() {

		final List<List<String>> rows = IntStream.rangeClosed(1, 500).mapToObj(rowIndex -> {
			final List<String> columns = IntStream.rangeClosed(1, 10).mapToObj(columnIndex -> "R" + rowIndex + "C" + columnIndex).collect(Collectors.toList());
			return columns;
		}).collect(Collectors.toList());

		final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
		final String rowsJson;
		try {

			rowsJson = mapper.writeValueAsString(rows);
		} catch (JsonProcessingException e) {

			throw new IllegalStateException(e);
		}

		int pageIndex = 0;
		int pageSize = 20;
		int pageCount = rows.size() / pageSize;
		final int odd = rows.size() % pageSize;
		if (0 < odd) {

			pageCount++;
		}

		final int pageStart = pageIndex * pageSize;
		int pageEnd = pageStart + pageSize;
		if (rows.size() < pageEnd) {

			pageEnd = pageStart + odd;
		}

		final PagingInfo pagingInfo = new PagingInfo();
		pagingInfo.setPageIndex(pageIndex);
		pagingInfo.setPageSize(pageSize);
		pagingInfo.setPageCount(pageCount);

		return ok(views.html.lab.library.handsontable.render(pagingInfo, rowsJson));
	}

	public static Result highlight() {

		return ok(views.html.lab.library.highlight.render());
	}

	public static Result d3geoprojection() {

		return ok(views.html.lab.library.d3geoprojection.render());
	}

	public static Result cropperjs() {

		return ok(views.html.lab.library.cropperjs.render());
	}

	public static Result twentytwenty() {

		return ok(views.html.lab.library.twentytwenty.render());
	}

	public static Result diff2html() {

		return ok(views.html.lab.library.diff2html.render());
	}

	public static Result summernote() {

		return ok(views.html.lab.library.summernote.render());
	}

	public static Result parsley() {

		return ok(views.html.lab.library.parsley.render());
	}

	public static Result bootstrapcolorpicker() {

		return ok(views.html.lab.library.bootstrapcolorpicker.render());
	}

	public static Result frappegantt() {

		return ok(views.html.lab.library.frappegantt.render());
	}

	public static Result pdfjs() {

		return ok(views.html.lab.library.pdfjs.render());
	}

	public static Result camera() {

		return ok(views.html.lab.library.camera.render());
	}

	public static Result mustache() {

		// fixed.

		final String fromTemplate = Templates.fill("resources/lab/library/mustache/template.mustache", new EventNotify("Our Customer", "My Company"));

		// free.

		final Map<String, Object> eventNotify = new HashMap<>();
		eventNotify.put("to", "Our Customer");
		eventNotify.put("events", Arrays.asList(new Event(LocalDate.of(2000, Month.JANUARY, 3), "First event."), new Event(LocalDate.of(2000, Month.JANUARY, 5), "Second event.")));
		eventNotify.put("from", "My Company");

		final String fromTemplateText = Templates.fill(new StringReader("" + //
				"To: {{to}}\n" + //
				"\n" + //
				"{{#events}}" + //
				"  Date: {{date}} / {{description}}\n" + //
				"{{/events}}\n" + //
				"\n" + //
				"From: {{from}}"), eventNotify);

		return ok(views.html.lab.library.mustache.render(fromTemplate, fromTemplateText));
	}

	private static class Event {

		@SuppressWarnings("unused")
		LocalDate date;
		@SuppressWarnings("unused")
		String description;

		Event(final LocalDate date, final String description) {

			this.date = date;
			this.description = description;
		}
	}

	private static class EventNotify {

		@SuppressWarnings("unused")
		String to;
		@SuppressWarnings("unused")
		String from;

		EventNotify(final String to, final String from) {

			this.to = to;
			this.from = from;
		}

		@SuppressWarnings("unused")
		List<Event> events() {

			return Arrays.asList(new Event(LocalDate.of(2000, Month.JANUARY, 3), "First event."), new Event(LocalDate.of(2000, Month.JANUARY, 5), "Second event."));
		}

		private static class Event {

			@SuppressWarnings("unused")
			LocalDate date;
			@SuppressWarnings("unused")
			String description;

			Event(final LocalDate date, final String description) {

				this.date = date;
				this.description = description;
			}
		}
	}

	public static Result zxing() {

		final Map<String, String> map = new HashMap<>();

		final byte[] ean13 = Barcodes.toEAN13(200, 100, "4512223683107");
		final String ean13Base64 = Base64.getEncoder().encodeToString(ean13);
		map.put("EAN-13", ean13Base64);

		final byte[] qrCode = Barcodes.toQRCode(150, 150, "https://www.playframework.com");
		final String qrCodeBase64 = Base64.getEncoder().encodeToString(qrCode);
		map.put("QR Code", qrCodeBase64);

		return ok(views.html.lab.library.zxing.render(map));
	}

	public static Result materialicons() {

		return ok(views.html.lab.library.materialicons.render());
	}

	public static Result icofont() {

		return ok(views.html.lab.library.icofont.render());
	}

	public static Result tdmol() {

		try (final InputStream inputStream = play.Environment.simple().resourceAsStream("resources/lab/library/tdmol/2por.pdb"); //
				final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

			final String pdb = reader.lines().collect(Collectors.joining("\n"));

			return ok(views.html.lab.library.tdmol.render(pdb));
		} catch (IOException e) {

			throw new RuntimeException(e);
		}
	}
}
