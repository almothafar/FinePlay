package test;

import org.apache.commons.lang3.StringUtils;

public class Counter {

	private final int width;
	private int current = 0;

	public Counter(final int width) {

		this.width = width;
	}

	public Counter increment() {

		current++;
		return this;
	}

	public Counter reset() {

		current = 0;
		return this;
	}

	@Override
	public String toString() {

		return StringUtils.leftPad(String.valueOf(current), width, '0');
	}
}
