package co.com.whatsapp.drivers;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class DriverWhatsapp {
	private final static Logger LOGGER = Logger.getLogger("Clase driver");

	private static AppiumDriver driver;

	public static DriverWhatsapp hisBrowserMovil() {
		try {
			DesiredCapabilities capabilities = new DesiredCapabilities();

			capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "android");
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "6PW4USGAJF9XCU4P");
			//capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
			capabilities.setCapability(MobileCapabilityType.APP, "");
			//capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "7.1");
			capabilities.setCapability("appPackage", "com.whatsapp");
			capabilities.setCapability("appActivity", "com.whatsapp.HomeActivity");
			capabilities.setCapability("noReset", true);
			capabilities.setCapability("unicodeKeyboard", true);
			capabilities.setCapability("resetKeyboard", true);
			driver = new AppiumDriver(new URL(" http://127.0.0.1:4724/wd/hub"), capabilities);

			driver.setLogLevel(Level.INFO);

		} catch (Exception e) {
			LOGGER.log(Level.WARNING, e.getMessage());
		}
		return new DriverWhatsapp();
	}

	public AppiumDriver android() {
		return driver;
	}
}
