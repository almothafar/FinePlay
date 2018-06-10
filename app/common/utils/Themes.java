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
		for (final Theme theme : Theme.values()) {

			themeIdToNameMap.put(theme.name(), messages.get(Locales.toLang(locale), theme.getMessageKey()));
		}

		return themeIdToNameMap;
	}
}
