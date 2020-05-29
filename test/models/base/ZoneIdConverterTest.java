package models.base;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.ZoneId;

import javax.persistence.AttributeConverter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ZoneIdConverterTest {

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

		final AttributeConverter<ZoneId, String> converter = new ZoneIdConverter();

		assertThat("", converter.convertToDatabaseColumn(null), nullValue());
		assertThat("", converter.convertToDatabaseColumn(ZoneId.of("UTC")), is("UTC"));
		assertThat("", converter.convertToDatabaseColumn(ZoneId.of("US/Central")), is("US/Central"));
		assertThat("", converter.convertToDatabaseColumn(ZoneId.of("Asia/Tokyo")), is("Asia/Tokyo"));
	}

	@Test
	public void testConvertToEntityAttribute() {

		final AttributeConverter<ZoneId, String> converter = new ZoneIdConverter();

		assertThat("", converter.convertToEntityAttribute(null), nullValue());
		assertThat("", converter.convertToEntityAttribute("UTC"), is(ZoneId.of("UTC")));
		assertThat("", converter.convertToEntityAttribute("US/Central"), is(ZoneId.of("US/Central")));
		assertThat("", converter.convertToEntityAttribute("Asia/Tokyo"), is(ZoneId.of("Asia/Tokyo")));
	}
}
