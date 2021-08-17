package zh.config.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import zh.config.parser.lexer.Lexer;
import zh.config.parser.syntax.ConfigValue;
import zh.config.parser.syntax.ListValue;
import zh.config.parser.syntax.MapValue;
import zh.config.parser.syntax.StringValue;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static zh.config.parser.SyntaxError.Type.MISMATCHING_BRACE;
import static zh.config.parser.SyntaxError.Type.MISMATCHING_BRACKET;
import static zh.config.parser.SyntaxError.Type.SYNTAX;

@DisplayName("Test Config Parser")
class ConfigParserTest implements Builder<String> {
    private final ConfigParser parser = new ConfigParser();
    private final Lexer lexer = new Lexer(parser);

    private List<SyntaxError> errors;
    private int indentLevel = 0;

    @Override
    public String build(ConfigValue value, List<SyntaxError> errors) {
        this.errors = errors;
        return toString(value);
    }

    private String toString(ListValue value) {
        return value.getList().stream()
                .map(this::toString)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    private String toString(StringValue value) {
        return "'" + value.getString() + "'";
    }

    private String toString(MapValue value) {
        String suffix = "\n" + indent() + "}";
        ++indentLevel;
        String res = value.getMap().entrySet()
                .stream()
                .map(entry -> "\n" + indent() + entry.getKey() + " => " + toString(entry.getValue()))
                .collect(Collectors.joining("", "{", suffix));
        --indentLevel;
        return res;
    }

    private String toString(ConfigValue value) {
        if (value instanceof ListValue)
            return toString((ListValue) value);
        if (value instanceof StringValue)
            return toString((StringValue) value);
        if (value instanceof MapValue)
            return toString((MapValue) value);
        return "";
    }

    private String indent() {
        return "  ".repeat(indentLevel);
    }

    private void assertParsed(String input, String expected) {
        lexer.lex(input);

        String actual = parser.build(this);

        assertThat(actual, is(equalTo(expected)));
    }

    private void assertErrors(String input, SyntaxError error) {
        lexer.lex(input);

        parser.build(this);

        assertThat(errors, contains(error));
    }

    @Nested
    class SimpleEntries {
        @Test
        @DisplayName("Parse empty config")
        void parseEmptyConfig() {
            assertParsed("", "" +
                    "{\n" +
                    "}");
        }

        @Test
        @DisplayName("Parse a config with a single name entry")
        void singleEntry_SimpleEntry() {
            assertParsed("key1 value1", "" +
                    "{\n" +
                    "  key1 => 'value1'\n" +
                    "}");
        }

        @Test
        @DisplayName("Parse a config with a single simple string entry")
        void singleEntry_SimpleString() {
            assertParsed("key1 #value1#", "" +
                    "{\n" +
                    "  key1 => '#value1#'\n" +
                    "}");
        }

        @Test
        @DisplayName("Parse a config with a single string entry")
        void singleEntry_String() {
            assertParsed("key1 \"three words value\"", "" +
                    "{\n" +
                    "  key1 => 'three words value'\n" +
                    "}");
        }

        @Test
        @DisplayName("Parse a config with a single list entry")
        void singleEntry_List() {
            assertParsed("key1 [a b c]", "" +
                    "{\n" +
                    "  key1 => ['a', 'b', 'c']\n" +
                    "}");
        }

        @Test
        @DisplayName("Parse a config with a single map entry")
        void singleEntry_Map() {
            assertParsed("external_key { internal_key [a b\tc]\n}", "" +
                    "{\n" +
                    "  external_key => {\n" +
                    "    internal_key => ['a', 'b', 'c']\n" +
                    "  }\n" +
                    "}");
        }

        @Test
        @DisplayName("Parse a config with multiple simple entries")
        void multipleEntries() {
            assertParsed("" +
                    "name_key some_name simple_string_key some_simple_string!\n" +
                    "\tstring_key \"Here comes the string!\"\n" +
                    "\t\tlist_key\n" +
                    "[1 2 \"Gerard D'Pardie\"]\n" +
                    "map_key {one uno}",
                    "" +
                            "{\n" +
                            "  name_key => 'some_name'\n" +
                            "  simple_string_key => 'some_simple_string!'\n" +
                            "  string_key => 'Here comes the string!'\n" +
                            "  list_key => ['1', '2', 'Gerard D'Pardie']\n" +
                            "  map_key => {\n" +
                            "    one => 'uno'\n" +
                            "  }\n" +
                            "}");
        }
    }

    @Nested
    class NestedEntries {
        @Test
        @DisplayName("Should parse a list containing another list within")
        void listInsideList() {
            assertParsed("" +
                    "names [\n" +
                    "\t[John Doe]\n" +
                    "\t[Jane Dow]\n" +
                    "\t[Parker Johnson]\n" +
                    "\t[John Parkerson]\n" +
                    "]",
                    "" +
                            "{\n" +
                            "  names => [['John', 'Doe'], ['Jane', 'Dow'], ['Parker', 'Johnson'], ['John', 'Parkerson']]\n" +
                            "}");
        }

        @Test
        @DisplayName("Should parse a list contained in a map")
        void listInsideMap() {
            assertParsed(
                    "" +
                            "dictionary {\n" +
                            "   english_to_spanish [one uno]\n" +
                            "   english_to_german [one ein]\n" +
                            "   english_to_russian [one один]\n" +
                            "}",
                    "" +
                            "{\n" +
                            "  dictionary => {\n" +
                            "    english_to_spanish => ['one', 'uno']\n" +
                            "    english_to_german => ['one', 'ein']\n" +
                            "    english_to_russian => ['one', 'один']\n" +
                            "  }\n" +
                            "}"
            );
        }

        @Test
        @DisplayName("Should parse a map contained in another map")
        void mapInsideMap() {
            assertParsed(
                    "" +
                            "m1 { m11{m111 v1}\n" +
                            "m12 {m121 v2 m122 v3}" +
                            "}",
                    "" +
                            "{\n" +
                            "  m1 => {\n" +
                            "    m11 => {\n" +
                            "      m111 => 'v1'\n" +
                            "    }\n" +
                            "    m12 => {\n" +
                            "      m121 => 'v2'\n" +
                            "      m122 => 'v3'\n" +
                            "    }\n" +
                            "  }\n" +
                            "}"
            );
        }

        @Test
        @DisplayName("Should parse a map contained in a list")
        void mapInsideArray() {
            assertParsed(
                    "" +
                            "dictionaries [\n" +
                            "  { one uno two dos three tres }\n" +
                            "  { one ein two zwei three drei }\n" +
                            "]",
                    "" +
                            "{\n" +
                            "  dictionaries => [{\n" +
                            "    one => 'uno'\n" +
                            "    two => 'dos'\n" +
                            "    three => 'tres'\n" +
                            "  }, {\n" +
                            "    one => 'ein'\n" +
                            "    two => 'zwei'\n" +
                            "    three => 'drei'\n" +
                            "  }]\n" +
                            "}");
        }
    }

    @Nested
    class AcceptanceTests {
        @Test
        @DisplayName("Should parse a lingvo config")
        void lingvoConfig() {
            assertParsed(
                    "" +
                            "parts_of_speech [\n" +
                            "  [NOUN n]\n" +
                            "  [VERB v]\n" +
                            "  [ADJECTIVE adj]\n" +
                            "]\n" +
                            "\n" +
                            "\n" +
                            "English {\n" +
                            "    name English\n" +
                            "    code En\n" +
                            "    parts_of_speech [\n" +
                            "        [NOUN n noun]\n" +
                            "        [VERB v verb]\n" +
                            "        [ADJECTIVE adj adjective]\n" +
                            "    ]\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "Russian {\n" +
                            "    name Русский\n" +
                            "    code Ru\n" +
                            "    parts_of_speech [\n" +
                            "        [NOUN сущ. \"имя существительное\"]\n" +
                            "        [VERB гл. глагол]\n" +
                            "        [ADJECTIVE прил. \"имя прилагательное\"]\n" +
                            "    ]\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "Spanish {\n" +
                            "    name Español\n" +
                            "    code Es\n" +
                            "    parts_of_speech [\n" +
                            "        [NOUN n \"nombre sustantivo\"]\n" +
                            "        [VERB v verbo]\n" +
                            "        [ADJECTIVE adj adjetivo]\n" +
                            "    ]\n" +
                            "}",
                    "" +
                            "{\n" +
                            "  parts_of_speech => [['NOUN', 'n'], ['VERB', 'v'], ['ADJECTIVE', 'adj']]\n" +
                            "  English => {\n" +
                            "    name => 'English'\n" +
                            "    code => 'En'\n" +
                            "    parts_of_speech => [['NOUN', 'n', 'noun'], ['VERB', 'v', 'verb'], ['ADJECTIVE', 'adj', 'adjective']]\n" +
                            "  }\n" +
                            "  Russian => {\n" +
                            "    name => 'Русский'\n" +
                            "    code => 'Ru'\n" +
                            "    parts_of_speech => [['NOUN', 'сущ.', 'имя существительное'], ['VERB', 'гл.', 'глагол'], ['ADJECTIVE', 'прил.', 'имя прилагательное']]\n" +
                            "  }\n" +
                            "  Spanish => {\n" +
                            "    name => 'Español'\n" +
                            "    code => 'Es'\n" +
                            "    parts_of_speech => [['NOUN', 'n', 'nombre sustantivo'], ['VERB', 'v', 'verbo'], ['ADJECTIVE', 'adj', 'adjetivo']]\n" +
                            "  }\n" +
                            "}");
        }
    }

    @Nested
    class Errors {
        @Test
        @DisplayName("Should make a syntax error if a map is closed with a bracket")
        void mapClosedWithBracket() {
            assertErrors("external_key {internal_key internal_value\n]",
                    new SyntaxError(MISMATCHING_BRACKET, "",2, 1));
        }

        @Test
        @DisplayName("Should make a syntax error if a list is closed with a brace")
        void listClosedWithBrace() {
            assertErrors("key [a b c}",
                    new SyntaxError(MISMATCHING_BRACE, "", 1, 11));
        }

        @Test
        @DisplayName("Should make a syntax error if a string is not closed")
        void unclosedString() {
            assertErrors("key [\"abc]",
                    new SyntaxError(SYNTAX, "", 1, 10));
        }
    }
}