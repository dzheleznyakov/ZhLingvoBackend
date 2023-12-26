package zh.lingvo.config.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.config.parser.lexer.Lexer;
import zh.lingvo.config.parser.syntax.ConfigValue;
import zh.lingvo.config.parser.syntax.ListValue;
import zh.lingvo.config.parser.syntax.StringValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@DisplayName("Test ListParser")
class ListParserTest {
    private final List<SyntaxError> errors = new ArrayList<>();
    private final ArrayList<Parser> registry = new ArrayList<>();
    private ConfigValue builtValue = null;

    private final Parser parser = new ListParser(registry, errors, cf -> builtValue = cf);
    private final Lexer lexer = new Lexer(parser);

    @Test
    @DisplayName("Should parse the list containing string values")
    void stringValuesOnly() {
        lexer.lex("a\nb\n \"hello world\"]");

        String result = toString(builtValue);

        assertThat(result, is(equalTo("['a', 'b', 'hello world']")));
        assertThat(registry, is(empty()));
        assertThat(errors, is(empty()));
    }

    @Test
    @DisplayName("Should parse the list containing another list")
    void stringAndListValues() {
        lexer.lex("a \"b & c\"\n\t[");
        new Lexer(registry.get(registry.size() - 1)).lex("1 2\t3]");
        lexer.lex("\n\td]");

        String result = toString(builtValue);

        assertThat(result, is("['a', 'b & c', ['1', '2', '3'], 'd']"));
        assertThat(registry, is(empty()));
        assertThat(errors, is(empty()));
    }

    @Test
    @DisplayName("Should register an error when there is mismatching closed brace")
    void closedBraceCausesError() {
        lexer.lex("a b c}");

        assertThat(errors, hasSize(1));
        assertError(errors.get(0), SyntaxError.Type.MISMATCHING_BRACE, 1, 6);
    }

    @Test
    @DisplayName("Should register a syntax error if input has one")
    void syntaxErrorInList() {
        lexer.lex("a b \"cd]");

        assertThat(errors, hasSize(1));
        assertError(errors.get(0), SyntaxError.Type.SYNTAX_ERROR, 1, 8);
    }

    private void assertError(SyntaxError error, SyntaxError.Type type, int lineNumber, int pos) {
        assertThat(error.type, is(type));
        assertThat(error.lineNumber, is(lineNumber));
        assertThat(error.position, is(pos));
    }

    private String toString(ListValue value) {
        return value.getList().stream()
                .map(this::toString)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    private String toString(StringValue value) {
        return "'" + value.getString() + "'";
    }

    private String toString(ConfigValue value) {
        if (value instanceof ListValue)
            return toString((ListValue) value);
        if (value instanceof StringValue)
            return toString((StringValue) value);
        return "";
    }
}