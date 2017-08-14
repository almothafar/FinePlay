package controllers.resetuser;

import static org.fluentlenium.core.filter.FilterConstructor.withId;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import play.test.TestBrowser;
import test.Page;

public class ResetPage extends Page {

	public ResetPage(TestBrowser browser) {

		super(browser);
	}

	@Override
	public String getUrl() {

		return "resetuser";
	}

	@Override
	public void isAt() {

		assertThat("", getBrowser().url(), is(getUrl()));
	}

	public void inputUserId(final String userId) {

		getBrowser().$(withId("userId")).fill().with(userId);
	}

	public String getUserId() {

		return getBrowser().$(withId("userId")).value();
	}

	public String contentTitle() {

		return getBrowser().$("h4.card-title").first().text();
	};

	public void clickApply() {

		getBrowser().$(withId("applyButton")).click();
	}
}
