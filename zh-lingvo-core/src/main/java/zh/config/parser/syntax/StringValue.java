package zh.config.parser.syntax;

public class StringValue implements ConfigValue {
    private final String value;

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public String getString() {
        return value;
    }

    @Override
    public int getInt() {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return ConfigValue.super.getInt();
        }
    }

    @Override
    public double getDouble() {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return ConfigValue.super.getDouble();
        }
    }

    @Override
    public boolean getBoolean() {
        return "true".equals(value);
    }
}
