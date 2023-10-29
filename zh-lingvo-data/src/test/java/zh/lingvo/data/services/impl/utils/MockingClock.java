package zh.lingvo.data.services.impl.utils;

import zh.lingvo.util.BasicClock;
import zh.lingvo.util.Clock;

import java.util.ArrayList;
import java.util.List;

public class MockingClock implements Clock {
    private static final Clock DEFAULT = BasicClock.get();

    private final List<Long> times = new ArrayList<>();
    private int nextTimeIndex = 0;

    public void setTime(long time) {
        times.add(time);
    }

    public void setTime(long... times) {
        for (long time : times)
            this.times.add(time);
    }

    @Override
    public long now() {
        if (times.isEmpty())
            return DEFAULT.now();
        if (nextTimeIndex == times.size())
            return times.get(nextTimeIndex - 1);
        return times.get(nextTimeIndex++);
    }
}
