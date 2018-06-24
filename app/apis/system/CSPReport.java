package apis.system;

import java.lang.invoke.MethodHandles;

import org.slf4j.LoggerFactory;

import play.filters.csp.CSPReportBodyParser;
import play.filters.csp.JavaCSPReport;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

public class CSPReport extends Controller {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@BodyParser.Of(CSPReportBodyParser.class)
	public Result log() {

		final JavaCSPReport report = request().body().as(JavaCSPReport.class);
		LOGGER.warn("CSP violation: violatedDirective = {}, blockedUri = {}, originalPolicy = {}", report.violatedDirective(), report.blockedUri(), report.originalPolicy());

		return ok();
	}
}
