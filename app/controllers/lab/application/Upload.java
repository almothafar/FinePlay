package controllers.lab.application;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import common.system.MessageKeys;
import common.utils.Binaries;
import models.system.System.PermissionsAllowed;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.MessagesApi;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;

@PermissionsAllowed
public class Upload extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
			final Path fileNamePath = Paths.get(filePart.getFilename());
			final String fileName = fileNamePath.getFileName().toString();
			@SuppressWarnings("unused")
			final String contentType = filePart.getContentType();
			final Path path = filePart.getFile().toPath();
			LOGGER.info("Receive: Path={} File name={} Content type={}", path, fileName, contentType);

			try {

				final Metadata metadata = Binaries.getMetadata(Files.readAllBytes(path));
				final String realContentType = metadata.get(HttpHeaders.CONTENT_TYPE);
				final Set<String> acceptContentTypes = new HashSet<>(Arrays.asList("image/png", "image/jpeg", "image/gif"));
				LOGGER.info("Accept : Content types={} Content type={}", acceptContentTypes, realContentType);
				if (!acceptContentTypes.contains(realContentType)) {

					throw new IllegalStateException(filePart.getFilename() + " is illegal.");
				}

				final Path uploadPath = Paths.get(System.getProperty("user.home")).resolve(UUID.randomUUID() + fileName);
				Files.move(path, uploadPath);
				LOGGER.info("Upload : Path={} File name={} Content type={}", uploadPath, fileName, realContentType);

				flash("imageSuccess", "<strong>" + messages.get(lang(), MessageKeys.SUCCESS) + "</strong> " + messages.get(lang(), MessageKeys.FILE) + " uploaded");
				return index();
			} catch (IOException | IllegalStateException e) {

				flash("imageWarning", "<strong>" + messages.get(lang(), MessageKeys.WARNING) + "</strong> " + messages.get(lang(), MessageKeys.SYSTEM_ERROR_X__DATA_ILLEGAL, filePart.getFilename()));
				return index();
			} finally {

				try {

					Files.deleteIfExists(path);
				} catch (IOException e) {
				}
			}
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
