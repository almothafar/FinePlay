package apis.system;

import java.lang.invoke.MethodHandles;

import org.slf4j.LoggerFactory;

import play.filters.csp.CSPReportBodyParser;
import play.filters.csp.JavaCSPReport;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Http.Request;

public class CSPReport extends Controller {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private MessagesApi messagesApi;

	@BodyParser.Of(CSPReportBodyParser.class)
	public Result log(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		messages.lang();

		final JavaCSPReport report = request.body().as(JavaCSPReport.class);
		LOGGER.warn("CSP violation: violatedDirective = {}, blockedUri = {}, originalPolicy = {}", report.violatedDirective(), report.blockedUri(), report.originalPolicy());

		return ok();
	}
}
