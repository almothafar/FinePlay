package views.framework.application;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static play.test.Helpers.contentAsString;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.utils.Locales;
import common.utils.Sessions;
import models.user.User;
import models.user.User.Role;
import models.user.User.Theme;
import play.mvc.Http;
import play.mvc.Http.Cookie;
import play.mvc.Http.RequestBuilder;
import play.test.Helpers;
import play.test.WithApplication;
import play.twirl.api.Content;
import test.Condition;
import test.Filter;
import test.LoggerNames;

/**
 *
 * Simple (JUnit) tests that can call all parts of a play app. If you are
 * interested in mocking a whole application, see the wiki for more details.
 *
 */
public class I18nTest extends WithApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private Http.Context context;

	private Http.Context getMockContext(final Locale locale) {

		final RequestBuilder builder = new RequestBuilder();
		builder.header("User-Agent", "mocked user-agent");
		builder.session(User.USER_ID, "mockUser");
		builder.session(User.ROLES, Sessions.toValue(Arrays.asList(new Role[] { Role.ADMIN })));
		builder.session(models.user.User.ZONE_ID, "UTC");
		builder.session(models.user.User.THEME, Theme.DEFAULT.name());
		builder.cookie(Cookie.builder(Helpers.stubMessagesApi().langCookieName(), locale.toLanguageTag()).build());
		builder.cookie(Cookie.builder(models.user.User.THEME, Theme.DEFAULT.name()).build());

		final Http.Context mockContext = spy(Helpers.httpContext(builder.build()));
		when(mockContext.lang()).thenReturn(Locales.toLang(locale));

		return mockContext;
	}

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
	public void render() {

		@SuppressWarnings("serial")
		final List<Condition> conditions = Arrays.asList(new Condition[] { //
				new Condition("", Locale.US, new HashMap<String, Object>() {
					{
						put("message.code", "Lang(en_US)");
					}
				}), //
				new Condition("", Locale.JAPAN, new HashMap<String, Object>() {
					{
						put("message.code", "Lang(ja_JP)");
					}
				}) //
		});

		conditions.stream().filter(new Filter()).forEach(condition -> {

			context = getMockContext(condition.getLocale());
			Http.Context.current.set(context);

			final Content html = views.html.framework.application.i18n.render(new HashMap<>());

			assertThat("text/html", is(html.contentType()));
			@SuppressWarnings("unused")
			final ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(LoggerNames.PLAY_MAILER);
			LOGGER.info(contentAsString(html));
			assertTrue(contentAsString(html).contains(condition.get("message.code")));
		});
	}
}
