
import com.codeborne.selenide.SelenideElement;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.windows.WindowsDriver;
import lombok.NonNull;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static nebula_ui.utils.AwaitUtil.INTERVAL_MS;
import static nebula_ui.utils.AwaitUtil.TIMEOUT_SEC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SuppressWarnings("unused")
public abstract class BaseScreen {
    private static final String VERTICAL_SCROLL_UI_AUTOMATOR = "new UiScrollable(new UiSelector().scrollable(true)).scrollToEnd(1,5)";
    private static final String HORIZONTAL_SCROLL_UI_AUTOMATOR = "new UiScrollable(new UiSelector().scrollable(true)).setAsHorizontalList().scrollToEnd(1,5)";
    private static final String VISIBLE_REGEX = ".*"; // Додано константу

    protected final WebDriver driver;
    private final FluentWait<WebDriver> fluentWait;
    private final AwaitUtil awaitUtil; // Додано поле

    /**
     * Створює новий екземпляр базового екрану
     * @param driver веб-драйвер для взаємодії з елементами
     */
    public BaseScreen(WebDriver driver) {
        this.driver = driver;
        this.fluentWait = new FluentWait<>(driver)
                .ignoreAll(List.of(NoSuchElementException.class, StaleElementReferenceException.class))
                .withTimeout(TIMEOUT_SEC)
                .pollingEvery(INTERVAL_MS);
        this.awaitUtil = new AwaitUtil();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    // Виправлено опечатку в назві методу
    protected WebElement waitUntilElementIsClickable(WebElement element) {
        return fluentWait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Перевіряє наявність тексту на екрані
     * @param expectedText текст для пошуку
     */
    public void isTextOnScreen(String expectedText) {
        String pageSource = awaitUtil.waitingFor(driver::getPageSource,
                        source -> source.contains(expectedText))
                .orElseGet(driver::getPageSource);
        assertThat(pageSource).contains(expectedText);
    }

    protected void typeTextSlowly(WebElement element, String text) {
        if (text == null) return;
        
        waitAndClick(element);
        Actions actions = new Actions(driver);
        actions.click(element);
        
        // Оптимізовано розбиття рядка
        for (char letter : text.toCharArray()) {
            actions.sendKeys(String.valueOf(letter));
        }
        actions.build().perform();
    }

    @SuppressWarnings("UnusedReturnValue")
    protected WebElement waitAndFill(WebElement element, boolean isMaskedField, CharSequence... keysToSend) {
        if (keysToSend == null) {
            throw new IllegalArgumentException("keysToSend cannot be null");
        }
        
        WebElement webElement = waitUntilElementIsClickable(element);
        webElement.sendKeys(keysToSend);
        int inputTextLength = Stream.of(keysToSend).mapToInt(CharSequence::length).sum();
        awaitUtil.waitingFor(() -> isMaskedField || webElement.getText().length() >= inputTextLength);
        return webElement;
    }

    /**
     * Закриває клавіатуру для різних типів драйверів
     * @throws UnsupportedOperationException якщо операція не підтримується для поточного драйвера
     */
    public void closeKeyboard() {
        if (driver instanceof AndroidDriver androidDriver) {
            androidDriver.hideKeyboard();
        } else if (driver instanceof IOSDriver iosDriver) {
            iosDriver.hideKeyboard();
        } else if (driver instanceof WindowsDriver) {
            // Windows не потребує закриття клавіатури
            return;
        }
        throw new UnsupportedOperationException("'HideKeyboard' command not supported for "
                + driver.getClass().getName() + " driver");
    }

    protected void isElementOnScreen(String expectedElement) {
        new AwaitUtil().waitingFor(() -> Pattern.matches(VISIBLE_REGEX, expectedElement), (Boolean value) -> value);
        assertThat(expectedElement).isVisible();
    }

    protected WebElement waitAndClick(WebElement element) {
        WebElement webElement = waitUntilElementIsClickable(element);
        webElement.click();
        return webElement;
    }


    @SuppressWarnings("UnusedReturnValue")
    protected WebElement waitAndFill(WebElement element, ValueSupplier<? extends CharSequence>... keysToSend) {
        CharSequence[] keys = Arrays.stream(keysToSend)
                .map(ValueSupplier::getValue)
                .toArray(CharSequence[]::new);
        return waitAndFill(element, false, keys);
    }

    protected WebElement clickAndFill(WebElement element, CharSequence... keysToSend) {
        return waitAndFill(waitAndClick(element), false, keysToSend);
    }

    protected WebElement clickAndFill(WebElement element, String keysToSend) {
        return waitAndFill(waitAndClick(element), keysToSend);
    }


    public void scrollToEndVertically(SelenideElement selenideElement) {
        scrollToEnd(VERTICAL_SCROLL_UI_AUTOMATOR, selenideElement);
    }

    public void scrollToEndHorizontally(SelenideElement selenideElement) {
        scrollToEnd(HORIZONTAL_SCROLL_UI_AUTOMATOR, selenideElement);
    }

    private void scrollToEnd(String scroll, SelenideElement selenideElement) {
        new nebula_ui.utils.AwaitUtil().waitingFor(
                () -> {
                    new WebElementFinderUtil().findWebElement(driver, scroll);
                    return selenideElement;
                },
                element -> element.isDisplayed() && element.isEnabled());
    }



    public void clickAtCoordinates(@NonNull Integer coordinateX, @NonNull Integer coordinateY) {
        if (driver instanceof AppiumDriver appiumDriver) {
            await().pollDelay(Duration.ofSeconds(1)).until(() -> true);
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1)
                    .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), coordinateX, coordinateY))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            appiumDriver.perform(Collections.singletonList(tap));
        } else {
            throw new UnsupportedOperationException("'Click at coordinates' jperation supported for appium driver only : " + driver.getClass());
        }
    }


}