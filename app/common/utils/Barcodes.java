package common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class Barcodes {

	private Barcodes() {
	}

	@Nonnull
	public static byte[] toEAN13(final int width, final int height, @Nonnull String contents) {

		return toBarcode(BarcodeFormat.EAN_13, width, height, contents);
	}

	@Nonnull
	public static byte[] toQRCode(final int width, final int height, @Nonnull String contents) {

		return toBarcode(BarcodeFormat.QR_CODE, width, height, contents);
	}

	@SuppressWarnings("null")
	@Nonnull
	private static byte[] toBarcode(@Nonnull final BarcodeFormat format, final int width, final int height, @Nonnull final String contents) {

		Objects.requireNonNull(format);
		Objects.requireNonNull(contents);

		final Map<EncodeHintType, Object> hint = new HashMap<>();
		hint.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
		hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		final Writer writer = new MultiFormatWriter();
		BitMatrix matrix = null;
		try {

			matrix = writer.encode(contents, format, width, height, hint);
		} catch (WriterException e) {

			throw new IllegalStateException(e);
		}

		try (final ByteArrayOutputStream stream = new ByteArrayOutputStream()) {

			MatrixToImageWriter.writeToStream(matrix, "png", stream);

			final byte[] bytes = stream.toByteArray();
			return bytes;
		} catch (IOException e) {

			throw new UncheckedIOException(e);
		}
	}
}
