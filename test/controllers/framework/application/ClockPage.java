package controllers.framework.application;

import static org.fluentlenium.core.filter.FilterConstructor.withId;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import play.test.TestBrowser;
import test.Page;

public class ClockPage extends Page {

	public ClockPage(TestBrowser browser) {

		super(browser);
	}

	@Override
	public String getUrl() {

		return "framework/application?state=clock";
	}

	@Override
	public void isAt() {

		assertThat("", getBrowser().url(), is(getUrl()));
	}

	public String getServerDateTime() {

		return getBrowser().$(withId("serverDateTime")).textContent();
	}

	public String getClientDateTime() {

		return getBrowser().$(withId("clientDateTime")).textContent();
	}
}
