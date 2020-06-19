package common.utils;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import models.user.User.Appearance;
import play.i18n.Lang;
import play.i18n.MessagesApi;

public class Appearances {

	private Appearances() {
	}

	@Inject
	private static MessagesApi messagesApi;

	@Nonnull
	public static Map<String, String> getAppearanceIdToNameMap(@Nonnull final Locale locale) {

		Objects.requireNonNull(locale);

		final Map<String, String> appearanceIdToNameMap = new LinkedHashMap<>();
		for (final Appearance appearance : Appearance.values()) {

			appearanceIdToNameMap.put(appearance.name(), messagesApi.get(new Lang(locale), appearance.getMessageKey()));
		}

		return appearanceIdToNameMap;
	}
}
