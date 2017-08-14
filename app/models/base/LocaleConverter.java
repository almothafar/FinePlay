package models.base;

import java.util.Locale;

import javax.annotation.Nullable;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocaleConverter implements AttributeConverter<Locale, String> {

	@Nullable
	@Override
	public String convertToDatabaseColumn(@Nullable final Locale locale) {

		return (locale == null) ? null : locale.toLanguageTag();
	}

	@Nullable
	@Override
	public Locale convertToEntityAttribute(@Nullable final String localeString) {

		return (localeString == null) ? null : Locale.forLanguageTag(localeString);
	}
}
