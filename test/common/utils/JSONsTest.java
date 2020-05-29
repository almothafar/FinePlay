package common.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JSONsTest {

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
	public void testToJSON_BigDecimal() {

		final String json = JSONs.toJSON(new BigDecimal("0.0000001"));

		assertThat("", json, is("0.0000001"));
	}

	@Test
	public void testToJSON_List() {

		final List<String> list = new ArrayList<>();
		list.add("value");

		final String json = JSONs.toJSON(list);

		assertThat("", "[ \"value\" ]", is(json));
	}

	@Test
	public void testToJSON_Map() {

		final Map<String, String> map = new HashMap<>();
		map.put("key", "value");

		final String json = JSONs.toJSON(map);

		assertThat("", "{\n  \"key\" : \"value\"\n}", is(json));
	}

	@Test
	public void testToJSON_DateTime() {

		final DateTime dateTime = new DateTime();
		dateTime.dateTime = LocalDateTime.of(1234, Month.MAY, 6, 7, 8);
		dateTime.date = LocalDate.of(1234, Month.MAY, 6);
		dateTime.time = LocalTime.of(7, 8);

		final String json = JSONs.toJSON(dateTime);

		assertThat("", "{\n  \"dateTime\" : \"1234-05-06T07:08:00\",\n  \"date\" : \"1234-05-06\",\n  \"time\" : \"07:08:00\"\n}", is(json));
	}

	@Test
	public void testToBean_List() {

		final String json = "[\"value\"]";

		@SuppressWarnings("unchecked")
		final List<String> list = JSONs.toBean(json, ArrayList.class);

		assertThat("", 1, is(list.size()));
		assertThat("", "value", is(list.get(0)));
	}

	@Test
	public void testToBean_Map() {

		final String json = "{\"key\":\"value\"}";

		@SuppressWarnings("unchecked")
		final Map<String, String> map = JSONs.toBean(json, HashMap.class);

		assertThat("", 1, is(map.size()));
		assertThat("", "value", is(map.get("key")));
	}

	@Test
	public void testToBean_DateTime() {

		final String json = "{\n  \"dateTime\" : \"1234-05-06T07:08:00\",\n  \"date\" : \"1234-05-06\",\n  \"time\" : \"07:08:00\"\n}";

		final DateTime dateTime = JSONs.toBean(json, DateTime.class);

		assertThat("", LocalDateTime.of(1234, Month.MAY, 6, 7, 8), is(dateTime.dateTime));
		assertThat("", LocalDate.of(1234, Month.MAY, 6), is(dateTime.date));
		assertThat("", LocalTime.of(7, 8), is(dateTime.time));
	}

	private static class DateTime {

		LocalDateTime dateTime;
		LocalDate date;
		LocalTime time;

		@SuppressWarnings("unused")
		public LocalDateTime getDateTime() {
			return dateTime;
		}

		@SuppressWarnings("unused")
		public void setDateTime(LocalDateTime dateTime) {
			this.dateTime = dateTime;
		}

		@SuppressWarnings("unused")
		public LocalDate getDate() {
			return date;
		}

		@SuppressWarnings("unused")
		public void setDate(LocalDate date) {
			this.date = date;
		}

		@SuppressWarnings("unused")
		public LocalTime getTime() {
			return time;
		}

		@SuppressWarnings("unused")
		public void setTime(LocalTime time) {
			this.time = time;
		}
	}
}
