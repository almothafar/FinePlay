package common.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BinariesTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@SuppressWarnings("null")
	@Test
	public void testToHex() {

		assertThat("", Binaries.toHexString("".getBytes(StandardCharsets.UTF_8)), is(""));
		assertThat("", Binaries.toHexString("0123456789".getBytes(StandardCharsets.UTF_8)), is("30313233343536373839"));
		assertThat("", Binaries.toHexString("abcdefghijklmnopqrstuvwxyz".getBytes(StandardCharsets.UTF_8)), is("6162636465666768696a6b6c6d6e6f707172737475767778797a"));
		assertThat("", Binaries.toHexString("ABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes(StandardCharsets.UTF_8)), is("4142434445464748494a4b4c4d4e4f505152535455565758595a"));
	}

	@Test
	public void testConcat() {

		assertThat("", new String(Binaries.concat(new byte[] { (byte) 0x61, (byte) 0x62, (byte) 0x63 }, new byte[] { (byte) 0x64, (byte) 0x65, (byte) 0x66 }), StandardCharsets.UTF_8), is("abcdef"));
		assertThat("", new String(Binaries.concat(//
				new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x91, (byte) 0xA9 }, //
				new byte[] { (byte) 0xE2, (byte) 0x80, (byte) 0x8D }, //
				new byte[] { (byte) 0xE2, (byte) 0x9D, (byte) 0xA4, (byte) 0xEF, (byte) 0xB8, (byte) 0x8F }, //
				new byte[] { (byte) 0xE2, (byte) 0x80, (byte) 0x8D }, //
				new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x91, (byte) 0xA9 }), StandardCharsets.UTF_8), is("👩‍❤️‍👩"));
	}

	@SuppressWarnings("null")
	@Test
	public void testGetMetadata() throws IOException {

		Metadata metadata;

		metadata = Binaries.getMetadata(Files.readAllBytes(Paths.get("public", "images", "en-US", "logo.png")));
		System.out.println(metadata.toString());
		assertThat("", metadata.get(HttpHeaders.CONTENT_TYPE), is("image/png"));

		metadata = Binaries.getMetadata(Files.readAllBytes(Paths.get("public", "images", "icons", "fineplay.svg")));
		System.out.println(metadata.toString());
		assertThat("", metadata.get(HttpHeaders.CONTENT_TYPE), is("image/svg+xml"));

		metadata = Binaries.getMetadata(Files.readAllBytes(Paths.get("conf", "messages.xlsx")));
		System.out.println(metadata.toString());
		assertThat("", metadata.get(Metadata.CONTENT_TYPE), is("application/x-tika-ooxml"));

		metadata = Binaries.getMetadata(Files.readAllBytes(Paths.get("conf", "messages")));
		System.out.println(metadata.toString());
		assertThat("", metadata.get(Metadata.CONTENT_TYPE), is("text/plain"));

		metadata = Binaries.getMetadata(Files.readAllBytes(Paths.get("conf", "fonts", "ipamjm.ttf")));
		System.out.println(metadata.toString());
		assertThat("", metadata.get(HttpHeaders.CONTENT_TYPE), is("application/x-font-ttf"));

		metadata = Binaries.getMetadata(Files.readAllBytes(Paths.get("conf", "resources", "pdfs", "tracemonkey.pdf")));
		System.out.println(metadata.toString());
		assertThat("", metadata.get(HttpHeaders.CONTENT_TYPE), is("application/pdf"));

		metadata = Binaries.getMetadata(Files.readAllBytes(Paths.get("test", "common", "utils", "fake.png")));
		System.out.println(metadata.toString());
		assertThat("", metadata.get(HttpHeaders.CONTENT_TYPE), is("text/plain"));
	}
}
