package nebula_ui.tests;

import nebula_ui.BaseTest;
import org.testng.annotations.Test;
import nebula_ui.screens.signUp.FirstOpenScreen;

public class SignUpTests extends BaseTest {

    FirstOpenScreen firstOpenScreen = new FirstOpenScreen(driver);

    @Test
    public void positiveSignUpTest(){
    firstOpenScreen.startSignUp();
    }
}
