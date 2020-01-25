package models.base;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.time.Year;

import javax.persistence.AttributeConverter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class YearConverterTest {

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

		final AttributeConverter<Year, Integer> converter = new YearConverter();

		assertThat("", converter.convertToDatabaseColumn(null), nullValue());
		assertThat("", converter.convertToDatabaseColumn(Year.of(Year.MIN_VALUE)), is(-999999999));
		assertThat("", converter.convertToDatabaseColumn(Year.of(-10000)), is(-10000));
		assertThat("", converter.convertToDatabaseColumn(Year.of(-9999)), is(-9999));
		assertThat("", converter.convertToDatabaseColumn(Year.of(0)), is(0));
		assertThat("", converter.convertToDatabaseColumn(Year.of(9999)), is(9999));
		assertThat("", converter.convertToDatabaseColumn(Year.of(10000)), is(10000));
		assertThat("", converter.convertToDatabaseColumn(Year.of(Year.MAX_VALUE)), is(999999999));
	}

	@Test
	public void testConvertToEntityAttribute() {

		final AttributeConverter<Year, Integer> converter = new YearConverter();

		assertThat("", converter.convertToEntityAttribute(null), nullValue());
		assertThat("", converter.convertToEntityAttribute(-999999999),is(Year.of(Year.MIN_VALUE)));
		assertThat("", converter.convertToEntityAttribute(-10000), is(Year.of(-10000)));
		assertThat("", converter.convertToEntityAttribute(-9999), is(Year.of(-9999)));
		assertThat("", converter.convertToEntityAttribute(0), is(Year.of(0)));
		assertThat("", converter.convertToEntityAttribute(9999), is(Year.of(9999)));
		assertThat("", converter.convertToEntityAttribute(10000), is(Year.of(10000)));
		assertThat("", converter.convertToEntityAttribute(999999999), is(Year.of(Year.MAX_VALUE)));
	}
}
