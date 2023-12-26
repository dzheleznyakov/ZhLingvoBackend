package zh.lingvo.config.parser.syntax;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

public interface ConfigValue {
    ConfigValue NULL = new ConfigValue() {};

    default int getInt() {
        return 0;
    }

    default double getDouble() {
        return 0.0;
    }

    default boolean getBoolean() {
        return false;
    }

    default String getString() {
        return "";
    }

    default List<ConfigValue> getList() {
        return ImmutableList.of();
    }

    default Map<String, ConfigValue> getMap() {
        return ImmutableMap.of();
    }

    interface Builder<V extends ConfigValue> {
        V build();
    }
}
