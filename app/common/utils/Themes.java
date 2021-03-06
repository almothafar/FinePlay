package common.utils;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import models.user.User.Theme;
import play.i18n.Lang;
import play.i18n.MessagesApi;

public class Themes {

	private Themes() {
	}

	@Inject
	private static MessagesApi messagesApi;

	@Nonnull
	public static Map<String, String> getThemeIdToNameMap(@Nonnull final Locale locale) {

		Objects.requireNonNull(locale);

		final Map<String, String> themeIdToNameMap = new LinkedHashMap<>();
		for (final Theme theme : Theme.values()) {

			themeIdToNameMap.put(theme.name(), messagesApi.get(new Lang(locale), theme.getMessageKey()));
		}

		return themeIdToNameMap;
	}
}
