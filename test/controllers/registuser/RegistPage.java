package controllers.registuser;

import static org.fluentlenium.core.filter.FilterConstructor.withId;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import play.test.TestBrowser;
import test.Page;

public class RegistPage extends Page {

	public RegistPage(TestBrowser browser) {

		super(browser);
	}

	@Override
	public String getUrl() {

		return "registuser";
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

	public void inputRePassword(final String rePassword) {

		getBrowser().$(withId("rePassword")).fill().with(rePassword);
	}

	public String getUserId() {

		return getBrowser().$(withId("userId")).value();
	}

	public String getPassword() {

		return getBrowser().$(withId("password")).value();
	}

	public String getRePassword() {

		return getBrowser().$(withId("rePassword")).value();
	}

	public String contentTitle() {

		return getBrowser().$("h4.card-title").first().text();
	};

	public void clickApply() {

		getBrowser().$(withId("applyButton")).click();
	}
}
