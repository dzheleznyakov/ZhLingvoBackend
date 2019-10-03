package zh.lingvo.caches;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import zh.lingvo.domain.languages.Language;
import zh.lingvo.domain.languages.LanguageFactory;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@Service
@Scope(SCOPE_SINGLETON)
public class LanguagesCache {
    private List<Language> languages;

    LanguagesCache() {
        try {
            initialiseLanguages();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load languages", e);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private void initialiseLanguages() throws IOException {
        ClassPath classPath = ClassPath.from(getClass().getClassLoader());
        ImmutableSet<ClassPath.ClassInfo> classes = classPath.getTopLevelClassesRecursive("zh.lingvo.domain.languages");
        languages = classes.stream()
                .map(ClassPath.ClassInfo::load)
                .filter(Language.class::isAssignableFrom)
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .map(LanguageFactory::getInstance)
                .collect(ImmutableList.toImmutableList());
    }

    public List<Language> get() {
        return ImmutableList.copyOf(languages);
    }

    public Language get(String code) {
        return languages.stream()
                .filter(language -> language.getCode().equals(code))
                .findAny()
                .orElse(null);
    }

    public boolean isRegistered(String languageCode) {
        return languages.stream()
                .map(Language::getCode)
                .map(languageCode::equals)
                .filter(b -> b)
                .findAny()
                .orElse(false);
    }
}
