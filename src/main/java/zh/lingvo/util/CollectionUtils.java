package zh.lingvo.util;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class CollectionUtils {
    public static <S, T> List<T> transform(Supplier<List<S>> listSupplier, Function<S, T> transform) {
        List<S> list = listSupplier.get();
        return list == null ? null : list.stream()
                .map(transform)
                .collect(ImmutableList.toImmutableList());
    }

    public static <S, K, V> Map<K, V> transformToMap(
            Supplier<Collection<S>> collectionSupplier,
            Function<S, K> keyTransformer,
            Function<S, V> valueTransformer
    ) {
        Collection<S> collection = collectionSupplier.get();
        return collection == null ? null : collection.stream()
                .map(s -> new Pair<K, V>(keyTransformer.apply(s), valueTransformer.apply(s)))
                .collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond));
    }

    @SuppressWarnings("unchecked")
    public static <E> ImmutableList<E> toImmutableList(List<E> list) {
        return list == null
                ? null
                : list instanceof ImmutableList ? (ImmutableList) list : ImmutableList.copyOf(list);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> ImmutableMap<K, V> toImmutableMap(Map<K, V> map) {
        return map == null
                ? null
                : map instanceof ImmutableMap ? (ImmutableMap) map : ImmutableMap.copyOf(map);
    }

    @NotNull
    public static <E> List<E> getNotNull(Supplier<List<E>> getter) {
        return MoreObjects.firstNonNull(getter.get(), ImmutableList.of());
    }

    public static <E> boolean isNullOrEmpty(List<E> list) {
        return list == null || list.isEmpty();
    }

    public static <E> List<E> remove(List<E> list, int index) {
        return list == null || index < 0 || index >= list.size() ? list : IntStream.range(0, list.size())
                .filter(i -> i != index)
                .mapToObj(list::get)
                .collect(ImmutableList.toImmutableList());
    }

    public static <E> List<E> add(List<E> list, E item) {
        return ImmutableList.<E>builder()
                .addAll(getNotNull(() -> list))
                .add(item)
                .build();
    }
}
