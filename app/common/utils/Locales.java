package common.utils;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;

import play.i18n.Lang;

public class Locales {

	private Locales() {
	}

	@Nonnull
	public static Lang toLang(@Nonnull final Locale locale) {

		Objects.requireNonNull(locale);

		return new Lang(play.api.i18n.Lang.apply(locale.getLanguage(), locale.getCountry(), "", ""));
	}

	@SuppressWarnings("null")
	@Nonnull
	public static Map<String, String> getLocaleIdToNameMap(@Nonnull final Locale locale) {

		Objects.requireNonNull(locale);

		final Map<String, String> localeIdToNameMap = new LinkedHashMap<>();
		localeIdToNameMap.put(toLang(Locale.US).code(), Locale.US.getDisplayName(locale));
		localeIdToNameMap.put(toLang(Locale.JAPAN).code(), Locale.JAPAN.getDisplayName(locale));

		return localeIdToNameMap;
	}
}
