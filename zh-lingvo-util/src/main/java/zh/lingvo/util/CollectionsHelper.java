package zh.lingvo.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static com.google.common.base.MoreObjects.firstNonNull;

public class CollectionsHelper {
    private CollectionsHelper() {
    }

    public static <E> Set<E> getSetSafely(Supplier<Set<E>> supplier) {
        return firstNonNull(supplier.get(), ImmutableSet.of());
    }

    public static <E> List<E> getListSafely(Supplier<List<E>> supplier) {
        return firstNonNull(supplier.get(), ImmutableList.of());
    }

    public static <E> Collector<E, LinkedHashSet<E>, LinkedHashSet<E>> toLinkedHashSet() {
        return Collector.of(
                LinkedHashSet::new,
                LinkedHashSet::add,
                (es, es2) -> {
                    es.addAll(es2);
                    return es;
                }
        );
    }
}
