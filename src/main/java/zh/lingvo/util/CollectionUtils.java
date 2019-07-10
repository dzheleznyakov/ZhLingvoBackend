package zh.lingvo.util;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class CollectionUtils {
    public static <S, T> List<T> transform(Supplier<List<S>> listSupplier, Function<S, T> transform) {
        List<S> list = listSupplier.get();
        return list == null ? null : list.stream()
                .map(transform)
                .collect(ImmutableList.toImmutableList());
    }

    @SuppressWarnings("unchecked")
    public static <E> ImmutableList<E> toImmutableList(List<E> list) {
        return list == null
                ? null
                : list instanceof ImmutableList ? (ImmutableList) list : ImmutableList.copyOf(list);
    }
}
