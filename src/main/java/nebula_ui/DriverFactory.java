package nebula_ui;

import lombok.Getter;
import org.openqa.selenium.WebDriver;

import static nebula_ui.data.RunVariables.PLATFORM;

@Getter
public abstract class DriverFactory implements AutoCloseable {


    public abstract WebDriver getDriver();

    public static DriverFactory of() {

        switch (PLATFORM) {
            case AOS -> {
                return new AndroidDriverFactory();
            }
            case IOS -> {
                return new IosDriverFactory();
            }
            default -> throw new UnsupportedOperationException("Platform " + PLATFORM.name() + " not supported yet");
        }
    }

}
