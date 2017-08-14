package test;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.hamcrest.Matcher;
import org.hamcrest.core.AnyOf;

public class Condition {

	private final String message;
	private final Locale locale;
	private final Map<String, Object> map;

	public Condition(final String message, final Map<String, Object> map) {

		this(message, null, map);
	}

	public Condition(final String message, final Locale locale, final Map<String, Object> map) {

		this.message = message;
		this.locale = locale;
		this.map = map;
	}

	public String getMessage() {

		return message;
	}

	public Locale getLocale() {

		return locale;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(final String key) {

		return (T) map.get(key);
	}

	public static <VALUE> AnyOf<VALUE> anyConditionOf(@Nonnull final List<Condition> conditions, @Nonnull final String key, @Nonnull final Class<VALUE> clazz) {

		@SuppressWarnings("unchecked")
		final Iterable<Matcher<? super VALUE>> matchers = conditions.stream().map(condition -> is((VALUE) condition.get(key))).collect(Collectors.toList());
		return AnyOf.<VALUE>anyOf(matchers);
	}
}
