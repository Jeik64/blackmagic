package nebula_ui;


import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.AutomationName;
import lombok.SneakyThrows;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;

import java.net.URL;
import java.time.Duration;

import static nebula_ui.data.RunVariables.DEVICE1;
import static nebula_ui.data.RunVariables.SERVER_ADDRESS;

public class IosDriverFactory extends DriverFactory {

    private final IOSDriver driver;

    private final String IOSBundleId = "com.genesismedia.Nebula.Horoscope";

    @SneakyThrows
    public IosDriverFactory() {
        this.driver = new IOSDriver(new URL(SERVER_ADDRESS), getCapabilities());
    }

    private Capabilities getCapabilities() {
        XCUITestOptions caps = new XCUITestOptions()
                .setAutomationName(AutomationName.IOS_XCUI_TEST)
                .setNewCommandTimeout(Duration.ofSeconds(60))
                .setUdid(DEVICE1)
                .setNoReset(false)
                .setPlatformName(Platform.IOS.name())
                .setBundleId(IOSBundleId);

        return caps;
    }

    @Override
    public WebDriver getDriver() {
        return driver;
    }

    @Override
    public void close() {
        driver.quit();
    }

}