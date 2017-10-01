package common.utils;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.apache.commons.codec.binary.Hex;

public class Binaries {

	private Binaries() {
	}

	@SuppressWarnings("null")
	@Nonnull
	public static String toHexString(@Nonnull final byte[] bytes) {

		Objects.requireNonNull(bytes);

		return Hex.encodeHexString(bytes);
	}
}
