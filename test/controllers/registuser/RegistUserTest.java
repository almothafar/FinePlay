package controllers.registuser;

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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import controllers.home.IntroPage;
import controllers.registuser.provisional.CompletePage;
import controllers.user.IndexPage;
import test.Appender;
import test.BrowserLocaleFilter;
import test.Captures;
import test.Condition;
import test.Counter;
import test.Filter;
import test.LoggerNames;

public class RegistUserTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testApply() {

		@SuppressWarnings("serial")
		final List<Condition> conditions = Arrays.asList(new Condition[]{ //
				new Condition("", Locale.US, new HashMap<String, Object>() {
					{
						put("title.signin", "Sign in | fine✿play");
						put("title.registuser", "Regist user | fine✿play");
						put("registuser.please_regist", "Please regist user.");
						put("registuser.provisional.complete", "Provisional regist completion");
						put("registuser.regular.complete", "Regular regist completion");
						put("title.home", "Home | fine✿play");
					}
				}), //
				new Condition("", Locale.JAPAN, new HashMap<String, Object>() {
					{
						put("title.signin", "サインイン | ファイン✿プレイ");
						put("title.registuser", "ユーザー登録 | ファイン✿プレイ");
						put("registuser.please_regist", "ユーザー登録してください。");
						put("registuser.provisional.complete", "仮登録完了");
						put("registuser.regular.complete", "本登録完了");
						put("title.home", "ホーム | ファイン✿プレイ");
					}
				}) //
		});

		final Path capturePath = Captures.getFolderPath();

		conditions.stream().filter(new Filter()).filter(new BrowserLocaleFilter()).forEach(condition -> {

			running(testServer(3333, fakeApplication(inMemoryDatabase())), FIREFOX, browser -> {

				final Counter counter = new Counter(3);
				final Locale locale = condition.getLocale();

				final ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(LoggerNames.PLAY_MAILER);

				final Appender testAppender = new Appender();
				root.addAppender(testAppender);

				final IndexPage indexPage = new IndexPage(browser);
				browser.goTo(indexPage.getUrl());
				indexPage.isAt();
				assertThat("", browser.window().title(), anyConditionOf(conditions, "title.signin", String.class));
				indexPage.takeScreenshot(capturePath, locale, counter, "Sign In");
				indexPage.clickRegistUser();

				final RegistPage registPage = new RegistPage(browser);
				browser.await().atMost(10, TimeUnit.SECONDS).until(() -> browser.url().startsWith(registPage.getUrl()));
				registPage.isAt();
				assertThat("", registPage.contentTitle(), is(condition.get("registuser.please_regist").toString()));
				registPage.inputUserId("regist@example.com");
				assertThat("", registPage.getUserId(), is("regist@example.com"));
				registPage.inputPassword("1!aAregist");
				assertThat("", registPage.getPassword(), is("1!aAregist"));
				registPage.inputRePassword("1!aAregist");
				assertThat("", registPage.getRePassword(), is("1!aAregist"));
				registPage.takeScreenshot(capturePath, locale, counter, "Regist User - Input");
				registPage.clickApply();

				final CompletePage provisionalCompletePage = new CompletePage(browser);
				browser.await().atMost(10, TimeUnit.SECONDS).until(() -> browser.url().startsWith(provisionalCompletePage.getUrl()));
				provisionalCompletePage.isAt();
				assertThat("", provisionalCompletePage.contentTitle(), is(condition.get("registuser.provisional.complete").toString()));
				provisionalCompletePage.takeScreenshot(capturePath, locale, counter, "Provisional - Compleate");
				final String provisionalMailLog = testAppender.getEvents().stream()//
						.filter(event -> event.getFormattedMessage().contains("<html"))//
						.findFirst()//
						.map(event -> event.getFormattedMessage())//
						.get();
				final Element registLink = Jsoup.parse(provisionalMailLog).select("a[href ^= http://localhost:9000/registuser/regular/]").get(0);
				final String regularUrl = registLink.attr("href").replace("http://localhost:9000", "http://localhost:3333");
				provisionalCompletePage.clickTop();

				final IndexPage indexPage2 = new IndexPage(browser);
				browser.await().atMost(10, TimeUnit.SECONDS).until(() -> browser.url().startsWith(indexPage2.getUrl()));
				indexPage2.isAt();
				assertThat("", browser.window().title(), anyConditionOf(conditions, "title.signin", String.class));
				indexPage2.takeScreenshot(capturePath, locale, counter, "Sign In - After Provisional.png");
				browser.goTo(regularUrl);

				final controllers.registuser.reqular.CompletePage reqularCompletePage = new controllers.registuser.reqular.CompletePage(browser);
				browser.await().atMost(10, TimeUnit.SECONDS).until(() -> browser.url().startsWith(reqularCompletePage.getUrl()));
				reqularCompletePage.isAt();
				assertThat("", reqularCompletePage.contentTitle(), is((String) condition.get("registuser.regular.complete")));
				reqularCompletePage.takeScreenshot(capturePath, locale, counter, "Regular - Compleate");
				reqularCompletePage.clickTop();

				final IndexPage indexPage3 = new IndexPage(browser);
				browser.await().atMost(10, TimeUnit.SECONDS).until(() -> browser.url().startsWith(indexPage3.getUrl()));
				indexPage3.isAt();
				assertThat("", browser.window().title(), anyConditionOf(conditions, "title.signin", String.class));
				indexPage3.takeScreenshot(capturePath, locale, counter, "Sign In - After Regular");
				indexPage3.inputUserId("regist@example.com");
				indexPage3.inputPassword("1!aAregist");
				indexPage3.takeScreenshot(capturePath, locale, counter, "Sign In - Input");
				indexPage3.clickSignIn();

				final IntroPage introPage = new IntroPage(browser);
				browser.await().atMost(10, TimeUnit.SECONDS).until(() -> browser.url().startsWith(introPage.getUrl()));
				introPage.isAt();
				assertThat("", browser.window().title(), is((String) condition.get("title.home")));
				introPage.takeScreenshot(capturePath, locale, counter, "Sign In - Success");
			});
		});
	}
}
