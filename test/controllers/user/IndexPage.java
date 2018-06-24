package controllers.user;

import static org.fluentlenium.core.filter.FilterConstructor.withId;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import play.test.TestBrowser;
import test.Page;

public class IndexPage extends Page {

	public IndexPage(TestBrowser browser) {

		super(browser);
	}

	@Override
	public String getUrl() {

		return "";
	}

	@Override
	public void isAt() {

		assertThat("", getBrowser().url(), is(getUrl()));
	}

	public void inputUserId(final String userId) {

		getBrowser().$(withId("userId")).fill().with(userId);
	}

	public void inputPassword(final String password) {

		getBrowser().$(withId("password")).fill().with(password);
	}

	public String getUserId() {

		return getBrowser().$(withId("userId")).get(0).value();
	}

	public String getPassword() {

		return getBrowser().$(withId("password")).get(0).value();
	}

	public void clickSignIn() {

		getBrowser().$(withId("signInButton")).click();
	}

	public void clickRegistUser() {

		getBrowser().$("a[href = '/registuser']").click();
	}

	public void clickResetUser() {

		getBrowser().$("a[href = '/resetuser']").click();
	}
}
