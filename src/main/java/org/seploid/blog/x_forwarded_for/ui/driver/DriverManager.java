package org.seploid.blog.x_forwarded_for.ui.driver;

import org.apache.http.HeaderElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class DriverManager {

	private static final String URL = "http://%s:%s/wd/hub";
	private static final String CHROME_PATH = "chromedriver.exe";
	private static final String FIREFOX_PATH = "geckodriver.exe";

	public static WebDriver getWebDriverWithCustomHeader(String host, String port, BrowserType browserType,
			DeviceType deviceType, List<HeaderElement> headerElements) {
		CapabilityBuilder builder = new CapabilityBuilder(browserType);
		builder.setHeaderElements(headerElements);
		builder.setUserAgent(deviceType.getUserAgent());
		DesiredCapabilities capabilities = builder.build();
		try {
			HttpCommandExecutor executor = new HttpCommandExecutor(new URL(String.format(URL, host, port)));
			return new RemoteWebDriver(executor, capabilities);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param host
	 * @param port
	 * @param browserType
	 * @param deviceType
	 * @param headerElements
	 * @return
	 */
	public static WebDriver getWebDriverWithCustomHeaderLocal(BrowserType browserType, DeviceType deviceType,
			List<HeaderElement> headerElements) {
		
		CapabilityBuilder builder = new CapabilityBuilder(browserType);
		builder.setHeaderElements(headerElements);
		builder.setUserAgent(deviceType.getUserAgent());
		DesiredCapabilities capabilities = builder.build();
		switch (browserType) {
			case CHROME:
				System.setProperty("webdriver.chrome.driver", DriverManager.class.getClassLoader().getResource(CHROME_PATH).getFile());
				return new ChromeDriver(capabilities);
			case FIREFOX:
				System.setProperty("webdriver.gecko.driver", DriverManager.class.getClassLoader().getResource(FIREFOX_PATH).getFile());
				return new FirefoxDriver(capabilities);
			default:
				return null;
		}
	}

	public static void main(String[] args) {
		System.out.println(DriverManager.class.getClassLoader().getResource("geckodriver.exe").getFile());
	}
}
