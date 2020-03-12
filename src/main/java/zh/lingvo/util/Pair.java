package zh.lingvo.util;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Map;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equal(first, pair.first) &&
                Objects.equal(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(first, second);
    }

    public static <F, S> Pair<F, S> from(F first, S second) {
        return new Pair<>(first, second);
    }

    public static <F, S> Pair<F, S> from(Map.Entry<F, S> entry) {
        return new Pair<>(entry.getKey(), entry.getValue());
    }
}
