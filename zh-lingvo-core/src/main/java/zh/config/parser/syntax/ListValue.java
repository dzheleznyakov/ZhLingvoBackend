package zh.config.parser.syntax;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class ListValue implements ConfigValue {
    private final List<ConfigValue> list;

    private ListValue(Builder builder) {
        list = builder.list.build();
    }

    @Override
    public List<ConfigValue> getList() {
        return ImmutableList.copyOf(list);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements ConfigValue.Builder<ListValue> {
        private final ImmutableList.Builder<ConfigValue> list = ImmutableList.builder();

        private Builder() {}

        public Builder add(ConfigValue value) {
            list.add(value);
            return this;
        }

        public ListValue build() {
            return new ListValue(this);
        }
    }
}
