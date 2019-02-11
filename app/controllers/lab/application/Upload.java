package controllers.lab.application;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
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
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.libs.Files.TemporaryFile;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import javax.annotation.Nonnull;
import play.i18n.Messages;
import play.mvc.Http.Request;

@PermissionsAllowed
public class Upload extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private MessagesApi messagesApi;

	public Result index(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return ok(views.html.lab.application.upload.render(new HashMap<>(), request, lang, messages));
	}

	@RequireCSRFCheck
	public Result form(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
		final play.mvc.Http.MultipartFormData.FilePart<TemporaryFile> filePart = body.getFile("inputName");
		if (filePart != null) {

			@SuppressWarnings("unused")
			final String fileName = filePart.getFilename();
			@SuppressWarnings("unused")
			final String contentType = filePart.getContentType();
			final TemporaryFile tempFile = filePart.getRef();
			final Path path = tempFile.path();
			LOGGER.info("" + path);

			final Map<String, String> alertInfo = new HashMap<String, String>() {
				{
					put("success", "<strong>" + messages.at(MessageKeys.SUCCESS) + "</strong> " + messages.at(MessageKeys.FILE) + " uploaded");
				}
			};

			return ok(views.html.lab.application.upload.render(alertInfo, request, lang, messages));
		} else {

			final Map<String, String> alertInfo = new HashMap<String, String>() {
				{
					put("warning", "<strong>" + messages.at(MessageKeys.WARNING) + "</strong> Missing " + messages.at(MessageKeys.FILE));
				}
			};

			return ok(views.html.lab.application.upload.render(alertInfo, request, lang, messages));
		}
	}

	@RequireCSRFCheck
	public Result image(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
		final play.mvc.Http.MultipartFormData.FilePart<TemporaryFile> filePart = body.getFile("inputName");
		if (filePart != null) {

			final Path fileNamePath = Paths.get(filePart.getFilename());
			final String fileName = fileNamePath.getFileName().toString();
			final String contentType = filePart.getContentType();
			final TemporaryFile tempFile = filePart.getRef();
			final Path path = tempFile.path();
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

				final Map<String, String> alertInfo = new HashMap<String, String>() {
					{
						put("imageSuccess", "<strong>" + messages.at(MessageKeys.SUCCESS) + "</strong> " + messages.at(MessageKeys.FILE) + " uploaded");
					}
				};

				return ok(views.html.lab.application.upload.render(alertInfo, request, lang, messages));
			} catch (IOException | IllegalStateException e) {

				final Map<String, String> alertInfo = new HashMap<String, String>() {
					{
						put("imageWarning", "<strong>" + messages.at(MessageKeys.WARNING) + "</strong> " + messages.at(MessageKeys.SYSTEM_ERROR_X__DATA_ILLEGAL, filePart.getFilename()));
					}
				};

				return ok(views.html.lab.application.upload.render(alertInfo, request, lang, messages));
			} finally {

				try {

					Files.deleteIfExists(path);
				} catch (IOException e) {
				}
			}
		} else {

			final Map<String, String> alertInfo = new HashMap<String, String>() {
				{
					put("imageWarning", "<strong>" + messages.at(MessageKeys.WARNING) + "</strong> Missing " + messages.at(MessageKeys.FILE));
				}
			};

			return ok(views.html.lab.application.upload.render(alertInfo, request, lang, messages));
		}
	}

	@BodyParser.Of(value = BodyParser.MultipartFormData.class)
	@RequireCSRFCheck
	public Result direct(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		messages.lang();

		final MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
		@SuppressWarnings("unused")
		final Map<String, String[]> form = body.asFormUrlEncoded();
		final play.mvc.Http.MultipartFormData.FilePart<TemporaryFile> filePart = body.getFile("inputName");

		final TemporaryFile tempFile = filePart.getRef();
		final Path path = tempFile.path();
		LOGGER.info("" + path);

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();
		result.put("status", "success");
		result.put("message", "<strong>" + messages.at(MessageKeys.SUCCESS) + "</strong> " + messages.at(MessageKeys.FILE) + " uploaded");

		return ok(result);
	}
}
