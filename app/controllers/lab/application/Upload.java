package controllers.lab.application;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import common.system.MessageKeys;
import controllers.user.User;
import models.system.System.PermissionsAllowed;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.MessagesApi;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;

@PermissionsAllowed
public class Upload extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

	@Inject
	private MessagesApi messages;

	public Result index() {

		return ok(views.html.lab.application.upload.render());
	}

	@RequireCSRFCheck
	public Result form() {

		final MultipartFormData<File> body = request().body().asMultipartFormData();
		final play.mvc.Http.MultipartFormData.FilePart<File> filePart = body.getFile("inputName");
		if (filePart != null) {

			@SuppressWarnings("unused")
			final String fileName = filePart.getFilename();
			@SuppressWarnings("unused")
			final String contentType = filePart.getContentType();
			@SuppressWarnings("unused")
			final Path path = filePart.getFile().toPath();
			LOGGER.info("" + path);

			flash("success", "<strong>" + messages.get(lang(), MessageKeys.SUCCESS) + "</strong> " + messages.get(lang(), MessageKeys.FILE) + " uploaded");
			return index();
		} else {

			flash("warning", "<strong>" + messages.get(lang(), MessageKeys.WARNING) + "</strong> Missing " + messages.get(lang(), MessageKeys.FILE));
			return index();
		}
	}

	@RequireCSRFCheck
	public Result image() {

		final MultipartFormData<File> body = request().body().asMultipartFormData();
		final play.mvc.Http.MultipartFormData.FilePart<File> filePart = body.getFile("inputName");
		if (filePart != null) {

			@SuppressWarnings("unused")
			final String fileName = filePart.getFilename();
			@SuppressWarnings("unused")
			final String contentType = filePart.getContentType();
			final Path path = filePart.getFile().toPath();
			LOGGER.info("" + path);

			flash("imageSuccess", "<strong>" + messages.get(lang(), MessageKeys.SUCCESS) + "</strong> " + messages.get(lang(), MessageKeys.FILE) + " uploaded");
			return index();
		} else {

			flash("imageWarning", "<strong>" + messages.get(lang(), MessageKeys.WARNING) + "</strong> Missing " + messages.get(lang(), MessageKeys.FILE));
			return index();
		}
	}

	@BodyParser.Of(value = BodyParser.MultipartFormData.class)
	@RequireCSRFCheck
	public Result direct() {

		final MultipartFormData<File> body = request().body().asMultipartFormData();
		final Map<String, String[]> form = body.asFormUrlEncoded();
		final play.mvc.Http.MultipartFormData.FilePart<File> filePart = body.getFile("inputName");

		final Path path = filePart.getFile().toPath();
		LOGGER.info("" + path);

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();
		result.put("status", "success");
		result.put("message", "<strong>" + messages.get(lang(), MessageKeys.SUCCESS) + "</strong> " + messages.get(lang(), MessageKeys.FILE) + " uploaded");

		return ok(result);
	}
}
