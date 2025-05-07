package nebula_ui;

import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.appmanagement.AndroidTerminateApplicationOptions;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.AutomationName;
import org.openqa.selenium.WebDriverException;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static nebula_ui.data.RunVariables.PLATFORM;
import static nebula_ui.data.RunVariables.SERVER_ADDRESS;

public class DriverProvider {



    public AppiumDriver createDriver(String device) {
        try {
            switch (PLATFORM) {
                case IOS:
                    XCUITestOptions iosOptions = getIOSCapabilities();
                    iosOptions.setUdid(device);
                    return new IOSDriver(new URL(SERVER_ADDRESS), iosOptions);
                case AOS:
                    UiAutomator2Options androidOptions = getAndroidCapabilities();
                    androidOptions.setUdid(device);
                    return new AndroidDriver(new URL(SERVER_ADDRESS), androidOptions);
                default:
                    throw new IllegalArgumentException("Unsupported platform: " + PLATFORM);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium server URL: " + SERVER_ADDRESS, e);
        }
    }

    private UiAutomator2Options getAndroidCapabilities() {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setAutomationName(AutomationName.ANDROID_UIAUTOMATOR2);
        options.setPlatformName("Android");
        options.setAppPackage(AOSPackage);
        options.setAppActivity(AOSActivity);
        options.setNewCommandTimeout(Duration.ofSeconds(60));
        options.setNoReset(true);
        return options;
    }

    private XCUITestOptions getIOSCapabilities() {
        XCUITestOptions options = new XCUITestOptions();
        options.setAutomationName(AutomationName.IOS_XCUI_TEST);
        options.setPlatformName("iOS");
        options.setDeviceName("iPhone");
        options.setBundleId(IOSBundleId);
        options.setNewCommandTimeout(Duration.ofSeconds(60));
        options.setNoReset(true);
        return options;
    }

    public void terminateApp() {
        switch (PLATFORM) {
            case AOS:
                AndroidTerminateApplicationOptions options = new AndroidTerminateApplicationOptions()
                        .withTimeout(Duration.ofSeconds(10));
                try {
                    getAndroidDriver().terminateApp(AOSPackage, options);
                } catch (WebDriverException e) {
                    e.printStackTrace();
                    activateApp();
                }
                break;
            case IOS:
                getIOSDriver().terminateApp(IOSBundleId);
                break;
            default:
                throw new IllegalArgumentException("Unsupported platform: " + PLATFORM);
        }
    }

    public void activateApp() {
        switch (PLATFORM) {
            case AOS:
                getAndroidDriver().activateApp(AOSPackage);
                break;
            case IOS:
                getIOSDriver().activateApp(IOSBundleId);
                break;
            default:
                throw new IllegalArgumentException("Unsupported platform: " + PLATFORM);
        }
    }
}
