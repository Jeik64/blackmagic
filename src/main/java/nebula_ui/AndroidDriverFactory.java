package nebula_ui;


import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.remote.AutomationName;
import lombok.SneakyThrows;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;

import java.net.URL;
import java.time.Duration;

import static nebula_ui.data.RunVariables.DEVICE1;
import static nebula_ui.data.RunVariables.SERVER_ADDRESS;

public class AndroidDriverFactory extends DriverFactory {

    private final String AOSPackage = "genesis.nebula";
    private final String AOSActivity = "genesis.nebula.module.activity.MainActivity";
    private final AndroidDriver driver;

    @SneakyThrows
    public AndroidDriverFactory() {
        this.driver = new AndroidDriver(new URL(SERVER_ADDRESS), getCapabilities());
    }

    //https://github.com/appium/appium-uiautomator2-driver/blob/master/README.md
    private Capabilities getCapabilities() {
        UiAutomator2Options uiAutomator2Options = new UiAutomator2Options()
                .setAppPackage(AOSPackage)
                .setAppActivity(AOSActivity)
                .setAutomationName(AutomationName.ANDROID_UIAUTOMATOR2)
                .setNewCommandTimeout(Duration.ofSeconds(60))
                .setDeviceName(DEVICE1)
                .setNoReset(false)
                .setPlatformName(Platform.ANDROID.name())
                .setAppWaitForLaunch(true);

        return uiAutomator2Options;
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
