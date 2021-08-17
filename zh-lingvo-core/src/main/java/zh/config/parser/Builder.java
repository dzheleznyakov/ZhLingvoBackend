package zh.config.parser;

import zh.config.parser.syntax.ConfigValue;

import java.util.List;

public interface Builder<E> {
    E build(ConfigValue value, List<SyntaxError> errors);
}
