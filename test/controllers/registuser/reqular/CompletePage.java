package controllers.registuser.reqular;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import play.test.TestBrowser;
import test.Page;

public class CompletePage extends Page {

	public CompletePage(TestBrowser browser) {

		super(browser);
	}

	@Override
	public String getUrl() {

		return "registuser/regular";
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
