package zh.lingvo.config.parser.syntax;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test StringValue")
class StringValueTest {
    @Test
    @DisplayName("Should return the held string value")
    void testGetString() {
        String str = "abcdef";
        StringValue value = new StringValue(str);

        assertThat(value.getString(), equalTo(str));
    }

    @Test
    @DisplayName("Should return an integer encoded in the string")
    void testGetInt() {
        StringValue value = new StringValue("42");

        assertThat(value.getInt(), equalTo(42));
    }

    @Test
    @DisplayName("Should return 0 if the string does not contain integer")
    void testGetInt_Failure() {
        StringValue value = new StringValue("42a");

        assertThat(value.getInt(), equalTo(0));
    }

    @Test
    @DisplayName("Should return a double value encoded in the string")
    void testGetDouble() {
        StringValue value = new StringValue("42.01");

        assertEquals(42.01, value.getDouble(), .001);
    }

    @Test
    @DisplayName("Should return 0.0 if the string does not contain a double value")
    void testGetDouble_Failure() {
        StringValue value = new StringValue("42a");

        assertEquals(0.0, value.getDouble(), .001);
    }

    @Test
    @DisplayName("Should return a boolean value encoded in the string")
    void testGetBoolean() {
        StringValue value = new StringValue("true");

        assertThat(value.getBoolean(), is(true));
    }
}