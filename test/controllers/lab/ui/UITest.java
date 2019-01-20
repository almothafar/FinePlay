package controllers.lab.ui;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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

public class UITest {

	@Test
	public void testScroll() {

		@SuppressWarnings("serial")
		final List<Condition> conditions = Arrays.asList(new Condition[] { //
				new Condition("", Locale.US, new HashMap<String, Object>() {
					{
						put("title.signin", "Sign in | fine✿play");
						put("user.id", "admin@example.com");
						put("user.password", "admin1!aA");
						//
						put("title.fixedbar", "Lab | fine✿play");
					}
				}), //
				new Condition("", Locale.JAPAN, new HashMap<String, Object>() {
					{
						put("title.signin", "サインイン | ファイン✿プレイ");
						put("user.id", "adminjajp@example.com");
						put("user.password", "adminjajp1!aA");
						//
						put("title.fixedbar", "実験室 | ファイン✿プレイ");
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

				final FixedBarPage fixedBarPage = new FixedBarPage(browser);
				browser.goTo(fixedBarPage.getUrl());
				browser.await().atMost(10, TimeUnit.SECONDS).until(() -> browser.url().startsWith(fixedBarPage.getUrl()));
				fixedBarPage.isAt();
				assertThat("", browser.window().title(), is((String) condition.get("title.fixedbar")));

				fixedBarPage.scrollWithId("group_0");
				fixedBarPage.takeScreenshot(capturePath, locale, counter, "Group 1");
				fixedBarPage.scrollWithId("group_1");
				fixedBarPage.takeScreenshot(capturePath, locale, counter, "Group 2");
				fixedBarPage.scrollWithId("group_2");
				fixedBarPage.takeScreenshot(capturePath, locale, counter, "Group 3");
				fixedBarPage.scrollWithId("group_3");
				fixedBarPage.takeScreenshot(capturePath, locale, counter, "Group 4");
			});
		});
	}
}
