package zh.config.parser.lexer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@DisplayName("Test Zh Config Lexer")
class LexerTest implements TokenCollector {
    private String output = "";
    private final Lexer lexer = new Lexer(this);

    private void add(String s) {
        if (!output.isEmpty())
            output += " ";
        output += s;
    }

    private void assertLexed(String input, String expected) {
        lexer.lex(input);
        assertThat(output, is(equalTo(expected)));
    }

    @Override
    public void openBrace(int line, int pos) {
        add("OBc");
    }

    @Override
    public void closedBrace(int line, int pos) {
        add("CBc");
    }

    @Override
    public void openBracket(int line, int pos) {
        add("OBt");
    }

    @Override
    public void closedBracket(int line, int pos) {
        add("CBt");
    }

    @Override
    public void name(String name, int line, int pos) {
        add("_" + name + "_");
    }

    @Override
    public void string(String str, int line, int pos) {
        add("#" + str + "#");
    }

    @Override
    public void regexp(String matcher, String substitution, int line, int pos) {
        add("REGEXP(" + matcher + " :=>: " + substitution + ")");
    }

    @Override
    public void error(int line, int pos) {
        add("Error{" + line + "," + pos + "}");
    }

    @Nested
    class SingleCharTokens {
        @Test
        @DisplayName("Should parse open brace")
        void testOpenBrace() {
            assertLexed("{", "OBc");
        }

        @Test
        @DisplayName("Should parse closed brace")
        void testClosedBrace() {
            assertLexed("}", "CBc");
        }

        @Test
        @DisplayName("Should parse open bracket")
        void testOpenBracket() {
            assertLexed("[", "OBt");
        }

        @Test
        @DisplayName("Should parse closed bracket")
        void testClosedBracket() {
            assertLexed("]", "CBt");
        }

        @Test
        @DisplayName("Should parse multiple tokens")
        void multipleTokens() {
            assertLexed("[{}]", "OBt OBc CBc CBt");
        }

        @Test
        @DisplayName("Should parse multiple tokens on multiple lines")
        void multipleTokensMultiline() {
            assertLexed("\t[{\n} ]", "OBt OBc CBc CBt");
        }
    }

    @Nested
    class Names {
        @Test
        @DisplayName("Should parse simple name")
        void parseSimpleName() {
            assertLexed("hello", "_hello_");
        }

        @Test
        @DisplayName("Should parse a name starting with underscore")
        void parseNameStartingWithUnderscore() {
            assertLexed("_hello", "__hello_");
        }

        @Test
        @DisplayName("Should parse a name containing numbers")
        void parseNameWithNumbers() {
            assertLexed("hello1", "_hello1_");
        }

        @Test
        @DisplayName("Should parse two names in one line")
        void parseTwoNames_OneLine() {
            assertLexed("hello world", "_hello_ _world_");
        }

        @Test
        @DisplayName("Should parse two names in two lines")
        void parseTwoNames_TwoLines() {
            assertLexed("hello\nworld", "_hello_ _world_");
        }

        @Test
        @DisplayName("Open brackets end a name")
        void openBracketEndNames() {
            assertLexed("abc[def", "_abc_ OBt _def_");
        }

        @Test
        @DisplayName("Closed brackets end a name")
        void closedBracketEndNames() {
            assertLexed("abc]def", "_abc_ CBt _def_");
        }

        @Test
        @DisplayName("Open brace end a name")
        void openBraceEndNames() {
            assertLexed("abc{def", "_abc_ OBc _def_");
        }

        @Test
        @DisplayName("Closed brace end a name")
        void closedBraceEndNames() {
            assertLexed("abc}def", "_abc_ CBc _def_");
        }
    }

    @Nested
    class Strings {
        @Test
        @DisplayName("Should parse a simple string that starts as a name")
        void parseSimpleString_StartsAsName() {
            assertLexed("hello!", "#hello!#");
        }

        @Test
        @DisplayName("Should parse a simple string that starts not as a name, but as a simple string")
        void parseSimpleString_StartsAsString() {
            assertLexed("¡Hola!", "#¡Hola!#");
        }

        @Test
        @DisplayName("Should parse a simple string with double quotation in the middle")
        void parseSimpleString_DoubleQuotationInTheMiddle() {
            assertLexed("Hello\"world", "#Hello\"world#");
        }

        @Test
        @DisplayName("Should parse two simple strings from one line")
        void parseTwoSimpleStrings_OneLine() {
            assertLexed("hello! world?", "#hello!# #world?#");
        }

        @Test
        @DisplayName("Should parse two simple strings from multiple lines")
        void parseTwoSimpleStrings_TwoLines() {
            assertLexed("hello!\n\t\n\n\t\t world?", "#hello!# #world?#");
        }

        @Test
        @DisplayName("Should parse a simple string that starts with a number")
        void parseSimpleString_StartingWithNumber() {
            assertLexed("123", "#123#");
        }

        @Test
        @DisplayName("Should parse a string")
        void parseString() {
            assertLexed("\"Hello!\"", "#Hello!#");
        }

        @Test
        @DisplayName("Should parse a string that contains a white space")
        void parseString_WithWhitespace() {
            assertLexed("\"Hello World!\"", "#Hello World!#");
        }

        @Test
        @DisplayName("Should parse a string that contains a line break")
        void parseString_WithLineBreak() {
            assertLexed("\"Hello\nWorld!\"", "#Hello\nWorld!#");
        }

        @Test
        @DisplayName("Should parse two strings on one line")
        void parseTwoStrings_OneLine() {
            assertLexed("\"Hello world!\" \"Here I am!\"",  "#Hello world!# #Here I am!#");
        }

        @Test
        @DisplayName("Should parse two strings on multiple lines")
        void parseTwoStrings_MultipleLines() {
            assertLexed("\"Hello world!\"\n\"Here I am!\"",  "#Hello world!# #Here I am!#");
        }

        @Test
        @DisplayName("Should parse a string with escaped double quotation mark within")
        void parseString_escapeQuotationMark() {
            assertLexed("\"abc\\\"def\"", "#abc\"def#");
        }

        @Test
        @DisplayName("Should parse a string with escaped new line within")
        void parseString_escapeNewLine() {
            assertLexed("\"abc\\ndef\"", "#abc\ndef#");
        }

        @Test
        @DisplayName("Should parse a string with escaped tab within")
        void parseString_escapeTab() {
            assertLexed("\"abc\\tdef\"", "#abc\tdef#");
        }

        @Test
        @DisplayName("Should parse a string with escaped backslash within")
        void parseString_escapeBackslash() {
            assertLexed("\"abc\\\\def\"", "#abc\\def#");
        }

        @Test
        @DisplayName("Should parse a string with escaped tab within")
        void parseString_multipleEscapedCharacters() {
            assertLexed("\"a\\\"b\\nc\\td\\\\e f\"", "#a\"b\nc\td\\e f#");
        }

        @Test
        @DisplayName("Should parse multiple strings with escaped characters")
        void parseString_MultipleStringsWithEscapedCharacters() {
            assertLexed("\"a\\nb\" \"c\\nd\"", "#a\nb# #c\nd#");
        }

        @Test
        @DisplayName("Open brackets end a simple string")
        void openBracketEndSimpleStrings() {
            assertLexed("!abc[def", "#!abc# OBt _def_");
        }

        @Test
        @DisplayName("Closed brackets end a simple string")
        void closedBracketEndSimpleStrings() {
            assertLexed("!abc]def", "#!abc# CBt _def_");
        }

        @Test
        @DisplayName("Open brace end a simple string")
        void openBraceEndSimpleStrings() {
            assertLexed("!abc{def", "#!abc# OBc _def_");
        }

        @Test
        @DisplayName("Closed brace end a simple string")
        void closedBraceEndSimpleStrings() {
            assertLexed("!abc}def", "#!abc# CBc _def_");
        }

        @Test
        @DisplayName("Should parse brackets separately from a simple string/name if there are whitespaces between")
        void simpleStringAndBrackets_Whitespaces() {
            assertLexed("abc [ def ]", "_abc_ OBt _def_ CBt");
        }
    }

    @Nested
    class Regexp {
        @Test
        @DisplayName("Should parse regexp")
        void parseRegexp() {
            assertLexed("\\{-(c)s => -(c)id}", "REGEXP(-(c)s :=>: -(c)id)");
        }
    }

    @Nested
    class Comments {
        @Test
        @DisplayName("Should ignore comment till the end of an input")
        void commentsAreIgnored_EndOfFile() {
            assertLexed("abc // my comment", "_abc_");
        }

        @Test
        @DisplayName("Should ignore comment till the end of a line")
        void commentsAreIgnored_EndOfLine() {
            assertLexed("abc // my comment\ndef", "_abc_ _def_");
        }

        @Test
        @DisplayName("A name ends where a comment starts")
        void commentEndsName() {
            assertLexed("abc// my comment", "_abc_");
        }

        @Test
        @DisplayName("A simple string ends where a comment starts")
        void commentEndsSimpleString() {
            assertLexed("¡Hola!// my comment", "#¡Hola!#");
        }

        @Test
        @DisplayName("Should parse a string with double slash inside")
        void doubleSlashDoesNotEndString() {
            assertLexed("\"Hello // Hello\"", "#Hello // Hello#");
        }

        @Test
        @DisplayName("A single slash turns a name into a simple string")
        void singleSlashInSimpleString() {
            assertLexed("abc/def", "#abc/def#");
        }

        @Test
        @DisplayName("A single slash in a simple string is allowed")
        void singleSlashInSimpleString2() {
            assertLexed("¡Hola/Mundo!", "#¡Hola/Mundo!#");
        }
    }

    @Nested
    class Errors {
        @Test
        @DisplayName("An error should occur if the file ends before the string is closed")
        void stringShouldBeClosedBeforeEndOfFile() {
            assertLexed("name\n\"abc", "_name_ Error{2,4}");
        }

        @Test
        @DisplayName("An error should occur if the file ends before the regexp is closed")
        void regexpShouldBeClosedBeforeEndOfFile() {
             assertLexed("\\{abc", "Error{1,5}");
        }
    }

    @Nested
    class AcceptanceTests {
        @Test
        @DisplayName("Simple language description config")
        void languageDescr() {
            assertLexed("" +
                    "PartsOfSpeech [\n" +
                            "    [NOUN n]\n" +
                            "    [VERB v]\n" +
                            "    [ADJECTIVE adj]\n" +
                            "]\n" +
                            "\n" +
                            "English {\n" +
                            "    name English\n" +
                            "    code En\n" +
                            "    PartsOfSpeech [\n" +
                            "        [NOUN n noun]\n" +
                            "        [VERB v verb]\n" +
                            "        [ADJECTIVE adj adjective]\n" +
                            "    ]\n" +
                            "}\n" +
                            "\n" +
                            "Russian {\n" +
                            "    name Русский\n" +
                            "    code Ru\n" +
                            "    PartsOfSpeech [\n" +
                            "        [NOUN сущ. \"имя существительное\"]\n" +
                            "        [VERB гл. глагол]\n" +
                            "        [ADJECTIVE прил. \"имя прилагательное\"]\n" +
                            "    ]\n" +
                            "}",
                    "_PartsOfSpeech_" +
                            " OBt OBt _NOUN_ _n_ CBt" +
                            " OBt _VERB_ _v_ CBt" +
                            " OBt _ADJECTIVE_ _adj_ CBt CBt" +
                            " _English_ OBc" +
                            " _name_ _English_ _code_ _En_" +
                            " _PartsOfSpeech_ OBt" +
                            " OBt _NOUN_ _n_ _noun_ CBt" +
                            " OBt _VERB_ _v_ _verb_ CBt" +
                            " OBt _ADJECTIVE_ _adj_ _adjective_ CBt CBt CBc" +
                            " _Russian_ OBc" +
                            " _name_ _Русский_" +
                            " _code_ _Ru_" +
                            " _PartsOfSpeech_ OBt" +
                            " OBt _NOUN_ #сущ.# #имя существительное# CBt" +
                            " OBt _VERB_ #гл.# _глагол_ CBt" +
                            " OBt _ADJECTIVE_ #прил.# #имя прилагательное# CBt CBt CBc");
        }
    }
}