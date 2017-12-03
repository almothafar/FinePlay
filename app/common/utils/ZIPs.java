package common.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jboss.weld.exceptions.IllegalStateException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class ZIPs {

	private ZIPs() {
	}

	@Nonnull
	public static byte[] archive(@Nonnull final Path archiveFolderPath) {

		return zip(null, archiveFolderPath);
	}

	@Nonnull
	public static byte[] archive(@Nonnull final String password, @Nonnull final Path archiveFolderPath) {

		Objects.requireNonNull(password);

		return zip(password, archiveFolderPath);
	}

	@SuppressWarnings("null")
	@Nonnull
	private static byte[] zip(@Nullable final String password, @Nonnull final Path archiveFolderPath) {

		Objects.requireNonNull(archiveFolderPath);

		final Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"));
		final Path archiveFilePath = tmpPath.resolve(UUID.randomUUID() + ".zip");

		try {

			final ZipFile zipFile = new ZipFile(archiveFilePath.toFile());
			zipFile.setFileNameCharset(StandardCharsets.UTF_8.name());

			final ZipParameters parameters = new ZipParameters();
			parameters.setIncludeRootFolder(false);
			parameters.setReadHiddenFiles(false);
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			if (Objects.nonNull(password)) {

				parameters.setEncryptFiles(true);
				parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
				parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
				parameters.setPassword(password);
			}

			zipFile.createZipFileFromFolder(archiveFolderPath.toFile(), parameters, false, 0);

			final byte[] bytes = Files.readAllBytes(archiveFilePath);
			return bytes;
		} catch (IOException e) {

			throw new UncheckedIOException(e);
		} catch (ZipException e) {

			throw new IllegalStateException(e);
		}
	}

	@Nonnull
	public static Path extract(@Nonnull final byte[] extractFileBytes) {

		return unzip(null, extractFileBytes);
	}

	@Nonnull
	public static Path extract(@Nonnull final String password, @Nonnull final byte[] extractFileBytes) {

		Objects.requireNonNull(password);

		return unzip(password, extractFileBytes);
	}

	@Nonnull
	private static Path unzip(@Nullable final String password, @Nonnull final byte[] extractFileBytes) {

		Objects.requireNonNull(extractFileBytes);

		final Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"));
		final String tmpName = UUID.randomUUID().toString();
		final Path archiveFilePath = tmpPath.resolve(tmpName + ".zip");
		final Path extractFolderPath = tmpPath.resolve(tmpName);

		try {

			Files.write(archiveFilePath, extractFileBytes);
			final ZipFile zipFile = new ZipFile(archiveFilePath.toFile());
			if (zipFile.isEncrypted()) {

				Objects.requireNonNull(password);

				zipFile.setPassword(password);
			}

			zipFile.extractAll(extractFolderPath.toString());

			return extractFolderPath;
		} catch (ZipException e) {

			throw new IllegalStateException(e);
		} catch (IOException e) {

			throw new UncheckedIOException(e);
		}
	}
}
