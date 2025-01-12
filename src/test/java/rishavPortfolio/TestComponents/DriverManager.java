package rishavPortfolio.TestComponents;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverManager {

	private Properties properties;

	public DriverManager() throws IOException {
		properties = new Properties();
		String propertiesPath = System.getProperty("user.dir")
				+ "/src/test/java/rishavPortfolio/Resources/GlobalData.properties";
		try (FileInputStream fil = new FileInputStream(propertiesPath)) {
			properties.load(fil);
		}
	}

	public WebDriver initializeDriver() throws MalformedURLException {
		String browserName = System.getProperty("Browser") != null ? System.getProperty("Browser")
				: properties.getProperty("Browser");
		String platform = properties.getProperty("platform");

		if ("grid".equalsIgnoreCase(platform)) {
			return initializeGridDriver(browserName);
		} else {
			return initializeLocalDriver(browserName);
		}
	}

	private WebDriver initializeGridDriver(String browserName) throws MalformedURLException {
		String hubURL = properties.getProperty("HubLink");
		if (hubURL == null || hubURL.isEmpty()) {
			throw new IllegalArgumentException("HubLink property must be defined in the GlobalData.properties file.");
		}

		if ("chrome".equalsIgnoreCase(browserName)) {
			ChromeOptions options = new ChromeOptions();
			return new RemoteWebDriver(new URL(hubURL), options);
		} else if ("firefox".equalsIgnoreCase(browserName)) {
			FirefoxOptions options = new FirefoxOptions();
			return new RemoteWebDriver(new URL(hubURL), options);
		} else {
			throw new IllegalArgumentException("Unsupported browser: " + browserName);
		}
	}

	private WebDriver initializeLocalDriver(String browserName) {
		if ("chrome".equalsIgnoreCase(browserName)) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			return new org.openqa.selenium.chrome.ChromeDriver(options);
		} else if (browserName.contains("chrome_headless")) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			if (browserName.contains("headless")) {
				options.addArguments("headless");
				options.addArguments("window-size=1440,900"); // Ensure proper window size in headless mode
			}
			return new org.openqa.selenium.chrome.ChromeDriver(options);
		}

		else if ("firefox".equalsIgnoreCase(browserName)) {
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions options = new FirefoxOptions();
			return new org.openqa.selenium.firefox.FirefoxDriver(options);
		} else {
			throw new IllegalArgumentException("Unsupported browser: " + browserName);
		}
	}
}
