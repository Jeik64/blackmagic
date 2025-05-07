package nebula_ui.screens.signUp;

import com.codeborne.selenide.SelenideElement;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import nebula_ui.screens.BaseScreen;

import static nebula_ui.data.RunVariables.PLATFORM;

public class FirstOpenScreen extends BaseScreen {

    public FirstOpenScreen(AppiumDriver driver) {
        super(driver);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"GET STARTED\"]")
    @iOSXCUITFindBy()
    private static   SelenideElement signUpButton;

    @AndroidFindBy()
    @iOSXCUITFindBy()
    private static SelenideElement signInButton;

    @AndroidFindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View")
    private static SelenideElement getStartedButton;

    public void startSignIn(){
        signUpButton.click();
    }

    public void startSignUp() {
        switch (PLATFORM) {
            case IOS:
                signUpButton.click();
            case AOS:
                getStartedButton.click();
                signUpButton.click();
        }

    }
}
