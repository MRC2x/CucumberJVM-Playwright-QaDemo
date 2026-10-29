package infrastructure.constants;

import java.time.Duration;

public final class Timeouts {

    private Timeouts() {}

    public static Duration LONG_TIMEOUT = Duration.ofSeconds(15);
    public static Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    public static Duration MEDIUM_TIMEOUT = DEFAULT_TIMEOUT.dividedBy(2);
    public static Duration SMALL_TIMEOUT = DEFAULT_TIMEOUT.dividedBy(3);
}
