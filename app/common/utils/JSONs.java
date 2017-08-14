package common.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import play.api.PlayException.ExceptionSource;

public class JSONs {

	private JSONs() {
	}

	@SuppressWarnings({"null", "serial"})
	@Nonnull
	public static String toJSON(@Nonnull final Object bean) {

		Objects.requireNonNull(bean);

		String json = null;
		try {

			final ObjectMapper mapper = new ObjectMapper();
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bean);
		} catch (final JsonProcessingException e) {

			throw new ExceptionSource("JSON write error", e.toString(), e) {

				@Override
				public String sourceName() {

					return bean.getClass().getName() + " bean";
				}

				@Override
				public Integer position() {

					return e.getLocation().getColumnNr();
				}

				@Override
				public Integer line() {

					return e.getLocation().getLineNr();
				}

				@Override
				public String input() {

					return bean.toString();
				}
			};
		}

		return json;
	}

	@SuppressWarnings({"null", "serial"})
	@Nonnull
	public static <BEAN> BEAN toBean(@Nonnull final String json, @Nonnull final Class<BEAN> clazz) {

		Objects.requireNonNull(json);
		Objects.requireNonNull(clazz);

		BEAN bean = null;
		try {

			final ObjectMapper mapper = new ObjectMapper();
			bean = mapper.readValue(json, clazz);
		} catch (final JsonProcessingException e) {

			throw new ExceptionSource("JSON read error", e.toString(), e) {

				@Override
				public String sourceName() {

					return "Text of json";
				}

				@Override
				public Integer position() {

					return e.getLocation().getColumnNr();
				}

				@Override
				public Integer line() {

					return e.getLocation().getLineNr();
				}

				@Override
				public String input() {

					return json;
				}
			};
		} catch (final IOException e) {

			throw new UncheckedIOException(e);
		}

		return bean;
	}
}
