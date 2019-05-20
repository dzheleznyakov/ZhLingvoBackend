package zh.lingvo.rest.json;

import com.google.gson.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import zh.lingvo.rest.entities.RestEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class JsonFactory {
    private static final Gson GSON = new Gson();

    private JsonFactory() {}

    public static String toJson(int num) {
        JsonElement jsonElement = toJsonElement(num);
        return GSON.toJson(jsonElement);
    }

    public static String toJson(double num) {
        JsonElement jsonElement = toJsonElement(num);
        return GSON.toJson(jsonElement);
    }

    public static String toJson(float num) {
        JsonElement jsonElement = toJsonElement(num);
        return GSON.toJson(jsonElement);
    }

    public static String toJson(short num) {
        JsonElement jsonElement = toJsonElement(num);
        return GSON.toJson(jsonElement);
    }

    public static String toJson(byte num) {
        JsonElement jsonElement = toJsonElement(num);
        return GSON.toJson(jsonElement);
    }

    public static String toJson(long num) {
        JsonElement jsonElement = toJsonElement(num);
        return GSON.toJson(jsonElement);
    }

    public static String toJson(Number num) {
        JsonElement jsonElement = toJsonElement(num);
        return GSON.toJson(jsonElement);
    }

    public static String toJson(boolean value) {
        JsonElement jsonElement = toJsonElement(value);
        return GSON.toJson(jsonElement);
    }

    public static String toJson(char ch) {
        JsonElement jsonElement = toJsonElement(ch);
        return GSON.toJson(jsonElement);
    }

    public static String toJson(String s) {
        JsonElement jsonElement = s == null ? JsonNull.INSTANCE : toJsonElement(s);
        return GSON.toJson(jsonElement);
    }

    public static String toJson(int[] array) {
        JsonArray jsonArray = new JsonArray(array.length);
        for (int i : array)
            jsonArray.add(i);
        return GSON.toJson(jsonArray);
    }

    public static String toJson(double[] array) {
        JsonArray jsonArray = new JsonArray(array.length);
        for (double d : array)
            jsonArray.add(d);
        return GSON.toJson(jsonArray);
    }

    public static String toJson(float[] array) {
        JsonArray jsonArray = new JsonArray(array.length);
        for (float f : array)
            jsonArray.add(f);
        return GSON.toJson(jsonArray);
    }

    public static String toJson(short[] array) {
        JsonArray jsonArray = new JsonArray(array.length);
        for (short s : array)
            jsonArray.add(s);
        return GSON.toJson(jsonArray);
    }

    public static String toJson(byte[] array) {
        JsonArray jsonArray = new JsonArray(array.length);
        for (byte b : array)
            jsonArray.add(b);
        return GSON.toJson(jsonArray);
    }

    public static String toJson(long[] array) {
        JsonArray jsonArray = new JsonArray(array.length);
        for (long l : array)
            jsonArray.add(l);
        return GSON.toJson(jsonArray);
    }

    public static <N extends Number> String toJson(N[] array) {
        JsonArray jsonArray = new JsonArray(array.length);
        for (N n : array)
            jsonArray.add(n);
        return GSON.toJson(jsonArray);
    }

    public static String toJson(boolean[] array) {
        JsonArray jsonArray = new JsonArray(array.length);
        for (boolean b : array)
            jsonArray.add(b);
        return GSON.toJson(jsonArray);
    }

    public static String toJson(Boolean[] array) {
        JsonArray jsonArray = new JsonArray(array.length);
        for (Boolean b : array)
            jsonArray.add(b);
        return GSON.toJson(jsonArray);
    }

    public static String toJson(char[] array) {
        JsonArray jsonArray = new JsonArray(array.length);
        for (char c : array)
            jsonArray.add(c);
        return GSON.toJson(jsonArray);
    }

    public static String toJson(Character[] array) {
        JsonArray jsonArray = new JsonArray(array.length);
        for (Character c : array)
            jsonArray.add(c);
        return GSON.toJson(jsonArray);
    }

    public static String toJson(String[] array) {
        JsonArray jsonArray = new JsonArray(array.length);
        for (String s : array)
            jsonArray.add(s);
        return GSON.toJson(jsonArray);
    }

    public static <E extends RestEntity> String toJson(E[] array) {
        JsonArray jsonArray = new JsonArray(array.length);
        for (E e : array)
            jsonArray.add(toJsonElement(e));
        return GSON.toJson(jsonArray);
    }

    public static <E extends RestEntity> String toJson(E obj) {
        JsonElement jsonElement = obj == null ? JsonNull.INSTANCE : toJsonElement(obj);
        return GSON.toJson(jsonElement);
    }

    public static <E> String toJson(Iterable<E> iterable) {
        if (iterable == null)
            return GSON.toJson(JsonNull.INSTANCE);
        JsonArray jsonArray = new JsonArray();
        for (E e : iterable)
            jsonArray.add(convertToJsonElement(e));
        return GSON.toJson(jsonArray);
    }

    public static <E> String toJson(Iterator<E> iterator) {
        if (iterator == null)
            return GSON.toJson(JsonNull.INSTANCE);
        JsonArray jsonArray = new JsonArray();
        while (iterator.hasNext())
            jsonArray.add(convertToJsonElement(iterator.next()));
        return GSON.toJson(jsonArray);
    }

    public static <K, V> String toJson(Map<K, V> map) {
        if (map == null)
            return GSON.toJson(JsonNull.INSTANCE);
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<K, V> kvEntry : map.entrySet()) {
            K key = kvEntry.getKey();
            V value = kvEntry.getValue();
            String property = GSON.toJson(convertToJsonElement(key));
            jsonObject.add(property, convertToJsonElement(value));
        }
        return GSON.toJson(jsonObject);
    }

    public static String toJson(Object obj) {
        if (obj == null)
            return GSON.toJson(JsonNull.INSTANCE);
        throw new JsonConversionException(String.format("Can't convert an object of type [%s]", obj.getClass()), null);
    }

    @NotNull
    @Contract("_ -> new")
    private static JsonElement toJsonElement(Number number) {
        return new JsonPrimitive(number);
    }

    @NotNull
    @Contract("_ -> new")
    private static JsonElement toJsonElement(boolean value) {
        return new JsonPrimitive(value);
    }

    @NotNull
    @Contract("_ -> new")
    private static JsonElement toJsonElement(char ch) {
        return new JsonPrimitive(ch);
    }

    @NotNull
    @Contract("_ -> new")
    private static JsonElement toJsonElement(String s) {
        return new JsonPrimitive(s);
    }

    @NotNull
    @Contract("_ -> new")
    private static <E extends RestEntity> JsonElement toJsonElement(E obj) {
        JsonObject jsonObject = new JsonObject();
        addPropertiesToJson(obj, jsonObject);
        return jsonObject;
    }

    private static <E extends RestEntity> void addPropertiesToJson(E obj, JsonObject json) {
        Arrays.stream(obj.getClass().getDeclaredFields())
                .filter(JsonFactory::isPersistable)
                .forEach(field -> addPropertyToJson(obj, field, json));
    }

    private static boolean isPersistable(Field field) {
        return field.getAnnotationsByType(Persistable.class).length != 0;
    }

    private static <E extends RestEntity> void addPropertyToJson(E obj , Field field, JsonObject json) {
        String getterName = convertFieldToGetterName(field);
        Method getter = getMethodByName(obj.getClass(), getterName);
        Object value = invokeGetter(getter, obj);

        JsonElement jsonElement = convertToJsonElement(value);
        json.add(field.getName(), jsonElement);
    }

    private static JsonElement convertToJsonElement(Object value) {
        if (value == null)
            return JsonNull.INSTANCE;
        if (value instanceof Number)
            return toJsonElement((Number) value);
        if (value instanceof Character)
            return toJsonElement((Character) value);
        if (value instanceof String)
            return toJsonElement((String) value);
        if (value instanceof Boolean)
            return toJsonElement((Boolean) value);
        if (value instanceof RestEntity)
            return toJsonElement((RestEntity) value);
        throw new JsonConversionException(String.format("Cannot convert object [%s] to JSON", value), null);
    }

    private static String convertFieldToGetterName(Field field) {
        String verb = isBooleanClass(field.getType()) ? "is" : "get";
        String name = field.getName();
        char firstChar = name.charAt(0);
        return verb + Character.toUpperCase(firstChar) + name.substring(1);
    }

    private static boolean isBooleanClass(Class<?> cl) {
        return cl == boolean.class || cl == Boolean.class;
    }

    private static Method getMethodByName(Class<?> clazz, String name) {
        try {
            return clazz.getMethod(name);
        } catch (NoSuchMethodException e) {
            throw new JsonConversionException(
                    String.format("Method [%s] for class [%s] not found", name, clazz.getCanonicalName()), e);
        }
    }

    private static Object invokeGetter(Method method, Object object) {
        try {
            return method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new JsonConversionException(
                    String.format("Error when invoking [%s] for class [%s]", method.getName(), object.getClass().getCanonicalName()), e);
        }
    }
}
