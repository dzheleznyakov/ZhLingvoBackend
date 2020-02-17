package zh.lingvo.util

import com.google.common.collect.ImmutableList

class KCollectionUtils {
    companion object Util {
        @JvmStatic
        fun <S, T> transform(listSupplier: () -> List<S>?, transform: (S) -> T) =
            listSupplier.invoke()?.map(transform)?.toImmutableList()

        @JvmStatic
        fun <S, K, V> transformToMap(
                collectionSupplier: () -> Collection<S>,
                keyTransformer: (S) -> K,
                valueTransformer: (S) -> V
        ) = collectionSupplier.invoke() ?: null

        @JvmStatic
        fun <E> Collection<E>.toImmutableList() =
                if (this is ImmutableList) this else ImmutableList.copyOf(this)
    }
}
