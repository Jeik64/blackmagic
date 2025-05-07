package nebula_ui.utils;

import org.awaitility.core.ConditionFactory;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import static org.awaitility.Awaitility.await;

public class AwaitUtil {

    public final static Duration TIMEOUT_SEC = Duration.ofSeconds(20);
    public final static Duration INTERVAL_MS = Duration.ofMillis(200);

    private final AtomicBoolean stopFlag = new AtomicBoolean(false);

    public void waitingFor(Callable<Boolean> condition) {
        factory().until(condition);
    }

    public <T> Optional<T> waitingFor(Callable<T> supplier, Predicate<? super T> predicate) {
        try {
            return Optional.ofNullable(factory().until(supplier, predicate));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public ConditionFactory factory() {
        return await()
                .failFast(stopFlag::get)
                .ignoreExceptions()
                .pollDelay(Duration.ofSeconds(0))
                .pollInterval(INTERVAL_MS)
                .atMost(TIMEOUT_SEC);
    }

    public void stop() {
        stopFlag.set(true);
    }
}