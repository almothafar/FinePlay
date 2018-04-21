package common.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.apache.commons.codec.binary.Hex;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class Binaries {

	private Binaries() {
	}

	@Nonnull
	public static byte[] toHash(@Nonnull final String algorithm, @Nonnull final byte[] bytes) {

		Objects.requireNonNull(algorithm);
		Objects.requireNonNull(bytes);

		MessageDigest messageDigest = null;
		try {

			messageDigest = MessageDigest.getInstance(algorithm);
		} catch (final NoSuchAlgorithmException e) {

			throw new IllegalStateException(e);
		}

		messageDigest.update(bytes);
		final byte[] hash = messageDigest.digest();
		int length = -1;
		switch (algorithm) {
		case "SHA-512":

			length = 64;
			break;
		case "SHA-384":

			length = 48;
			break;
		case "SHA-256":

			length = 32;
			break;
		case "SHA-224":

			length = 28;
			break;
		case "SHA-1":
		case "MD5":
		case "MD2":
		default:

			throw new IllegalStateException(": " + algorithm);
		}

		if (length != hash.length) {

			throw new IllegalStateException(": " + hash.length);
		}

		return hash;
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

	@Nonnull
	public static Metadata getMetadata(@Nonnull final byte[] bytes) {

		Objects.requireNonNull(bytes);

		try (final InputStream stream = new ByteArrayInputStream(bytes)) {

			final ContentHandler handler = new BodyContentHandler();
			final Metadata metadata = new Metadata();
			final ParseContext context = new ParseContext();

			final Parser parser = new AutoDetectParser();
			parser.parse(stream, handler, metadata, context);

			return metadata;
		} catch (IOException e) {

			throw new UncheckedIOException(e);
		} catch (SAXException | TikaException e) {

			throw new IllegalStateException(e);
		}
	}
}
