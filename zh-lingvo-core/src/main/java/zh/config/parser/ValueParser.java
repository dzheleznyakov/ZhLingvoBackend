package zh.config.parser;

import zh.config.parser.syntax.ConfigValue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class ValueParser implements Parser {
    protected final ArrayList<Parser> registry;
    protected final List<SyntaxError> errors;
    protected final Consumer<ConfigValue> exitAction;

    protected ValueParser(ArrayList<Parser> registry, List<SyntaxError> errors, Consumer<ConfigValue> exitAction) {
        this.registry = registry;
        this.errors = errors;
        this.exitAction = exitAction;

        registry.add(this);
    }
}
