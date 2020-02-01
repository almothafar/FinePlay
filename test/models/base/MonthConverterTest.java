package models.base;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.time.Month;

import javax.persistence.AttributeConverter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MonthConverterTest {

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

		final AttributeConverter<Month, Integer> converter = new MonthConverter();

		assertThat("", converter.convertToDatabaseColumn(null), nullValue());
		assertThat("", converter.convertToDatabaseColumn(Month.JANUARY), is(1));
		assertThat("", converter.convertToDatabaseColumn(Month.DECEMBER), is(12));
	}

	@Test
	public void testConvertToEntityAttribute() {

		final AttributeConverter<Month, Integer> converter = new MonthConverter();

		assertThat("", converter.convertToEntityAttribute(null), nullValue());
		assertThat("", converter.convertToEntityAttribute(1), is(Month.JANUARY));
		assertThat("", converter.convertToEntityAttribute(12), is(Month.DECEMBER));
	}
}
