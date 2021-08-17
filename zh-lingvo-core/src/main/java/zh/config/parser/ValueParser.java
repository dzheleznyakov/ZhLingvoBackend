package zh.config.parser;

import zh.config.parser.syntax.ConfigValue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class ValueParser implements Parser {
    protected final ArrayList<Parser> registry;
    protected final List<SyntaxError> errors;
    protected final Consumer<ConfigValue> exitAciton;

    protected ValueParser(ArrayList<Parser> registry, List<SyntaxError> errors, Consumer<ConfigValue> exitAction) {
        this.registry = registry;
        this.errors = errors;
        this.exitAciton = exitAction;

        registry.add(this);
    }
}
