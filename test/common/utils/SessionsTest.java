package common.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.RoundingMode;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SessionsTest {

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
	public void testToValue() {

		assertThat("", Sessions.toValue(Arrays.asList(new String[] { "abc", "def" })), is("abc,def"));
		assertThat("", Sessions.toValue(Arrays.asList(new RoundingMode[] { RoundingMode.UP, RoundingMode.DOWN })), is("UP,DOWN"));
	}

}
