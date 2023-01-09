package zh.lingvo.data.model.converters;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Test Map<Long, Boolean>-to-String attribute converter")
class LongToBooleanMapAttributeConverterTest {
    private final LongToBooleanMapAttributeConverter converter = new LongToBooleanMapAttributeConverter();

    @Test
    @DisplayName("Should convert a null map to null")
    void convertNullMapToNull() {
        String dbValue = converter.convertToDatabaseColumn(null);

        assertThat(dbValue, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert an empty map to null")
    void convertEmptyMapToNull() {
        String dbValue = converter.convertToDatabaseColumn(ImmutableMap.of());

        assertThat(dbValue, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert a singleton map to a string")
    void convertSingletonMap() {
        String dbValue = converter.convertToDatabaseColumn(ImmutableMap.of(42L, true));

        assertThat(dbValue, is("42:1"));
    }

    @Test
    @DisplayName("Should convert a map to a string; true is mapped to 1, false to 0")
    void convertMap() {
        String dbValue = converter.convertToDatabaseColumn(ImmutableMap.of(42L, true, 100L, false, 33L, true));

        assertThat(dbValue, is("42:1,100:0,33:1"));
    }

    @Test
    @DisplayName("Should convert a null string to an empty map")
    void convertNullStringToEmptyMap() {
        Map<Long, Boolean> map = converter.convertToEntityAttribute(null);

        assertThat(map, is(anEmptyMap()));
    }

    @Test
    @DisplayName("Should convert an empty string to an empty map")
    void convertEmptyStringToEmptyMap() {
        Map<Long, Boolean> map = converter.convertToEntityAttribute("");

        assertThat(map, is(anEmptyMap()));
    }

    @Test
    @DisplayName("Should convert a properly formed string to a map")
    void convertStringToList() {
        Map<Long, Boolean> map = converter.convertToEntityAttribute("42:1,100:0,33:1");

        assertThat(map, is(equalTo(ImmutableMap.of(42L, true, 100L, false, 33L, true))));
    }
}