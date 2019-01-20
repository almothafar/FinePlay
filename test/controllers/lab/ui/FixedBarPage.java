package controllers.lab.ui;

import static org.fluentlenium.core.filter.FilterConstructor.withId;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import play.test.TestBrowser;
import test.Page;

public class FixedBarPage extends Page {

	public FixedBarPage(TestBrowser browser) {

		super(browser);
	}

	@Override
	public String getUrl() {

		return "lab/ui?ui=fixedbar";
	}

	@Override
	public void isAt() {

		assertThat("", getBrowser().url(), is(getUrl()));
	}

	public void scrollWithId(final String id) {

		getBrowser().$(withId(id)).scrollIntoView();
	}
}
