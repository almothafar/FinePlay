package controllers.resetuser.change;

import static org.fluentlenium.core.filter.FilterConstructor.withId;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import play.test.TestBrowser;
import test.Page;

public class ChangePage extends Page {

	public ChangePage(TestBrowser browser) {

		super(browser);
	}

	@Override
	public String getUrl() {

		return "resetuser/owner";
	}

	@Override
	public void isAt() {

		assertThat("", getBrowser().url(), startsWith(getUrl()));
	}

	public void inputPassword(final String password) {

		getBrowser().$(withId("password")).fill().with(password);
	}

	public void inputRePassword(final String rePassword) {

		getBrowser().$(withId("rePassword")).fill().with(rePassword);
	}

	public String getPassword() {

		return getBrowser().$(withId("password")).get(0).value();
	}

	public String getRePassword() {

		return getBrowser().$(withId("rePassword")).get(0).value();
	}

	public String contentTitle() {

		return getBrowser().$("h4.card-title").first().text();
	};

	public void clickChange() {

		getBrowser().$(withId("changeButton")).click();
	}
}
