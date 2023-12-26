package zh.lingvo.config.parser.syntax;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@DisplayName("Test ListValue")
class ListValueTest {
    @Test
    @DisplayName("Should hold the list of config values")
    void testGetList() {
        ConfigValue v1 = new StringValue("alpha");
        ConfigValue v2 = new StringValue("beta");
        ConfigValue v3 = new StringValue("gamma");

        ListValue listValue = ListValue.builder()
                .add(v1)
                .add(v2)
                .add(v3)
                .build();

        List<ConfigValue> configValues = listValue.getList();

        assertThat(configValues, hasSize(3));
        assertThat(configValues.get(0), is(v1));
        assertThat(configValues.get(1), is(v2));
        assertThat(configValues.get(2), is(v3));
    }
}