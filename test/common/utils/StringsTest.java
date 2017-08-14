package common.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StringsTest {

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
	public void testSplitNewLine() {

		assertThat("", Strings.splitNewLine(""), is(Collections.emptyList()));
		assertThat("", Strings.splitNewLine("\r"), is(Collections.emptyList()));
		assertThat("", Strings.splitNewLine("\n"), is(Collections.emptyList()));
		assertThat("", Strings.splitNewLine("\r\n"), is(Collections.emptyList()));
		assertThat("", Strings.splitNewLine("\r\r"), is(Collections.emptyList()));
		assertThat("", Strings.splitNewLine("\n\n"), is(Collections.emptyList()));
		assertThat("", Strings.splitNewLine("\r\n\r\n"), is(Collections.emptyList()));
		assertThat("", Strings.splitNewLine("\n\r\r\n"), is(Collections.emptyList()));
		assertThat("", Strings.splitNewLine("\n\r\r\n\n\r\r\n"), is(Collections.emptyList()));
		assertThat("", Strings.splitNewLine("\rb\nc\r\nd"), is(Arrays.asList("b", "c", "d")));
		assertThat("", Strings.splitNewLine("a\rb\nc\r\n"), is(Arrays.asList("a", "b", "c")));
		assertThat("", Strings.splitNewLine("a\rb\nc\r\nd"), is(Arrays.asList("a", "b", "c", "d")));
	}

	@Test
	public void testRemoveNewLine() {

		assertThat("", Strings.removeNewLine(""), is(""));
		assertThat("", Strings.removeNewLine("\r"), is(""));
		assertThat("", Strings.removeNewLine("\n"), is(""));
		assertThat("", Strings.removeNewLine("\r\n"), is(""));
		assertThat("", Strings.removeNewLine("\r\r"), is(""));
		assertThat("", Strings.removeNewLine("\n\n"), is(""));
		assertThat("", Strings.removeNewLine("\r\n\r\n"), is(""));
		assertThat("", Strings.removeNewLine("\n\r\r\n"), is(""));
		assertThat("", Strings.removeNewLine("\n\r\r\n\n\r\r\n"), is(""));
		assertThat("", Strings.removeNewLine("a\rb\nc\r\nd"), is("abcd"));
	}
}
