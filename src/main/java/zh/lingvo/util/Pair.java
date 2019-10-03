package zh.lingvo.util;

import com.google.common.base.MoreObjects;

public class Pair<F, S> {
    private final F first;
    private final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("first", first)
                .add("second", second)
                .toString();
    }

    public static <F, S> Pair<F, S> from(F first, S second) {
        return new Pair<>(first, second);
    }
}
