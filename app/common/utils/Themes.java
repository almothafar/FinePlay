package common.utils;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;

import common.system.MessageKeys;
import models.user.User.Theme;
import play.i18n.MessagesApi;

public class Themes {

	private Themes() {
	}

	@Nonnull
	public static Map<String, String> getThemeIdToNameMap(@Nonnull final Locale locale) {

		Objects.requireNonNull(locale);

		@SuppressWarnings("deprecation")
		final MessagesApi messages = common.system.System.getInjector().instanceOf(MessagesApi.class);

		final Map<String, String> themeIdToNameMap = new LinkedHashMap<>();
		themeIdToNameMap.put(Theme.DEFAULT.name(), messages.get(Locales.toLang(locale), MessageKeys.THEME_DEFAULT));
		themeIdToNameMap.put(Theme.PRETTY.name(), messages.get(Locales.toLang(locale), MessageKeys.THEME_PRETTY));

		return themeIdToNameMap;
	}
}
