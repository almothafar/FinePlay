package test;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.i18n.Lang;

public class Filter implements Predicate<Condition> {

	@Override
	public boolean test(Condition condition) {

		final Locale locale = condition.getLocale();

		final Config config = ConfigFactory.load();
		// Test execute locales
		final List<Locale> testLocales = config.getConfig("test").getStringList("locales").stream().map(lang -> Lang.forCode(lang).toLocale()).collect(Collectors.toList());
		final boolean isTestLocale = testLocales.contains(locale);

		return isTestLocale;
	}
}
