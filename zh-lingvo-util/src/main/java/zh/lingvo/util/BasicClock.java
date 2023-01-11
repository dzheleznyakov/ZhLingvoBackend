package zh.lingvo.util;

public class BasicClock implements Clock {
    private static final Clock INSTANCE = new BasicClock();

    private BasicClock() {}

    public static Clock get() {
        return INSTANCE;
    }

    @Override
    public long now() {
        return System.currentTimeMillis();
    }
}
