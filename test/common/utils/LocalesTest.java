package common.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import models.system.System.Direction;

public class LocalesTest {

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
	public void testToDirection() {

		assertThat("", Locales.toDirection(Locale.US), is(Direction.LTR));
		assertThat("", Locales.toDirection(Locale.JAPAN), is(Direction.LTR));
		assertThat("", Locales.toDirection(Locale.SIMPLIFIED_CHINESE), is(Direction.LTR));
		assertThat("", Locales.toDirection(Locale.TRADITIONAL_CHINESE), is(Direction.LTR));
		assertThat("", Locales.toDirection(Locale.KOREA), is(Direction.LTR));
		assertThat("", Locales.toDirection(new Locale("ar", "SA")), is(Direction.RTL));
	}
}
