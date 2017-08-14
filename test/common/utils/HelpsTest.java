package common.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.jsoup.Jsoup;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HelpsTest {

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
	public void testGetHelpContent() {

		assertThat("", Helps.getHelpContent(Jsoup.parse("<html><head></head><body><div id=\"base\"><div id=\"system_content\">content</div></div></body></html>")).html(), is("content"));
		assertThat("", Helps.getHelpContent(Jsoup.parse("<html><head></head><body>content</body></html>")).html(), is("content"));
		assertThat("", Helps.getHelpContent(Jsoup.parse("<div id=\"system_content\">content</div>")).html(), is("content"));
		assertThat("", Helps.getHelpContent(Jsoup.parse("content")).html(), is("content"));
	}

	@Test
	public void testWrapHelpText() {

		assertThat("", Helps.wrapHelpText("HelpText<div class=\"class\">HelpText<div class=\"class\">HelpText<div>HelpText<div>HelpText"), is("HelpText<div class=\"class\"><span class=\"help-text\">HelpText</span><div class=\"class\"><span class=\"help-text\">HelpText</span><div><span class=\"help-text\">HelpText</span><div>HelpText"));
	}

	@Test
	public void testWrapHelpTextForPreTagContent() {

		final String html = "" + //
				"<code class=\"language-html\" data-lang=\"html\"><span class=\"nt\">&lt;figure</span> <span class=\"na\">class=</span><span class=\"s\">\"figure\"</span><span class=\"nt\">&gt;</span>" + //
				"  <span class=\"nt\">&lt;img</span> <span class=\"na\">src=</span><span class=\"s\">\"srcText\"</span> <span class=\"na\">class=</span><span class=\"s\">\"classText\"</span> <span class=\"na\">alt=</span><span class=\"s\">\"altText\"</span><span class=\"nt\">&gt;</span>" + //
				"  <span class=\"nt\">&lt;figcaption</span> <span class=\"na\">class=</span><span class=\"s\">\"figure-caption\"</span><span class=\"nt\">&gt;</span>captionText<span class=\"nt\">&lt;/figcaption&gt;</span>" + //
				"<span class=\"nt\">&lt;/figure&gt;</span></code>";//
		final String resultHtml = "" + //
				"<code class=\"language-html\" data-lang=\"html\"><span class=\"nt\"><span class=\"help-text\">&lt;figure</span></span> <span class=\"na\"><span class=\"help-text\">class=</span></span><span class=\"s\"><span class=\"help-text\">\"figure\"</span></span><span class=\"nt\"><span class=\"help-text\">&gt;</span></span>" + //
				"  <span class=\"nt\"><span class=\"help-text\">&lt;img</span></span> <span class=\"na\"><span class=\"help-text\">src=</span></span><span class=\"s\"><span class=\"help-text\">\"srcText\"</span></span> <span class=\"na\"><span class=\"help-text\">class=</span></span><span class=\"s\"><span class=\"help-text\">\"classText\"</span></span> <span class=\"na\"><span class=\"help-text\">alt=</span></span><span class=\"s\"><span class=\"help-text\">\"altText\"</span></span><span class=\"nt\"><span class=\"help-text\">&gt;</span></span>" + //
				"  <span class=\"nt\"><span class=\"help-text\">&lt;figcaption</span></span> <span class=\"na\"><span class=\"help-text\">class=</span></span><span class=\"s\"><span class=\"help-text\">\"figure-caption\"</span></span><span class=\"nt\"><span class=\"help-text\">&gt;</span></span><span class=\"help-text\">captionText</span><span class=\"nt\"><span class=\"help-text\">&lt;/figcaption&gt;</span></span>" + //
				"<span class=\"nt\"><span class=\"help-text\">&lt;/figure&gt;</span></span></code>";
		assertThat("", Helps.wrapHelpTextForPreTag(html), is(resultHtml));
	}
}
