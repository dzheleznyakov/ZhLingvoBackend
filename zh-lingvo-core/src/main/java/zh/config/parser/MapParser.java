package zh.config.parser;

import zh.config.parser.SyntaxError.Type;
import zh.config.parser.syntax.ConfigValue;
import zh.config.parser.syntax.MapValue;
import zh.config.parser.syntax.StringValue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MapParser extends ValueParser {
    private final MapValue.Builder mapBuilder = MapValue.builder();
    private String parsedName = null;

    public MapParser(
            ArrayList<Parser> registry,
            List<SyntaxError> errors,
            Consumer<ConfigValue> exitAction
    ) {
        super(registry, errors, exitAction);
    }

    @Override
    public void openBrace(int line, int pos) {
        new MapParser(registry, errors, configValue -> addEntry(configValue, line, pos));
    }

    @Override
    public void closedBrace(int line, int pos) {
        exitAciton.accept(mapBuilder.build());
        registry.remove(this);
    }

    @Override
    public void openBracket(int line, int pos) {
        new ListParser(registry, errors, configValue -> addEntry(configValue, line, pos));
    }

    @Override
    public void closedBracket(int line, int pos) {
        errors.add(new SyntaxError(
                Type.MISMATCHING_BRACKET,
                "Closed bracket in map",
                line,
                pos
        ));
    }

    @Override
    public void name(String name, int line, int pos) {
        if (parsedName == null)
            parsedName = name;
        else
            addEntry(new StringValue(name), line, pos);
    }

    @Override
    public void string(String str, int line, int pos) {
        addEntry(new StringValue(str), line, pos);
    }

    private void addEntry(ConfigValue configValue, int line, int pos) {
        if (parsedName == null) {
            errors.add(new SyntaxError(
                    Type.MAP_NAME_MISSING,
                    "Cannot add entry with name missing",
                    line,
                    pos
            ));
        } else {
            mapBuilder.put(parsedName, configValue);
            parsedName = null;
        }
    }

    @Override
    public void error(int line, int pos) {
        errors.add(new SyntaxError(Type.SYNTAX, "", line, pos));
    }
}
