package zh.config.parser;

import zh.config.parser.SyntaxError.Type;
import zh.config.parser.syntax.ConfigValue;
import zh.config.parser.syntax.ListValue;
import zh.config.parser.syntax.RegExpValue;
import zh.config.parser.syntax.StringValue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ListParser extends ValueParser {
    private final ListValue.Builder listBuilder = ListValue.builder();

    public ListParser(
            ArrayList<Parser> registry,
            List<SyntaxError> errors,
            Consumer<ConfigValue> exitAction
    ) {
        super(registry, errors, exitAction);
    }

    @Override
    public void openBrace(int line, int pos) {
        new MapParser(registry, errors, listBuilder::add);
    }

    @Override
    public void closedBrace(int line, int pos) {
        errors.add(new SyntaxError(
                Type.MISMATCHING_BRACE,
                "Closed brace in list.",
                line,
                pos
        ));
        closeWithValue(null);
    }

    @Override
    public void openBracket(int line, int pos) {
        new ListParser(registry, errors, listBuilder::add);
    }

    @Override
    public void closedBracket(int line, int pos) {
        closeWithValue(listBuilder.build());
    }

    @Override
    public void name(String name, int line, int pos) {
        listBuilder.add(new StringValue(name));
    }

    @Override
    public void string(String str, int line, int pos) {
        listBuilder.add(new StringValue(str));
    }

    @Override
    public void regexp(String matcher, String substitution, int line, int pos) {
        RegExpValue value = RegExpValue.builder()
                .setMatcher(matcher)
                .setSubstitution(substitution)
                .build();
        listBuilder.add(value);
    }

    @Override
    public void error(int line, int pos) {
        errors.add(new SyntaxError(Type.SYNTAX_ERROR, "", line, pos));
    }

    private void closeWithValue(ConfigValue value) {
        exitAction.accept(value);
        registry.remove(this);
    }
}
