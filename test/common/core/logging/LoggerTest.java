package common.core.logging;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.invoke.MethodHandles;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.test.WithApplication;
import test.Appender;

/**
 *
 * Simple (JUnit) tests that can call all parts of a play app. If you are
 * interested in mocking a whole application, see the wiki for more details.
 *
 */
public class LoggerTest extends WithApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
	public void test() {

		final ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(LoggerTest.class);

		final Appender testAppender = new Appender();
		root.addAppender(testAppender);

		LOGGER.info("a{}c", "b");
		LOGGER.info("1{}3", "2");

		assertThat("", "abc", is(testAppender.getEvents().get(0).getFormattedMessage()));
		assertThat("", "123", is(testAppender.getEvents().get(1).getFormattedMessage()));
	}
}
