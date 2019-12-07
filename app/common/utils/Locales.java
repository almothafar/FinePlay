package common.utils;

import java.lang.invoke.MethodHandles;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.hibernate.Incubating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.system.System.Direction;

public class Locales {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final Locale HONGKONG = new Locale("zh", "HK");
	private static final Locale MACAU = new Locale("zh", "MO");

	private static final Locale UNITED_ARAB_EMIRATES = new Locale("ar", "AE");

	private Locales() {
	}

	@Nonnull
	public static Locale normalize(@Nonnull final Locale locale) {

		Objects.requireNonNull(locale);

		switch (locale.getLanguage()) {
		case "ja":

			return Locale.JAPAN;
		case "zh":

			return Locale.US;
//			switch (locale.getCountry()) {
//			case "TW":
//
//				return Locale.TAIWAN;
//			case "HK":
//
//				return HONGKONG;
//			case "MO":
//
//				return MACAU;
//			case "CN":
//			default:
//
//				return Locale.CHINA;
//			}
		case "ko":

			return Locale.US;
//			return Locale.KOREA;
		case "en":
		default:

			return Locale.US;
		}
	}

	@Nonnull
	public static Map<String, String> getLocaleIdToNameMap(@Nonnull final Locale locale) {

		Objects.requireNonNull(locale);

		final Map<String, String> localeIdToNameMap = new LinkedHashMap<>();
		localeIdToNameMap.put(Locale.US.toLanguageTag(), Locale.US.getDisplayName(Locale.US));
		localeIdToNameMap.put(Locale.JAPAN.toLanguageTag(), Locale.JAPAN.getDisplayName(Locale.JAPAN));
//		localeIdToNameMap.put(UNITED_ARAB_EMIRATES.toLanguageTag(), UNITED_ARAB_EMIRATES.getDisplayName(UNITED_ARAB_EMIRATES));

		return localeIdToNameMap;
	}

	@Incubating
	@Nonnull
	public static Direction toDirection(@Nonnull final Locale locale) {

		Objects.requireNonNull(locale);

		final String language = locale.getLanguage();
		if (!("ar".equals(language) || "iw".equals(language))) {

			return Direction.LTR;
		} else {
			LOGGER.warn("This is Joke.(ﾉ≧ڡ≦)");

			return Direction.RTL;
		}
	}
}
