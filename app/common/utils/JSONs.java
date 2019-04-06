package common.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import play.api.PlayException.ExceptionSource;

public class JSONs {

	private static final JavaTimeModule SERIALIZE_TIME_MODULE = initSerializeTimeModule();

	private static JavaTimeModule initSerializeTimeModule() {

		final JavaTimeModule timeModule = new JavaTimeModule();
		timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
		timeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME));

		return timeModule;
	}

	private static final JavaTimeModule DESERIALIZE_TIME_MODULE = initDeserializeTimeModule();

	private static JavaTimeModule initDeserializeTimeModule() {

		final JavaTimeModule timeModule = new JavaTimeModule();
		timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));
		timeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME));

		return timeModule;
	}

	private JSONs() {
	}

	@SuppressWarnings("serial")
	@Nonnull
	public static String toJSON(@Nonnull final Object bean) {

		Objects.requireNonNull(bean);

		String json = null;
		try {

			final ObjectMapper mapper = new ObjectMapper();
			mapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
			mapper.registerModule(SERIALIZE_TIME_MODULE);
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

	@SuppressWarnings({ "null", "serial" })
	@Nonnull
	public static <BEAN> BEAN toBean(@Nonnull final String json, @Nonnull final Class<BEAN> clazz) {

		Objects.requireNonNull(json);
		Objects.requireNonNull(clazz);

		BEAN bean = null;
		try {

			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(DESERIALIZE_TIME_MODULE);
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
