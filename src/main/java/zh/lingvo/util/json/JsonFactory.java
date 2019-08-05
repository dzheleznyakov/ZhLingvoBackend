package zh.lingvo.util.json;

import com.google.gson.Gson;
import zh.lingvo.rest.entities.JsonEntity;

import java.util.Map;

public class JsonFactory {
    private static final Gson GSON = new Gson();

    private JsonFactory() {}

    public static String toJson(int num) {
        return GSON.toJson(num);
    }

    public static String toJson(double num) {
        return GSON.toJson(num);
    }

    public static String toJson(float num) {
        return GSON.toJson(num);
    }

    public static String toJson(short num) {
        return GSON.toJson(num);
    }

    public static String toJson(byte num) {
        return GSON.toJson(num);
    }

    public static String toJson(long num) {
        return GSON.toJson(num);
    }

    public static String toJson(Number num) {
        return GSON.toJson(num);
    }

    public static String toJson(boolean value) {
        return GSON.toJson(value);
    }

    public static String toJson(Boolean value) {
        return GSON.toJson(value);
    }

    public static String toJson(char ch) {
        return GSON.toJson(ch);
    }

    public static String toJson(Character ch) {
        return GSON.toJson(ch);
    }

    public static String toJson(String s) {
        return GSON.toJson(s);
    }

    public static String toJson(Enum<?> value) {
        return GSON.toJson(value);
    }

    public static <E extends JsonEntity> String toJson(E obj) {
        return GSON.toJson(obj);
    }

    public static String toJson(int[] array) {
        return GSON.toJson(array);
    }

    public static String toJson(double[] array) {
        return GSON.toJson(array);
    }

    public static String toJson(float[] array) {
        return GSON.toJson(array);
    }

    public static String toJson(short[] array) {
        return GSON.toJson(array);
    }

    public static String toJson(byte[] array) {
        return GSON.toJson(array);
    }

    public static String toJson(long[] array) {
        return GSON.toJson(array);
    }

    public static <N extends Number> String toJson(N[] array) {
        return GSON.toJson(array);
    }

    public static String toJson(boolean[] array) {
        return GSON.toJson(array);
    }

    public static String toJson(Boolean[] array) {
        return GSON.toJson(array);
    }

    public static String toJson(char[] array) {
        return GSON.toJson(array);
    }

    public static String toJson(Character[] array) {
        return GSON.toJson(array);
    }

    public static String toJson(String[] array) {
        return GSON.toJson(array);
    }

    public static <E extends Enum<E>> String toJson(E[] array) {
        return GSON.toJson(array);
    }

    public static <J extends JsonEntity> String toJson(J[] array) {
        return GSON.toJson(array);
    }

    public static <E> String toJson(Iterable<E> iterable) {
        return GSON.toJson(iterable);
    }

    public static <K, V> String toJson(Map<K, V> map) {
        return GSON.toJson(map);
    }

    public static String toJson(Object obj) {
        if (obj == null)
            return GSON.toJson(obj);
        throw new JsonConversionException(String.format("Can't convert an object of type [%s]", obj.getClass()), null);
    }
}
