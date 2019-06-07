package zh.lingvo.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.typesafe.config.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ConfigReader {
    private static final String DEFAULT_CONFIG_PATH = "lingvo";
    private static final ConfigReader INSTANCE = new ConfigReader();

    public static final String DEFAULT_PATH = "value";

    private Config config;

    private ConfigReader() {
        this(DEFAULT_CONFIG_PATH);
    }

    ConfigReader(String configPath) {
        initialiseConfig(configPath);
    }

    private ConfigReader(ConfigValue value) {
        config = value.valueType() == ConfigValueType.OBJECT
                ? ConfigFactory.parseMap(((ConfigObject) value).unwrapped())
                : value.atPath(DEFAULT_PATH);
    }

    private void initialiseConfig(String configPath) {
        config = ConfigFactory.empty()
                .withFallback(ConfigFactory.parseResourcesAnySyntax(configPath));
    }

    public boolean hasPath(String path) {
        return config.hasPath(path);
    }

    public int getAsInt(String path) {
        return config.getInt(path);
    }

    public short getAsShort(String path) {
        return (short) config.getInt(path);
    }

    public byte getAsByte(String path) {
        return (byte) config.getInt(path);
    }

    public long getAsLong(String path) {
        return config.getLong(path);
    }

    public double getAsDouble(String path) {
        return config.getDouble(path);
    }

    public float getAsFloat(String path) {
        return (float) config.getDouble(path);
    }

    public boolean getAsBoolean(String path) {
        return config.getBoolean(path);
    }

    public String getAsString(String path) {
        return config.getString(path);
    }

    public <E extends Enum<E>> E getAsEnum(String path, Class<E> eClass) {
        return config.getEnum(eClass, path);
    }

    public <O> O getAsObject(String path, Function<ConfigReader, O> mapper) {
        ConfigObject configObject = this.config.getObject(path);
        ConfigReader config = new ConfigReader(configObject);
        return mapper.apply(config);
    }

    public List<Integer> getAsIntList(String path) {
        return config.getIntList(path);
    }

    public List<Long> getAsLongList(String path) {
        return config.getLongList(path);
    }

    public List<Double> getAsDoubleList(String path) {
        return config.getDoubleList(path);
    }

    public List<String> getAsStringList(String path) {
        return config.getStringList(path);
    }

    public List<Boolean> getAsBooleanList(String path) {
        return config.getBooleanList(path);
    }

    public <E extends Enum<E>> List<E> getAsEnumList(String path, Class<E> eClass) {
        return config.getEnumList(eClass, path);
    }

    public <E> List<E> getAsList(String path, Function<ConfigReader, E> mapper) {
        return getAsList(path, mapper, (a, b) -> 1);
    }

    public <E> List<E> getAsList(String path, Function<ConfigReader, E> mapper, Comparator<E> comparator) {
        return config.getList(path).stream()
                .map(ConfigReader::new)
                .map(mapper)
                .sorted(comparator)
                .collect(ImmutableList.toImmutableList());
    }

    public <K, E> Map<K, E> getAsMap(String path, Function<ConfigReader, K> keyMapper, Function<ConfigReader, E> valueMapper) {
        return config.getList(path).stream()
                .map(ConfigReader::new)
                .collect(ImmutableMap.toImmutableMap(keyMapper, valueMapper));
    }

    public static ConfigReader get() {
        return INSTANCE;
    }
}
