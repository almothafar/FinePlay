package common.core.format;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import play.data.format.Formatters;
import play.data.format.Formatters.SimpleFormatter;
import play.i18n.MessagesApi;

@Singleton
public class FormattersProvider implements Provider<Formatters> {

	private final MessagesApi messagesApi;

	@Inject
	public FormattersProvider(MessagesApi messagesApi) {

		this.messagesApi = messagesApi;
	}

	@Override
	public Formatters get() {

		final Formatters formatters = new Formatters(messagesApi);

		formatters.register(ZonedDateTime.class, new SimpleFormatter<ZonedDateTime>() {

			@Override
			public ZonedDateTime parse(String text, Locale locale) throws ParseException {

				return ZonedDateTime.parse(text, DateTimeFormatter.ISO_ZONED_DATE_TIME);
			}

			@Override
			public String print(ZonedDateTime zonedDateTime, Locale locale) {

				return zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
			}
		});

		formatters.register(LocalDateTime.class, new SimpleFormatter<LocalDateTime>() {

			@Override
			public LocalDateTime parse(String text, Locale locale) throws ParseException {

				return LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
			}

			@Override
			public String print(LocalDateTime localDateTime, Locale locale) {

				return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
			}
		});

		formatters.register(LocalDate.class, new SimpleFormatter<LocalDate>() {

			@Override
			public LocalDate parse(String text, Locale locale) throws ParseException {

				return LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
			}

			@Override
			public String print(LocalDate localDate, Locale locale) {

				return localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
			}
		});

		formatters.register(LocalTime.class, new SimpleFormatter<LocalTime>() {

			@Override
			public LocalTime parse(String text, Locale locale) throws ParseException {

				return LocalTime.parse(text, DateTimeFormatter.ISO_LOCAL_TIME);
			}

			@Override
			public String print(LocalTime localTime, Locale locale) {

				return localTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
			}
		});

		formatters.register(ZoneOffset.class, new SimpleFormatter<ZoneOffset>() {

			@Override
			public ZoneOffset parse(String offsetId, Locale locale) throws ParseException {

				return ZoneOffset.of(offsetId);
			}

			@Override
			public String print(ZoneOffset zoneOffset, Locale locale) {

				return zoneOffset.getId();
			}
		});

		return formatters;
	}
}