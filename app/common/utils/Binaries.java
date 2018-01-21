package common.utils;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
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

	@SuppressWarnings("null")
	@Nonnull
	public static byte[] concat(@Nonnull final byte[]... bytesArray) {

		Objects.requireNonNull(bytesArray);

		final List<byte[]> bytesList = Arrays.asList(bytesArray);
		bytesList.stream().forEach(bytes -> Objects.requireNonNull(bytes));

		final int totalLength = bytesList.stream().mapToInt(bytes -> bytes.length).sum();
		final ByteBuffer buffer = ByteBuffer.allocate(totalLength);
		bytesList.stream().forEach(bytes -> buffer.put(bytes));

		return buffer.array();
	}
}
