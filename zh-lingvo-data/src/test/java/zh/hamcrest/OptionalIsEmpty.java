package zh.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Objects;
import java.util.Optional;

class OptionalIsEmpty extends TypeSafeMatcher<Optional<?>> {
    @Override
    protected boolean matchesSafely(Optional<?> item) {
        return item.isEmpty();
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(null);
    }

    @Override
    protected void describeMismatchSafely(Optional<?> item, Description mismatchDescription) {
        mismatchDescription.appendText("was ").appendValue(item.orElseThrow());
    }
}
