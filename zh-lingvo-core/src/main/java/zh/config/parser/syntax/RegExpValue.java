package zh.config.parser.syntax;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

public class RegExpValue implements ConfigValue {
    private final String matcher;
    private final String substitution;

    private RegExpValue(Builder builder) {
        matcher = builder.values[0];
        substitution = builder.values[1];
    }

    @Override
    public String getString() {
        return matcher + " => " + substitution;
    }

    @Override
    public List<ConfigValue> getList() {
        return ImmutableList.of(
                new StringValue(matcher),
                new StringValue(substitution)
        );
    }

    @Override
    public Map<String, ConfigValue> getMap() {
        return ImmutableMap.of(
                "matcher", new StringValue(matcher),
                "substitution", new StringValue(substitution)
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements ConfigValue.Builder<RegExpValue> {
        private final String[] values = new String[2];

        private Builder() {}

        public Builder setMatcher(String matcher) {
            values[0] = matcher;
            return this;
        }

        public Builder setSubstitution(String substitution) {
            values[1] = substitution;
            return this;
        }

        @Override
        public RegExpValue build() {
            Preconditions.checkState(
                    values[0] != null,
                    "Cannot build RegExp value: matcher cannot be null");
            Preconditions.checkState(
                    values[1] != null,
                    "Cannot build RegExp value: substitution cannot be null");
            return new RegExpValue(this);
        }
    }
}
