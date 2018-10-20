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
		assertThat("", Locales.toDirection(new Locale("ar", "AE")), is(Direction.RTL));
		assertThat("", Locales.toDirection(new Locale("iw", "IL")), is(Direction.RTL));
	}

	@Deprecated(since = "Play framework 3.0", forRemoval = true)
	@Test
	public void testGetLanguage() {

		assertThat("", Locale.US.getLanguage(), is(Locales.toLang(Locale.US).language()));
		assertThat("", Locale.JAPAN.getLanguage(), is(Locales.toLang(Locale.JAPAN).language()));
		assertThat("", Locale.SIMPLIFIED_CHINESE.getLanguage(), is(Locales.toLang(Locale.SIMPLIFIED_CHINESE).language()));
		assertThat("", Locale.TRADITIONAL_CHINESE.getLanguage(), is(Locales.toLang(Locale.TRADITIONAL_CHINESE).language()));
		assertThat("", Locale.KOREA.getLanguage(), is(Locales.toLang(Locale.KOREA).language()));
		assertThat("", new Locale("ar", "AE").getLanguage(), is(Locales.toLang(new Locale("ar", "AE")).language()));
		assertThat("", new Locale("iw", "IL").getLanguage(), is(Locales.toLang(new Locale("iw", "IL")).language()));
	}

	@Deprecated(since = "Play framework 3.0", forRemoval = true)
	@Test
	public void testGetCountry() {

		assertThat("", Locale.US.getCountry(), is(Locales.toLang(Locale.US).country()));
		assertThat("", Locale.JAPAN.getCountry(), is(Locales.toLang(Locale.JAPAN).country()));
		assertThat("", Locale.SIMPLIFIED_CHINESE.getCountry(), is(Locales.toLang(Locale.SIMPLIFIED_CHINESE).country()));
		assertThat("", Locale.TRADITIONAL_CHINESE.getCountry(), is(Locales.toLang(Locale.TRADITIONAL_CHINESE).country()));
		assertThat("", Locale.KOREA.getCountry(), is(Locales.toLang(Locale.KOREA).country()));
		assertThat("", new Locale("ar", "AE").getCountry(), is(Locales.toLang(new Locale("ar", "AE")).country()));
		assertThat("", new Locale("iw", "IL").getCountry(), is(Locales.toLang(new Locale("iw", "IL")).country()));
	}

	@Deprecated(since = "Play framework 3.0", forRemoval = true)
	@Test
	public void testToLanguageTag() {

		assertThat("", Locale.US.toLanguageTag(), is(Locales.toLang(Locale.US).code()));
		assertThat("", Locale.JAPAN.toLanguageTag(), is(Locales.toLang(Locale.JAPAN).code()));
		assertThat("", Locale.SIMPLIFIED_CHINESE.toLanguageTag(), is(Locales.toLang(Locale.SIMPLIFIED_CHINESE).code()));
		assertThat("", Locale.TRADITIONAL_CHINESE.toLanguageTag(), is(Locales.toLang(Locale.TRADITIONAL_CHINESE).code()));
		assertThat("", Locale.KOREA.toLanguageTag(), is(Locales.toLang(Locale.KOREA).code()));
		assertThat("", new Locale("ar", "AE").toLanguageTag(), is(Locales.toLang(new Locale("ar", "AE")).code()));
		assertThat("", new Locale("iw", "IL").toLanguageTag(), is(Locales.toLang(new Locale("iw", "IL")).code()));
	}
}
