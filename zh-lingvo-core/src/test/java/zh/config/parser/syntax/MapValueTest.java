package zh.config.parser.syntax;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DisplayName("Test MapValue")
class MapValueTest {
    @Test
    @DisplayName("Should hold the mapped valued")
    void testGetMap() {
        String k1 = "one";
        String k2 = "two";
        String k3 = "three";

        StringValue v1 = new StringValue("uno");
        StringValue v2 = new StringValue("dos");
        StringValue v3 = new StringValue("tres");

        MapValue mapValue = MapValue.builder()
                .put(k1, v1)
                .put(k2, v2)
                .put(k3, v3)
                .build();

        Map<String, ConfigValue> map = mapValue.getMap();

        assertThat(map.size(), is(3));
        assertThat(map.get(k1), is(v1));
        assertThat(map.get(k2), is(v2));
        assertThat(map.get(k3), is(v3));
    }
}