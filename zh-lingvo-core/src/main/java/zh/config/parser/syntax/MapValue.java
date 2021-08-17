package zh.config.parser.syntax;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class MapValue implements ConfigValue {
    private final Map<String, ConfigValue> map;

    private MapValue(Builder builder) {
        map = builder.map.build();
    }

    @Override
    public Map<String, ConfigValue> getMap() {
        return ImmutableMap.copyOf(map);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements ConfigValue.Builder<MapValue> {
        private final ImmutableMap.Builder<String, ConfigValue> map = ImmutableMap.builder();

        private Builder() {}

        public Builder put(String key, ConfigValue value) {
            if (value != null)
                map.put(key, value);
            return this;
        }

        public MapValue build() {
            return new MapValue(this);
        }
    }
}
