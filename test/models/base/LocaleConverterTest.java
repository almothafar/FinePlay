package models.base;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import javax.persistence.AttributeConverter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LocaleConverterTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testConvertToDatabaseColumn() {

		final AttributeConverter<Locale, String> converter = new LocaleConverter();

		assertThat("", converter.convertToDatabaseColumn(null), nullValue());
		assertThat("", converter.convertToDatabaseColumn(Locale.ENGLISH), is("en"));
		assertThat("", converter.convertToDatabaseColumn(Locale.US), is("en-US"));
		assertThat("", converter.convertToDatabaseColumn(Locale.JAPANESE), is("ja"));
		assertThat("", converter.convertToDatabaseColumn(Locale.JAPAN), is("ja-JP"));
	}

	@Test
	public void testConvertToEntityAttribute() {

		final AttributeConverter<Locale, String> converter = new LocaleConverter();

		assertThat("", converter.convertToEntityAttribute(null), nullValue());
		assertThat("", converter.convertToEntityAttribute("en"), is(Locale.ENGLISH));
		assertThat("", converter.convertToEntityAttribute("en-US"), is(Locale.US));
		assertThat("", converter.convertToEntityAttribute("ja"), is(Locale.JAPANESE));
		assertThat("", converter.convertToEntityAttribute("ja-JP"), is(Locale.JAPAN));
	}
}
