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
import play.i18n.Lang;

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
	public void testNormalize() {

		assertThat("", Locales.normalize(Locale.ENGLISH), is(Locale.US));
		assertThat("", Locales.normalize(Locale.US), is(Locale.US));

		assertThat("", Locales.normalize(Locale.JAPANESE), is(Locale.JAPAN));
		assertThat("", Locales.normalize(Locale.JAPAN), is(Locale.JAPAN));

//		assertThat("", Locales.normalize(Locale.CHINESE), is(Locale.CHINA));
//		assertThat("", Locales.normalize(Locale.CHINA), is(Locale.CHINA));
//		assertThat("", Locales.normalize(new Locale("zh", "HK")), is(new Locale("zh", "HK")));
//		assertThat("", Locales.normalize(new Locale("zh", "MO")), is(new Locale("zh", "MO")));
		assertThat("", Locales.normalize(Locale.CHINESE), is(Locale.US));
		assertThat("", Locales.normalize(Locale.CHINA), is(Locale.US));
		assertThat("", Locales.normalize(new Locale("zh", "HK")), is(Locale.US));
		assertThat("", Locales.normalize(new Locale("zh", "MO")), is(Locale.US));

//		assertThat("", Locales.normalize(Locale.TAIWAN), is(Locale.TAIWAN));
		assertThat("", Locales.normalize(Locale.TAIWAN), is(Locale.US));

//		assertThat("", Locales.normalize(Locale.KOREAN), is(Locale.KOREA));
//		assertThat("", Locales.normalize(Locale.KOREA), is(Locale.KOREA));
		assertThat("", Locales.normalize(Locale.KOREAN), is(Locale.US));
		assertThat("", Locales.normalize(Locale.KOREA), is(Locale.US));
	}

	@Test
	public void testToDirection() {

		assertThat("", Locales.toDirection(Locale.US), is(Direction.LTR));
		assertThat("", Locales.toDirection(Locale.JAPAN), is(Direction.LTR));
		assertThat("", Locales.toDirection(Locale.CHINA), is(Direction.LTR));
		assertThat("", Locales.toDirection(Locale.TAIWAN), is(Direction.LTR));
		assertThat("", Locales.toDirection(Locale.KOREA), is(Direction.LTR));
		assertThat("", Locales.toDirection(new Locale("ar", "AE")), is(Direction.RTL));
		assertThat("", Locales.toDirection(new Locale("iw", "IL")), is(Direction.RTL));
	}

	@Test
	public void testGetLanguage() {

		assertThat("", Locale.US.getLanguage(), is(new Lang(Locale.US).language()));
		assertThat("", Locale.JAPAN.getLanguage(), is(new Lang(Locale.JAPAN).language()));
		assertThat("", Locale.CHINA.getLanguage(), is(new Lang(Locale.CHINA).language()));
		assertThat("", Locale.TAIWAN.getLanguage(), is(new Lang(Locale.TAIWAN).language()));
		assertThat("", Locale.KOREA.getLanguage(), is(new Lang(Locale.KOREA).language()));
		assertThat("", new Locale("ar", "AE").getLanguage(), is(new Lang(new Locale("ar", "AE")).language()));
		assertThat("", new Locale("iw", "IL").getLanguage(), is(new Lang(new Locale("iw", "IL")).language()));
	}

	@Test
	public void testGetCountry() {

		assertThat("", Locale.US.getCountry(), is(new Lang(Locale.US).country()));
		assertThat("", Locale.JAPAN.getCountry(), is(new Lang(Locale.JAPAN).country()));
		assertThat("", Locale.CHINA.getCountry(), is(new Lang(Locale.CHINA).country()));
		assertThat("", Locale.TAIWAN.getCountry(), is(new Lang(Locale.TAIWAN).country()));
		assertThat("", Locale.KOREA.getCountry(), is(new Lang(Locale.KOREA).country()));
		assertThat("", new Locale("ar", "AE").getCountry(), is(new Lang(new Locale("ar", "AE")).country()));
		assertThat("", new Locale("iw", "IL").getCountry(), is(new Lang(new Locale("iw", "IL")).country()));
	}

	@Test
	public void testToLanguageTag() {

		assertThat("", Locale.US.toLanguageTag(), is(new Lang(Locale.US).code()));
		assertThat("", Locale.JAPAN.toLanguageTag(), is(new Lang(Locale.JAPAN).code()));
		assertThat("", Locale.CHINA.toLanguageTag(), is(new Lang(Locale.CHINA).code()));
		assertThat("", Locale.TAIWAN.toLanguageTag(), is(new Lang(Locale.TAIWAN).code()));
		assertThat("", Locale.KOREA.toLanguageTag(), is(new Lang(Locale.KOREA).code()));
		assertThat("", new Locale("ar", "AE").toLanguageTag(), is(new Lang(new Locale("ar", "AE")).code()));
		assertThat("", new Locale("iw", "IL").toLanguageTag(), is(new Lang(new Locale("iw", "IL")).code()));
	}
}
