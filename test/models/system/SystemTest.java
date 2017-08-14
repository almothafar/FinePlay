package models.system;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import models.system.System.Color;

public class SystemTest {

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

	@Test
	public void colorTestToHex3() {

		final Color color = new Color("rgba(0, 123, 255, 0.5)");
		assertEquals("", color.toHex3(), "#07f");
	}

	@Test
	public void colorTestToHex() {

		final Color color = new Color("rgba(0, 123, 255, 0.5)");
		assertEquals("", color.toHex(), "#007bff");
	}

	@Test
	public void colorTestToRgba() {

		final Color color = new Color("rgba(0, 123, 255, 0.5)");
		assertEquals("", color.toRgba(), "rgba(0, 123, 255, 0.5)");
	}

	@Test
	public void colorTestToActiveBackgroundColor() {

		final Color color = new Color("#007bff");
		assertEquals("", color.toActiveBackgroundColor().toHex(), "#0069d9");
	}

	@Test
	public void colorTestToActiveBorderColor() {

		final Color color = new Color("#007bff");
		assertEquals("", color.toActiveBorderColor().toHex(), "#0062cc");
	}

	@Test
	public void colorTestToColor() {

		final Color color = new Color("#007bff");
		assertEquals("", color.toColor().toHex3(), "#fff");
	}

	@Test
	public void colorTestToColor_Black() {

		final Color color = new Color("#e0a800");
		assertEquals("", color.toColor().toHex3(), "#111");
	}

	@Test
	public void colorTestToFocusShadowColor() {

		final Color color = new Color("#007bff");
		assertEquals("", color.toFocusShadowColor().toRgba(), "rgba(0, 123, 255, 0.5)");
	}
}
