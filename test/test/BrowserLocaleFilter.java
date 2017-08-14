package test;

import java.util.Locale;
import java.util.function.Predicate;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.i18n.Lang;

public class BrowserLocaleFilter implements Predicate<Condition> {

	@Override
	public boolean test(Condition condition) {

		final Locale locale = condition.getLocale();

		final Config config = ConfigFactory.load();
		// Browser first locale
		final Locale testBrowserLocale = Lang.forCode(config.getConfig("test.browser").getString("locale")).toLocale();
		final boolean isTestBrowserLocale = locale.equals(testBrowserLocale);

		return isTestBrowserLocale;
	}
}
