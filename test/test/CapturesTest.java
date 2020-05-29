package test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CapturesTest {

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
	public void testMethod() {

		final Path path = Captures.getFolderPath();

		assertThat("", path.getNameCount(), is(6));
		assertThat("", path.getName(0).toString(), is("target"));
		assertThat("", path.getName(1).toString(), is("test-screenshot"));
		assertThat("", path.getName(2).toString(), is(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
		assertThat("", path.getName(3).toString(), is("test"));
		assertThat("", path.getName(4).toString(), is("CapturesTest"));
		assertThat("", path.getName(5).toString(), is("testMethod"));
	}
}