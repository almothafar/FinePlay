package apis.system;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.system.PDFInfoFormContent;
import models.system.System.PermissionsAllowed;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class PDF extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private FormFactory formFactory;

	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result index() {

		final Form<PDFInfoFormContent> pdfInfoFormContent = formFactory.form(PDFInfoFormContent.class).bindFromRequest();
		if (!pdfInfoFormContent.hasErrors()) {

			final PDFInfoFormContent signInFormContent = pdfInfoFormContent.get();

			final String url = signInFormContent.getUrl();
			final String returnUrl = signInFormContent.getReturnUrl();

			final Map<String, String> pdfInfo = new HashMap<>();
			pdfInfo.put(PDFInfoFormContent.URL, url);
			pdfInfo.put(PDFInfoFormContent.RETURNURL, returnUrl);

			return ok(views.html.system.pdf.render(pdfInfo));
		} else {

			throw new RuntimeException(pdfInfoFormContent.errors().toString());
		}
	}
}
