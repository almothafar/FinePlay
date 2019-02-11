package apis.system;

import java.lang.invoke.MethodHandles;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.utils.Exceptions;
import models.system.PaperInfoFormContent;
import models.system.SlideInfoFormContent;
import models.system.System.PermissionsAllowed;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.CSRF;
import play.filters.csrf.CSRF.Token;
import play.i18n.Messages;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Result;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Slide extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private MessagesApi messagesApi;

	@Inject
	private FormFactory formFactory;

	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result index(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final Form<SlideInfoFormContent> slideInfoForm = formFactory.form(SlideInfoFormContent.class).bindFromRequest(request);
		if (!slideInfoForm.hasErrors()) {

			final SlideInfoFormContent slideInfoFormContent = slideInfoForm.get();

			final String url = slideInfoFormContent.getUrl();
			final String returnUrl = slideInfoFormContent.getReturnUrl();

			final Map<String, String> slideInfo = new HashMap<>();
			slideInfo.put(SlideInfoFormContent.URL, createURL(url, getParams(slideInfoForm.rawData()), request));
			slideInfo.put(SlideInfoFormContent.RETURNURL, returnUrl);

			return ok(views.html.system.slide.render(slideInfo, request, lang, messages));
		} else {

			throw new RuntimeException(slideInfoForm.errors().toString());
		}
	}

	private Map<String, String> getParams(final Map<String, String> rawData) {

		final Map<String, String> params = new HashMap<>(rawData);
		params.remove(PaperInfoFormContent.URL);
		params.remove(PaperInfoFormContent.RETURNURL);

		return Collections.unmodifiableMap(params);
	}

	private String createURL(@Nonnull final String url, final Map<String, String> params, final Request request) {

		final StringBuilder builder = new StringBuilder(url);
		builder.append("?").append(getQuery(params));
		if (isSelf(url, request)) {

			builder.append("&").append(getToken(CSRF.getToken(request).get()));
		}

		return builder.toString();
	}

	private boolean isSelf(@Nonnull final String url, final Request request) {

		final boolean isContainSchema = url.startsWith("http://") || url.startsWith("https://");
		final boolean isContainHost = url.contains(request.host());

		final boolean isOuter = isContainSchema && !isContainHost;
		return !isOuter;
	}

	private String getQuery(@Nonnull final Map<String, String> params) {

		return params.entrySet().stream()//
				.map(e -> e.getKey() + "=" + Exceptions.callQuietly(() -> URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8.name())).replace("+", "%20"))//
				.collect(Collectors.joining("&"));
	}

	private String getToken(@Nonnull final Token token) {

		return token.name() + "=" + token.value();
	}
}
