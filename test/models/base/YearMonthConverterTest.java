package models.base;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;

import javax.persistence.AttributeConverter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class YearMonthConverterTest {

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

	@SuppressWarnings("deprecation")
	@Test
	public void testDateSpec() {

		assertThat("", new Date(-1000 - 1900, 0, 1).toString(), is("1001-01-01"));// 🤔
		assertThat("", new Date(-1 - 1900, 0, 1).toString(), is("0002-01-01"));// 🤔
		assertThat("", new Date(0 - 1900, 0, 1).toString(), is("0001-01-01"));// 🤔
		assertThat("", new Date(1 - 1900, 0, 1).toString(), is("0001-01-01"));
		assertThat("", new Date(1000 - 1900, 0, 1).toString(), is("1000-01-01"));
	}

	@Test
	public void testConvertToDatabaseColumn() {

		final AttributeConverter<YearMonth, Date> converter = new YearMonthConverter();

		assertThat("", converter.convertToDatabaseColumn(null), nullValue());
//		assertThat("", converter.convertToDatabaseColumn(YearMonth.of(-10000, Month.SEPTEMBER)), is(Date.valueOf(LocalDate.of(-10000, Month.SEPTEMBER, 1))));
//		assertThat("", converter.convertToDatabaseColumn(YearMonth.of(-10000, Month.OCTOBER)), is(Date.valueOf(LocalDate.of(-10000, Month.OCTOBER, 1))));
//		assertThat("", converter.convertToDatabaseColumn(YearMonth.of(-9999, Month.SEPTEMBER)), is(Date.valueOf(LocalDate.of(-9999, Month.SEPTEMBER, 1))));
//		assertThat("", converter.convertToDatabaseColumn(YearMonth.of(-9999, Month.OCTOBER)), is(Date.valueOf(LocalDate.of(-9999, Month.OCTOBER, 1))));
//		assertThat("", converter.convertToDatabaseColumn(YearMonth.of(-1, Month.SEPTEMBER)), is(Date.valueOf(LocalDate.of(-1, Month.SEPTEMBER, 1))));
//		assertThat("", converter.convertToDatabaseColumn(YearMonth.of(-1, Month.OCTOBER)), is(Date.valueOf(LocalDate.of(-1, Month.OCTOBER, 1))));
//		assertThat("", converter.convertToDatabaseColumn(YearMonth.of(0, Month.SEPTEMBER)), is(Date.valueOf(LocalDate.of(0, Month.SEPTEMBER, 1))));
//		assertThat("", converter.convertToDatabaseColumn(YearMonth.of(0, Month.OCTOBER)), is(Date.valueOf(LocalDate.of(0, Month.OCTOBER, 1))));
		assertThat("", converter.convertToDatabaseColumn(YearMonth.of(1, Month.SEPTEMBER)), is(Date.valueOf(LocalDate.of(1, Month.SEPTEMBER, 1))));
		assertThat("", converter.convertToDatabaseColumn(YearMonth.of(1, Month.OCTOBER)), is(Date.valueOf(LocalDate.of(1, Month.OCTOBER, 1))));
		assertThat("", converter.convertToDatabaseColumn(YearMonth.of(9999, Month.SEPTEMBER)), is(Date.valueOf(LocalDate.of(9999, Month.SEPTEMBER, 1))));
		assertThat("", converter.convertToDatabaseColumn(YearMonth.of(9999, Month.OCTOBER)), is(Date.valueOf(LocalDate.of(9999, Month.OCTOBER, 1))));
		assertThat("", converter.convertToDatabaseColumn(YearMonth.of(10000, Month.SEPTEMBER)), is(Date.valueOf(LocalDate.of(10000, Month.SEPTEMBER, 1))));
		assertThat("", converter.convertToDatabaseColumn(YearMonth.of(10000, Month.OCTOBER)), is(Date.valueOf(LocalDate.of(10000, Month.OCTOBER, 1))));
	}

	@Test
	public void testConvertToEntityAttribute() {

		final AttributeConverter<YearMonth, Date> converter = new YearMonthConverter();

		assertThat("", converter.convertToEntityAttribute(null), nullValue());
//		assertThat("", converter.convertToEntityAttribute(Date.valueOf(LocalDate.of(-10000, Month.SEPTEMBER, 1))), is(YearMonth.of(-10000, Month.SEPTEMBER)));
//		assertThat("", converter.convertToEntityAttribute(Date.valueOf(LocalDate.of(-10000, Month.OCTOBER, 1))), is(YearMonth.of(-10000, Month.OCTOBER)));
//		assertThat("", converter.convertToEntityAttribute(Date.valueOf(LocalDate.of(-9999, Month.SEPTEMBER, 1))), is(YearMonth.of(-9999, Month.SEPTEMBER)));
//		assertThat("", converter.convertToEntityAttribute(Date.valueOf(LocalDate.of(-9999, Month.OCTOBER, 1))), is(YearMonth.of(-9999, Month.OCTOBER)));
//		assertThat("", converter.convertToEntityAttribute(Date.valueOf(LocalDate.of(-1, Month.SEPTEMBER, 1))), is(YearMonth.of(-1, Month.SEPTEMBER)));
//		assertThat("", converter.convertToEntityAttribute(Date.valueOf(LocalDate.of(-1, Month.OCTOBER, 1))), is(YearMonth.of(-1, Month.OCTOBER)));
//		assertThat("", converter.convertToEntityAttribute(Date.valueOf(LocalDate.of(0, Month.SEPTEMBER, 1))), is(YearMonth.of(0, Month.SEPTEMBER)));
//		assertThat("", converter.convertToEntityAttribute(Date.valueOf(LocalDate.of(0, Month.OCTOBER, 1))), is(YearMonth.of(0, Month.OCTOBER)));
		assertThat("", converter.convertToEntityAttribute(Date.valueOf(LocalDate.of(1, Month.SEPTEMBER, 1))), is(YearMonth.of(1, Month.SEPTEMBER)));
		assertThat("", converter.convertToEntityAttribute(Date.valueOf(LocalDate.of(1, Month.OCTOBER, 1))), is(YearMonth.of(1, Month.OCTOBER)));
		assertThat("", converter.convertToEntityAttribute(Date.valueOf(LocalDate.of(9999, Month.SEPTEMBER, 1))), is(YearMonth.of(9999, Month.SEPTEMBER)));
		assertThat("", converter.convertToEntityAttribute(Date.valueOf(LocalDate.of(9999, Month.OCTOBER, 1))), is(YearMonth.of(9999, Month.OCTOBER)));
		assertThat("", converter.convertToEntityAttribute(Date.valueOf(LocalDate.of(10000, Month.SEPTEMBER, 1))), is(YearMonth.of(10000, Month.SEPTEMBER)));
		assertThat("", converter.convertToEntityAttribute(Date.valueOf(LocalDate.of(10000, Month.OCTOBER, 1))), is(YearMonth.of(10000, Month.OCTOBER)));
	}
}
