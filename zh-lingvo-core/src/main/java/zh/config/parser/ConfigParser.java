package zh.config.parser;

import zh.config.parser.syntax.ConfigValue;

import java.util.ArrayList;
import java.util.List;

public class ConfigParser implements Parser {
    private final ArrayList<Parser> registry = new ArrayList<>();
    private final List<SyntaxError> errors = new ArrayList<>();
    private ConfigValue parsedConfig = null;

    public ConfigParser() {
        new MapParser(registry, errors, cf -> parsedConfig = cf);
    }

    private Parser getCurrentParser() {
        return registry.get(registry.size() - 1);
    }

    @Override
    public void openBrace(int line, int pos) {
        getCurrentParser().openBrace(line, pos);
    }

    @Override
    public void closedBrace(int line, int pos) {
        getCurrentParser().closedBrace(line, pos);
    }

    @Override
    public void openBracket(int line, int pos) {
        getCurrentParser().openBracket(line, pos);
    }

    @Override
    public void closedBracket(int line, int pos) {
        getCurrentParser().closedBracket(line, pos);
    }

    @Override
    public void name(String name, int line, int pos) {
        getCurrentParser().name(name, line, pos);
    }

    @Override
    public void string(String str, int line, int pos) {
        getCurrentParser().string(str, line, pos);
    }

    @Override
    public void regexp(String matcher, String substitution, int line, int pos) {
        List<SyntaxError> regexpErrors = new ArrayList<>();
        if (matcher == null)
            regexpErrors.add(new SyntaxError(
                    SyntaxError.Type.REGEXP_MISSING_MATCHER,
                    "RegExp matcher cannot be null",
                    line,
                    pos
            ));
        if (substitution == null)
            regexpErrors.add(new SyntaxError(
                    SyntaxError.Type.REGEXP_MISSING_SUBSTITUTION,
                    "RegExp substitution cannot be null",
                    line,
                    pos
            ));
        errors.addAll(regexpErrors);
        if (regexpErrors.isEmpty())
            getCurrentParser().regexp(matcher, substitution, line, pos);
    }

    @Override
    public void error(int line, int pos) {
        getCurrentParser().error(line, pos);
    }

    @Override
    public void done(int line, int pos) {
        getCurrentParser().closedBrace(line + 1, 0);
    }

    public <E> E build(Builder<E> builder) {
        return builder.build(parsedConfig, errors);
    }
}
