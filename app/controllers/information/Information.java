package controllers.information;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.annotation.Nonnull;

import common.utils.Binaries;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import play.i18n.Messages;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

@PermissionsAllowed(value = { Permission.READ })
public class Information extends Controller {

	@Inject
	private MessagesApi messagesApi;

	@Authenticated(common.core.Authenticator.class)
	public Result creator(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final String myImageUrl = "https://www.gravatar.com/avatar/" + toHashedEMail("hiro20v++@icloud.com") + "?s=200";
		return ok(views.html.information.creator.render(myImageUrl, request, lang, messages));
	}

	public Result license(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final String myImageUrl = "https://www.gravatar.com/avatar/" + toHashedEMail("hiro20v++@icloud.com") + "?s=200";
		return ok(views.html.information.license.render(myImageUrl, request, lang, messages));
	}

	@Nonnull
	private static String toHashedEMail(@Nonnull final String eMail) {

		Objects.requireNonNull(eMail);

		MessageDigest messageDigest = null;
		try {

			messageDigest = MessageDigest.getInstance("MD5");
		} catch (final NoSuchAlgorithmException e) {

			throw new IllegalStateException(e);
		}

		messageDigest.update(eMail.getBytes(StandardCharsets.UTF_8));
		final byte[] hash = messageDigest.digest();
		if (hash.length != 16) {

			throw new IllegalStateException(": " + hash.length);
		}

		return Binaries.toHexString(hash);
	}
}
