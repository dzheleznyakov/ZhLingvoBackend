package zh.hamcrest;

import org.hamcrest.Matcher;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class ZhMatchers {
    public static Matcher<Optional<?>> empty() {
        return new OptionalIsEmpty();
    }

    public static <T, V> Matcher<Optional<T>> hasPropertySatisfying(Function<T, V> propertyExtractor, Predicate<V> propertyEvaluator) {
        return new OptionalHasPropertySatisfying<>(propertyExtractor, propertyEvaluator);
    }
}
