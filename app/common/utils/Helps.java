package common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import play.twirl.api.Html;

public class Helps {

	private Helps() {
	}

	private static final Pattern PATTERN_HELP_TEXT = Pattern.compile(">(.*?)<", Pattern.CASE_INSENSITIVE);

	private static final Pattern PATTERN_COMMENT_TAG = Pattern.compile("<!--[\\s\\S]*?-->", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_PRE_TAG = Pattern.compile("<pre>(.*?)</pre>", Pattern.CASE_INSENSITIVE);

	@SuppressWarnings("null")
	public static Html prepare(@Nonnull final Html html) {

		final Document document = Jsoup.parse(html.body());
		document.select("script").remove();
		document.select("#system_credit-container").remove();
		final Element helpContent = getHelpContent(document);
		final Elements preTags = helpContent.select("pre");

		String wrapedHelpContentText = wrapHelpText(Strings.removeNewLine(helpContent.html()));
		wrapedHelpContentText = removeComment(wrapedHelpContentText);

		final Document wrapedDocument = Jsoup.parse(wrapedHelpContentText);
		final Element wrapedHelpContent = getHelpContent(wrapedDocument);

		return Html.apply(repairPreTags(wrapedHelpContent, preTags));
	}

	static Element getHelpContent(@Nonnull final Document document) {

		final Elements helpContents = document.select("#system_content");
		final Element helpContent = 1 == helpContents.size() ? helpContents.first() : document.body();

		return helpContent;
	}

	static String wrapHelpText(@Nonnull final String htmlText) {

		final Matcher matcher = PATTERN_HELP_TEXT.matcher(htmlText);

		final StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {

			final boolean isSpaceOnly = matcher.group(1).trim().isEmpty();
			if (isSpaceOnly) {

				matcher.appendReplacement(buffer, ">$1<");
			} else {

				matcher.appendReplacement(buffer, "><span class=\"help-text\">$1</span><");
			}
		}
		matcher.appendTail(buffer);

		return buffer.toString();
	}

	static String removeComment(@Nonnull final String htmlText) {

		return PATTERN_COMMENT_TAG.matcher(htmlText).replaceAll("");
	}

	static String repairPreTags(@Nonnull final Element wrapedHelpContent, @Nonnull final Elements preTags) {

		final String wrapedHelpContentText = wrapedHelpContent.html();
		@SuppressWarnings("null")
		final List<String> wrapedPreTagContents = new ArrayList<>(preTags.stream().map(preTag -> wrapHelpTextForPreTag(preTag.html())).collect(Collectors.toList()));

		final Matcher matcher = PATTERN_PRE_TAG.matcher(wrapedHelpContentText);
		final StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {

			matcher.appendReplacement(buffer, "<pre>" + wrapedPreTagContents.remove(0) + "</pre>");
		}
		matcher.appendTail(buffer);

		return buffer.toString();
	}

	@SuppressWarnings("null")
	static String wrapHelpTextForPreTag(@Nonnull final String preTagContent) {

		String wrapedPreTagContent = preTagContent.replaceAll(Strings.REGEX_NEW_LINE, "<br>");
		wrapedPreTagContent = wrapHelpText(wrapedPreTagContent);
		wrapedPreTagContent = wrapedPreTagContent.replaceAll("<br>", "\n");

		return wrapedPreTagContent;
	}
}
