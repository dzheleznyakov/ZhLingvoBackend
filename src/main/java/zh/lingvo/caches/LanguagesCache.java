package zh.lingvo.caches;

import com.google.common.collect.ImmutableList;
import zh.lingvo.util.ConfigReader;
import zh.lingvo.domain.Language;

import java.util.Comparator;
import java.util.List;

public class LanguagesCache {
    private static final ConfigReader config = ConfigReader.get();
    private static ImmutableList<Language> languages;

    public static List<Language> get() {
        if (languages == null)
            initialiseLanguages();
        return languages;
    }

    public static Language get(String code) {
        return get().stream()
                .filter(language -> language.getCode().equals(code))
                .findAny()
                .orElse(null);
    }

    public static boolean isRegistered(String languageCode) {
        return get().stream()
                .map(Language::getCode)
                .map(languageCode::equals)
                .filter(b -> b)
                .findAny()
                .orElse(false);
    }

    private static void initialiseLanguages() {
        languages = ImmutableList.copyOf(
                config.getList("languages", Language::new, Comparator.comparing(Language::getCode)));
    }
}
