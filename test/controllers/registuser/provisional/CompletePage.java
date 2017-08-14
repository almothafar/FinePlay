package controllers.registuser.provisional;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

import play.test.TestBrowser;
import test.Page;

public class CompletePage extends Page {

	public CompletePage(TestBrowser browser) {

		super(browser);
	}

	@Override
	public String getUrl() {

		return "registuser/apply";
	}

	@Override
	public void isAt() {

		assertThat("", getBrowser().url(), startsWith(getUrl()));
	}

	public String contentTitle() {

		return getBrowser().$("h4.card-title").first().text();
	};

	public void clickTop() {

		getBrowser().goTo("");
		// getBrowser().$(withId("topButton")).click();
	}
}
