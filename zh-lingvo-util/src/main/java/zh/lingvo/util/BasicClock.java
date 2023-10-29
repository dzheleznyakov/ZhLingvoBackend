package zh.lingvo.util;

import com.google.common.annotations.VisibleForTesting;

public class BasicClock implements Clock {
    private static Clock INSTANCE = new BasicClock();

    private BasicClock() {}

    public static Clock get() {
        return INSTANCE;
    }

    @Override
    public long now() {
        return System.currentTimeMillis();
    }

    @VisibleForTesting
    static void setClock(Clock clock) {
        INSTANCE = clock;
    }
}
