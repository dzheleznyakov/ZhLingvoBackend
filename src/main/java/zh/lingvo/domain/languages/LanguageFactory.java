package zh.lingvo.domain.languages;

import com.google.common.base.Preconditions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class LanguageFactory {
    private LanguageFactory() throws IllegalAccessException {
        throw new IllegalAccessException("This is the static factory class");
    }

    public static <L> Language getInstance(Class<L> lClass) {
        Preconditions.checkArgument(Language.class.isAssignableFrom(lClass),
                "Class [%s] should extend [%s]", lClass, Language.class);

        Method instanceGetter;
        try {
            instanceGetter = lClass.getDeclaredMethod("getInstance");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("Class [%s] should have static \"getInstance()\" method", lClass), e);
        }
        Preconditions.checkArgument(Modifier.isStatic(instanceGetter.getModifiers()),
                "Method \"getInstance()\" of class [%s] should be static", lClass);

        Object instance;
        try {
            instance = instanceGetter.invoke(lClass);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("Exception when invoking \"getInstance()\" method of [%s]", lClass), e);
        }

        return (Language) instance;
    }
}
