import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class JUnitTest {

	@Rule
	public TemporaryFolder tmpFolder = new TemporaryFolder();

	@Test
	public void tmpTest() throws IOException {

		final Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"));
		final Path newPath = tmpFolder.newFile().toPath();

		assertThat("equalTmp", newPath.getParent().getParent(), is(tmpPath));
	}

	@Test
	public void dependOSTest() {

		final String os = System.getProperty("os.name").toLowerCase();
		if (os.startsWith("mac")) {

			assumeTrue(System.getProperty("os.name").contains("Mac"));

			assertThat("macOnly", System.getProperty("os.name"), is("Mac OS X"));
		} else if (os.startsWith("linux")) {

		} else if (os.startsWith("windows")) {

		} else {

		}
	}

	@Test
	public void mockitoTest() {

		@SuppressWarnings("unchecked")
		final List<String> mockList = mock(List.class);
		when(mockList.add("item")).thenReturn(false);

		assertThat("call#add()", mockList.add("item"), is(false));

		verify(mockList).add("item");
	}
}
