package controllers.framework.application;

import static org.fluentlenium.core.filter.FilterConstructor.withId;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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

		return getBrowser().$(withId("serverDateTime")).get(0).textContent();
	}

	public String getClientDateTime() {

		return getBrowser().$(withId("clientDateTime")).get(0).textContent();
	}
}
