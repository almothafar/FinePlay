package apis.system;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.system.SlideInfoFormContent;
import models.system.System.PermissionsAllowed;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Slide extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private WSClient ws;

	@Inject
	private FormFactory formFactory;

	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result index() {

		final Form<SlideInfoFormContent> slideInfoForm = formFactory.form(SlideInfoFormContent.class).bindFromRequest();
		if (!slideInfoForm.hasErrors()) {

			final SlideInfoFormContent slideInfoFormContent = slideInfoForm.get();

			final String url = slideInfoFormContent.getUrl();
			final String returnUrl = slideInfoFormContent.getReturnUrl();

			final Map<String, String> slideInfo = new HashMap<>();
			slideInfo.put(SlideInfoFormContent.URL, url);
			slideInfo.put(SlideInfoFormContent.RETURNURL, returnUrl);

			return ok(views.html.system.slide.render(slideInfo));
		} else {

			throw new RuntimeException(slideInfoForm.errors().toString());
		}
	}
}
