package controllers.framework.application;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static play.test.Helpers.FIREFOX;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;
import static test.Condition.anyConditionOf;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import controllers.home.IntroPage;
import controllers.user.IndexPage;
import test.Captures;
import test.Condition;
import test.Counter;
import test.Filter;

public class ApplicationTest {

	@Test
	public void testFixedClock() {

		@SuppressWarnings("serial")
		final List<Condition> conditions = Arrays.asList(new Condition[] { //
				new Condition("", Locale.US, new HashMap<String, Object>() {
					{
						put("title.signin", "Sign in | fine✿play");
						put("user.id", "admin@example.com");
						put("user.password", "admin1!aA");
						//
						put("title.clock", "Application | fine✿play");
						put("server", "Jan 1, 2020, 1:01:01 AM");
						put("client", "Dec 31, 2019, 5:01:01 PM");
						put("legacy-server", "Jan 1, 2020 1:01:01 AM");
						put("legacy-client", "Dec 31, 2019 5:01:01 PM");
					}
				}), //
				new Condition("", Locale.JAPAN, new HashMap<String, Object>() {
					{
						put("title.signin", "サインイン | ファイン✿プレイ");
						put("user.id", "adminjajp@example.com");
						put("user.password", "adminjajp1!aA");
						//
						put("title.clock", "Application | ファイン✿プレイ");
						put("server", "2020/01/01 1:01:01");
						put("client", "2020/01/01 10:01:01");
						put("legacy-server", "2020/01/01 1:01:01");
						put("legacy-client", "2020/01/01 10:01:01");
					}
				}) //
		});

		final Path capturePath = Captures.getFolderPath();

		conditions.stream().filter(new Filter()).forEach(condition -> {

			running(testServer(3333, fakeApplication(inMemoryDatabase())), FIREFOX, browser -> {

				final Counter counter = new Counter(3);
				final Locale locale = condition.getLocale();

				final IndexPage signInPage = new IndexPage(browser);

				browser.goTo(signInPage.getUrl());
				signInPage.isAt();
				assertThat("", browser.window().title(), anyConditionOf(conditions, "title.signin", String.class));
				signInPage.takeScreenshot(capturePath, locale, counter, "Sign In");
				signInPage.inputUserId(condition.get("user.id"));
				assertThat("", signInPage.getUserId(), is(condition.get("user.id").toString()));
				signInPage.inputPassword(condition.get("user.password"));
				assertThat("", signInPage.getPassword(), is(condition.get("user.password").toString()));
				signInPage.takeScreenshot(capturePath, locale, counter, "Sign In - Input");
				signInPage.clickSignIn();

				final IntroPage introPage = new IntroPage(browser);
				browser.await().atMost(10, TimeUnit.SECONDS).until(() -> browser.url().startsWith(introPage.getUrl()));

				final ClockPage clockPage = new ClockPage(browser);
				browser.goTo(clockPage.getUrl());
				browser.await().atMost(10, TimeUnit.SECONDS).until(() -> browser.url().startsWith(clockPage.getUrl()));
				clockPage.isAt();
				assertThat("", browser.window().title(), is((String) condition.get("title.clock")));
				assertThat("", clockPage.getServerDateTime(), anyOf(is((String) condition.get("legacy-server")), is((String) condition.get("server"))));
				assertThat("", clockPage.getClientDateTime(), anyOf(is((String) condition.get("legacy-client")), is((String) condition.get("client"))));
				clockPage.takeScreenshot(capturePath, locale, counter, "Fixed clock - Success");
			});
		});
	}
}
