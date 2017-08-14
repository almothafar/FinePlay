package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class Appender extends AppenderBase<ILoggingEvent> {

	private final List<ILoggingEvent> events = new ArrayList<>();

	@Override
	protected void append(ILoggingEvent eventObject) {
	}

	@Override
	public synchronized void doAppend(ILoggingEvent event) {

		events.add(event);
	}

	public List<ILoggingEvent> getEvents() {

		return Collections.unmodifiableList(events);
	}

	public void clearEvents() {

		events.clear();
	}
}
