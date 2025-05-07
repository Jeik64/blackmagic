package nebula_ui;

import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.AppiumDriver;
import nebula_ui.data.Platform;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import static nebula_ui.data.RunVariables.DEVICE1;
import static nebula_ui.data.RunVariables.PLATFORM;

public class BaseTest {
    private boolean existEnvironment = false;
    private final DriverProvider driverProvider = new DriverProvider();
    protected AppiumDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp()  {
        driver = driverProvider.createDriver(DEVICE1);
        WebDriverRunner.setWebDriver(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (!existEnvironment) {
            existEnvironment = true;
        }
        driverProvider.terminateApp();
        if (PLATFORM == Platform.AOS) {
            if (result.getStatus() == ITestResult.FAILURE) {
                DriverProvider.getAndroidDriver().navigate().back();
            }
        }
        WebDriverRunner.closeWebDriver();
    }

    @AfterSuite
    public void afterSuite() {
        Path allureReportPath = Paths.get("target/allure-report");
        Path resultsPath = Paths.get("target/allure-results");


    }
}