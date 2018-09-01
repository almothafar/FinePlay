package test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.awt.Toolkit;
import java.nio.file.Path;
import java.util.Locale;

import javax.annotation.Nonnull;

import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Window;

import common.utils.Locales;
import play.test.TestBrowser;

public class Page extends FluentPage {

	private final TestBrowser browser;

	private static Dimension dimension;
	private static Point point;

	public Page(final TestBrowser browser) {

		try {

			getDriver();
		} catch (NullPointerException e) {

			System.out.println("throw NullPointerException by call of FluentPage#getDriver().");
		}

		this.browser = browser;
	}

	public TestBrowser getBrowser() {

		return browser;
	}

	@Override
	public WebDriver getDriver() {

		return getBrowser().getDriver();
	}
	@Override
	public String url() {
		return getBrowser().url();
	}

	@Override
	public String getBaseUrl() {

		return "http://localhost:" + play.api.test.Helpers.testServerPort();// 3333
	}

	@Override
	public void isAt() {

		assertThat("", getBrowser().url(), is(getUrl()));
	}

	public void takeScreenShot(//
			@Nonnull final Path path, //
			@Nonnull final Locale locale, //
			@Nonnull final Counter counter, //
			@Nonnull final String name) {

		final Window window = getDriver().manage().window();
		// window.setSize(getDimension());
		// window.setPosition(getPoint());

		// TODO
		getBrowser().takeScreenShot(path.resolve(Locales.toLang(locale).code()).resolve(counter.increment() + " " + name + ".png").toString());
	}

	@SuppressWarnings("null")
	private static Dimension getDimension() {

		if (dimension == null) {

			final java.awt.Dimension awtDimension = Toolkit.getDefaultToolkit().getScreenSize();
			dimension = new Dimension(getWindowWidth(awtDimension), getWindowHeight(awtDimension));
		}

		return dimension;
	}

	private static Point getPoint() {

		if (point == null) {

			final java.awt.Dimension awtDimension = Toolkit.getDefaultToolkit().getScreenSize();
			final int x = (int) (awtDimension.getWidth() - getWindowWidth(awtDimension)) / 2;
			final int y = (int) (awtDimension.getHeight() - getWindowHeight(awtDimension)) / 2;

			point = new Point(x, y);
		}

		return point;
	}

	private static int getWindowWidth(@Nonnull final java.awt.Dimension awtDimension) {

		return (int) (awtDimension.getWidth() - 100);
	}

	private static int getWindowHeight(@Nonnull final java.awt.Dimension awtDimension) {

		return (int) (awtDimension.getHeight() - 100);
	}
}
