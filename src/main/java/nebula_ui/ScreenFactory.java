package nebula_ui;

import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ScreenFactory {

    private static final AtomicReference<ScreenFactory> SCREEN_FACTORY = new AtomicReference<>();

    private final DriverFactory driverFactory;
    private final Map<Class<? extends BaseScreen>, BaseScreen> screens;

    private ScreenFactory() {
        this.driverFactory = DriverFactory.of();
        this.screens = ReflectionUtil.instantiateSubclasses(BaseScreen.class,
                Pair.of(WebDriver.class, this.driverFactory.getDriver()));
    }

    private static ScreenFactory init() {
        return SCREEN_FACTORY.updateAndGet((ScreenFactory existingScreenFactory) ->
                Objects.isNull(existingScreenFactory) ? new ScreenFactory() : existingScreenFactory);
    }

    public static <T extends BaseScreen> T loadScreen(Class<T> clazz) {
        return Optional.ofNullable((T) init().screens.get(clazz))
                .orElseThrow(() -> new RuntimeException("Instance of class '" + clazz.getName() + "' not exist"));
    }

    public static void close() {
        Optional.ofNullable(SCREEN_FACTORY.getAndSet(null)).ifPresent(ScreenFactory::close);
    }

    @SneakyThrows
    private static void close(ScreenFactory screenFactory) {
        screenFactory.driverFactory.close();
    }
}
