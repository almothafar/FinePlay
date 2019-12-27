package controllers.lab.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import common.utils.Barcodes;
import common.utils.Templates;
import models.components.PagingInfo;
import models.system.System.PermissionsAllowed;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Messages;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Result;
import javax.annotation.Nonnull;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Library extends Controller {

	@Inject
	private MessagesApi messagesApi;

	@Authenticated(common.core.Authenticator.class)
	public Result index(@Nonnull final Request request, String lib) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (lib) {
		case "bootbox":

			return bootbox(request, lang, messages);
		case "select2":

			return select2(request, lang, messages);
		case "pickadate":

			return pickadate(request, lang, messages);
		case "chartjs":

			return chartjs(request, lang, messages);
		case "fullcalendar":

			return fullcalendar(request, lang, messages);
		case "bootstrapslider":

			return bootstrapslider(request, lang, messages);
		case "openlayers":

			return openlayers(request, lang, messages);
		case "jqvmap":

			return jqvmap(request, lang, messages);
		case "handsontable":

			return handsontable(request, lang, messages);
		case "highlight":

			return highlight(request, lang, messages);
		case "prettier":

			return prettier(request, lang, messages);
		case "d3":

			return d3(request, lang, messages);
		case "d3geoprojection":

			return d3geoprojection(request, lang, messages);
		case "cropperjs":

			return cropperjs(request, lang, messages);
		case "twentytwenty":

			return twentytwenty(request, lang, messages);
		case "diff2html":

			return diff2html(request, lang, messages);
		case "summernote":

			return summernote(request, lang, messages);
		case "marked":

			return marked(request, lang, messages);
		case "easymde":

			return easymde(request, lang, messages);
		case "markdeep":

			return markdeep(request, lang, messages);
		case "mermaid":

			return mermaid(request, lang, messages);
		case "echarts":

			return echarts(request, lang, messages);
		case "lightweightcharts":

			return lightweightcharts(request, lang, messages);
		case "html2canvas":

			return html2canvas(request, lang, messages);
		case "parsley":

			return parsley(request, lang, messages);
		case "hammerjs":

			return hammerjs(request, lang, messages);
		case "bootstrapcolorpicker":

			return bootstrapcolorpicker(request, lang, messages);
		case "frappegantt":

			return frappegantt(request, lang, messages);
		case "vizjs":

			return vizjs(request, lang, messages);
		case "jspdf":

			return jspdf(request, lang, messages);
		case "pdfjs":

			return pdfjs(request, lang, messages);
		case "revealjs":

			return revealjs(request, lang, messages);
		case "papercss":

			return papercss(request, lang, messages);
		case "camera":

			return camera(request, lang, messages);
		case "mustache":

			return mustache(request, lang, messages);
		case "zxing":

			return zxing(request, lang, messages);
		case "quaggajs":

			return quaggajs(request, lang, messages);
		case "jsqr":

			return jsqr(request, lang, messages);
		case "icon":

			return icon(request, lang, messages);
		case "3dmol":

			return tdmol(request, lang, messages);
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	public static Result bootbox(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.bootbox.render(request, lang, messages));
	}

	public static Result select2(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.select2.render(request, lang, messages));
	}

	public static Result pickadate(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.pickadate.render(request, lang, messages));
	}

	public static Result chartjs(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.chartjs.render(request, lang, messages));
	}

	public static Result fullcalendar(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.fullcalendar.render(request, lang, messages));
	}

	public static Result bootstrapslider(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.bootstrapslider.render(request, lang, messages));
	}

	public static Result openlayers(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.openlayers.render(request, lang, messages));
	}

	public static Result jqvmap(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.jqvmap.render(request, lang, messages));
	}

	public static Result handsontable(final Request request, final Lang lang, final Messages messages) {

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

		return ok(views.html.lab.library.handsontable.render(pagingInfo, rowsJson, request, lang, messages));
	}

	public static Result highlight(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.highlight.render(request, lang, messages));
	}

	public static Result prettier(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.prettier.render(request, lang, messages));
	}

	public static Result d3(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.d3.render(request, lang, messages));
	}

	public static Result d3geoprojection(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.d3geoprojection.render(request, lang, messages));
	}

	public static Result cropperjs(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.cropperjs.render(request, lang, messages));
	}

	public static Result twentytwenty(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.twentytwenty.render(request, lang, messages));
	}

	public static Result diff2html(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.diff2html.render(request, lang, messages));
	}

	public static Result summernote(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.summernote.render(request, lang, messages));
	}

	public static Result marked(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.marked.render(request, lang, messages));
	}

	public static Result easymde(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.easymde.render(request, lang, messages));
	}

	public static Result echarts(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.echarts.render(request, lang, messages));
	}

	public static Result lightweightcharts(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.lightweightcharts.render(request, lang, messages));
	}

	public static Result html2canvas(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.html2canvas.render(request, lang, messages));
	}

	public static Result parsley(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.parsley.render(request, lang, messages));
	}

	public static Result hammerjs(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.hammerjs.render(request, lang, messages));
	}

	public static Result bootstrapcolorpicker(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.bootstrapcolorpicker.render(request, lang, messages));
	}

	public static Result frappegantt(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.frappegantt.render(request, lang, messages));
	}

	public static Result vizjs(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.vizjs.render(request, lang, messages));
	}

	public static Result markdeep(final Request request, final Lang lang, final Messages messages) {

		try (final InputStream inputStream = play.Environment.simple().resourceAsStream("resources/lab/library/markdeep/features.md"); //
				final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

			final String markdeep = reader.lines().collect(Collectors.joining("\n"));

			return ok(views.html.lab.library.markdeep.render(markdeep, request, lang, messages));
		} catch (IOException e) {

			throw new RuntimeException(e);
		}
	}

	public static Result mermaid(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.mermaid.render(request, lang, messages));
	}

	public static Result jspdf(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.jspdf.render(request, lang, messages));
	}

	public static Result pdfjs(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.pdfjs.render(request, lang, messages));
	}

	public static Result revealjs(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.revealjs.render(request, lang, messages));
	}

	public static Result papercss(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.papercss.render(request, lang, messages));
	}

	public static Result camera(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.camera.render(request, lang, messages));
	}

	public static Result mustache(final Request request, final Lang lang, final Messages messages) {

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

		return ok(views.html.lab.library.mustache.render(fromTemplate, fromTemplateText, request, lang, messages));
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

	public static Result zxing(final Request request, final Lang lang, final Messages messages) {

		final Map<String, String> map = new HashMap<>();

		final byte[] ean13 = Barcodes.toEAN13(200, 100, "4512223683107");
		final String ean13Base64 = Base64.getEncoder().encodeToString(ean13);
		map.put("EAN-13", ean13Base64);

		final byte[] qrCode = Barcodes.toQRCode(150, 150, "https://www.playframework.com");
		final String qrCodeBase64 = Base64.getEncoder().encodeToString(qrCode);
		map.put("QR Code", qrCodeBase64);

		return ok(views.html.lab.library.zxing.render(map, request, lang, messages));
	}

	public static Result quaggajs(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.quaggajs.render(request, lang, messages));
	}

	public static Result jsqr(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.jsqr.render(request, lang, messages));
	}

	public static Result icon(final Request request, final Lang lang, final Messages messages) {

		return ok(views.html.lab.library.icon.render(request, lang, messages));
	}

	public static Result tdmol(final Request request, final Lang lang, final Messages messages) {

		try (final InputStream inputStream = play.Environment.simple().resourceAsStream("resources/lab/library/tdmol/2por.pdb"); //
				final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

			final String pdb = reader.lines().collect(Collectors.joining("\n"));

			return ok(views.html.lab.library.tdmol.render(pdb, request, lang, messages));
		} catch (IOException e) {

			throw new RuntimeException(e);
		}
	}

	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result pdfjs(@Nonnull final Request request, String pdf) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (pdf) {
		case "tracemonkey":

			return tracemonkey(request, lang, messages);
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	public static Result tracemonkey(final Request request, final Lang lang, final Messages messages) {

		return ok(play.Environment.simple().resourceAsStream("resources/pdfs/tracemonkey.pdf"));
	}

	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result revealjs(@Nonnull final Request request, String slide) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		switch (slide) {
		case "demo":

			return demo(request, lang, messages);
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	public static Result demo(final Request request, final Lang lang, final Messages messages) {

		return ok(play.Environment.simple().resourceAsStream("resources/slides/demo.html"));
	}

	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result papercss(@Nonnull final Request request, String paper) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final Map<String, String> params = getParams(request.queryString());
		switch (paper) {
		case "demo":

			return ok(views.html.lab.library.papercss_demo.render(params, request, lang, messages));
		case "receipt":

			return ok(views.html.lab.library.papercss_receipt.render(params, request, lang, messages));
		default:

			return redirect(controllers.setting.user.routes.User.index());
		}
	}

	private Map<String, String> getParams(final Map<String, String[]> queryString) {

		final Map<String, String> params = queryString.entrySet().stream()//
				.map(e -> new SimpleImmutableEntry<String, String>(e.getKey(), e.getValue()[0]))//
				.collect(Collectors.toMap(//
						e -> e.getKey(), //
						e -> e.getValue()));

		return Collections.unmodifiableMap(params);
	}
}
