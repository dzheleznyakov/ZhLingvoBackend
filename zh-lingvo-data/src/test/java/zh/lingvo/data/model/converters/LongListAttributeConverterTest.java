package zh.lingvo.data.model.converters;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Test List<Long>-to-String attribute converter")
class LongListAttributeConverterTest {
    private final LongListAttributeConverter converter = new LongListAttributeConverter();

    @Test
    @DisplayName("Should covert null to null")
    void convertNullToNull() {
        String dbValue = converter.convertToDatabaseColumn(null);

        assertThat(dbValue, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert an empty list to null")
    void convertEmptyListToNull() {
        String dbValue = converter.convertToDatabaseColumn(ImmutableList.of());

        assertThat(dbValue, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert a singleton list in a string that is just a number")
    void convertSingletonList() {
        String dbValue = converter.convertToDatabaseColumn(ImmutableList.of(42L));

        assertThat(dbValue, is("42"));
    }

    @Test
    @DisplayName("Should convert a list into a string of comma-separated numbers")
    void convertMultipleValueList() {
        String dbValue = converter.convertToDatabaseColumn(ImmutableList.of(42L, 100L, 33L));

        assertThat(dbValue, is("42,100,33"));
    }

    @Test
    @DisplayName("Should convert null DB value to an empty list")
    void convertNullToEmptyList() {
        List<Long> list = converter.convertToEntityAttribute(null);

        assertThat(list, is(empty()));
    }

    @Test
    @DisplayName("Should convert an empty string to an empty list")
    void convertEmptyStringToEmptyList() {
        List<Long> list = converter.convertToEntityAttribute("");

        assertThat(list, is(empty()));
    }

    @Test
    @DisplayName("Should convert a single number to a singleton list")
    void convertSingleNumberToSingletonList() {
        List<Long> list = converter.convertToEntityAttribute("42");

        assertThat(list, is(equalTo(ImmutableList.of(42L))));
    }

    @Test
    @DisplayName("Should convert properly formed string to a list")
    void convertStringToList() {
        List<Long> list = converter.convertToEntityAttribute("42,100,33");

        assertThat(list, is(equalTo(ImmutableList.of(42L, 100L, 33L))));
    }
}