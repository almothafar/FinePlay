package common.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
}
