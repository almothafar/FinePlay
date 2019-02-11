package apis.system;

import java.lang.invoke.MethodHandles;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.utils.Exceptions;
import models.system.PaperInfoFormContent;
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
public class Paper extends Controller {

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

		final Form<PaperInfoFormContent> paperInfoForm = formFactory.form(PaperInfoFormContent.class).bindFromRequest(request);
		if (!paperInfoForm.hasErrors()) {

			final PaperInfoFormContent paperInfoFormContent = paperInfoForm.get();

			final String url = paperInfoFormContent.getUrl();
			final String returnUrl = paperInfoFormContent.getReturnUrl();
			final String size = paperInfoFormContent.getSize();
			final boolean isPageNo = paperInfoFormContent.isPageNo();
			final boolean isPrint = paperInfoFormContent.isPrint();

			final Map<String, String> paperInfo = new HashMap<>();
			paperInfo.put(PaperInfoFormContent.URL, createURL(url, getParams(paperInfoForm.rawData()), request));
			paperInfo.put(PaperInfoFormContent.RETURNURL, returnUrl);
			paperInfo.put(PaperInfoFormContent.SIZE, size);
			paperInfo.put(PaperInfoFormContent.PAGENO, Objects.toString(isPageNo));
			paperInfo.put(PaperInfoFormContent.PRINT, Objects.toString(isPrint));

			return ok(views.html.system.paper.render(paperInfo, request, lang, messages));
		} else {

			throw new RuntimeException(paperInfoForm.errors().toString());
		}
	}

	private Map<String, String> getParams(final Map<String, String> rawData) {

		final Map<String, String> params = new HashMap<>(rawData);
		params.remove(PaperInfoFormContent.URL);
		params.remove(PaperInfoFormContent.RETURNURL);
		params.remove(PaperInfoFormContent.SIZE);
		params.remove(PaperInfoFormContent.PAGENO);
		params.remove(PaperInfoFormContent.PRINT);

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
