package zh.lingvo.config.parser;

import zh.lingvo.config.parser.syntax.ConfigValue;

import java.util.List;

public interface Builder<E> {
    E build(ConfigValue value, List<SyntaxError> errors);
}
