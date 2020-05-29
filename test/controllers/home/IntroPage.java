package controllers.home;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import play.test.TestBrowser;
import test.Page;

public class IntroPage extends Page {

	public IntroPage(TestBrowser browser) {

		super(browser);
	}

	@Override
	public String getUrl() {

		return "home";
	}

	@Override
	public void isAt() {

		assertThat("", getBrowser().url(), is(getUrl()));
	}

	public void signOut() {

		getBrowser().goTo("");
		// getBrowser().$(withId("signOutLink")).click();
	}
}
