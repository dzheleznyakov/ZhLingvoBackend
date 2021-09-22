package zh.lingvo.core;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.ClassPath;
import lombok.SneakyThrows;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class LanguageDescriptorManager {
    private static final String DESCRIPTORS_SUBPACKAGE = "descriptors";
    private final Map<String, LanguageDescriptor> descriptorsByCodes;

    @SuppressWarnings("UnstableApiUsage")
    public LanguageDescriptorManager() throws IOException {
        ClassPath classPath = ClassPath.from(getClass().getClassLoader());
        String packageName = getClass().getPackageName() + "." + DESCRIPTORS_SUBPACKAGE;
        descriptorsByCodes = classPath.getTopLevelClasses(packageName).stream()
                .map(ClassPath.ClassInfo::load)
                .filter(LanguageDescriptor.class::isAssignableFrom)
                .map(this::getConsturctor)
                .map(this::newLanguageDescriptor)
                .collect(ImmutableMap.toImmutableMap(LanguageDescriptor::getLanguageCode, Function.identity()));
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private Constructor<? extends LanguageDescriptor> getConsturctor(Class<?> cl) {
        return (Constructor<? extends LanguageDescriptor>) cl.getConstructor();
    }

    @SneakyThrows
    private LanguageDescriptor newLanguageDescriptor(Constructor<? extends LanguageDescriptor> constructor) {
        return constructor.newInstance();
    }

    public List<String> getLanguageCodes() throws IOException {
        return descriptorsByCodes.values().stream()
                .map(LanguageDescriptor::getLanguageCode)
                .collect(ImmutableList.toImmutableList());
    }

    public LanguageDescriptor getLanguageDescriptor(String code) {
        return descriptorsByCodes.getOrDefault(code, LanguageDescriptor.NULL);
    }
}
