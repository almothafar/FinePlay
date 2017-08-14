
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.FIREFOX;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

import org.junit.Test;

public class BrowserTest {

	@Test
	public void runInBrowser() {

		running(testServer(), FIREFOX, browser -> {
			browser.goTo("/test");
			// browser.takeScreenShot();
			assertEquals("Title", browser.window().title());
			assertTrue(browser.pageSource().contains("Hello"));
			// assertEquals("Title", browser.$("#title").text());
			// browser.$("a").click();
			assertEquals("test", browser.url());
		});
	}
}
