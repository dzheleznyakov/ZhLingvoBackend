package zh.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

class OptionalHasPropertySatisfying<T, V> extends TypeSafeMatcher<Optional<T>> {
    private final Function<T, V> propertyExtractor;
    private final Predicate<V> propertyEvaluator;
    private V value;

    public OptionalHasPropertySatisfying(Function<T, V> propertyExtractor, Predicate<V> propertyEvaluator) {
        this.propertyExtractor = propertyExtractor;
        this.propertyEvaluator = propertyEvaluator;
    }

    @Override
    protected boolean matchesSafely(Optional<T> item) {
        return item
                .map(propertyExtractor)
                .map(v -> value = v)
                .filter(propertyEvaluator)
                .isPresent();
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(value);
    }

    @Override
    protected void describeMismatchSafely(Optional<T> item, Description mismatchDescription) {
        mismatchDescription.appendText("was ").appendValue(item.map(propertyExtractor).orElse(null));
    }
}
