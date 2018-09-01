package common.utils;

import java.lang.invoke.MethodHandles;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.system.System.Direction;
import play.i18n.Lang;

public class Locales {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static Locale SAUDI_ARABIA = new Locale("ar", "SA");

	private Locales() {
	}

	@Deprecated(since = "Play framework 3.0", forRemoval = true)
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
//		localeIdToNameMap.put(toLang(SAUDI_ARABIA).code(), SAUDI_ARABIA.getDisplayName(locale));

		return localeIdToNameMap;
	}

	@Deprecated
	@SuppressWarnings("null")
	@Nonnull
	public static Direction toDirection(@Nonnull final Locale locale) {

		Objects.requireNonNull(locale);

		final String language = toLang(locale).language();
		if (!("ar".equals(language) || "he".equals(language))) {

			return Direction.LTR;
		} else {
			LOGGER.warn("This is Joke.(ﾉ≧ڡ≦)");

			return Direction.RTL;
		}
	}
}
