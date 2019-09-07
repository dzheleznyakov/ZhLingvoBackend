package zh.lingvo.caches;

import com.google.common.collect.ImmutableList;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import zh.lingvo.util.ConfigReader;
import zh.lingvo.domain.Language;

import java.util.Comparator;
import java.util.List;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@Service
@Scope(SCOPE_SINGLETON)
public class LanguagesCache {
    private static final ConfigReader config = ConfigReader.get();
    private ImmutableList<Language> languages;

    LanguagesCache() {
        initialiseLanguages();
    }

    private void initialiseLanguages() {
        languages = ImmutableList.copyOf(
                config.getList("languages", Language::new, Comparator.comparing(Language::getCode)));
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
