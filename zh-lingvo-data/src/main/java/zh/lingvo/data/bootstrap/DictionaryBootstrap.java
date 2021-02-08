package zh.lingvo.data.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import zh.lingvo.data.domain.Language;
import zh.lingvo.data.services.LanguageService;

import java.util.Optional;

@Component
@Slf4j
public class DictionaryBootstrap implements CommandLineRunner {
    private final LanguageService languageService;

    public DictionaryBootstrap(LanguageService languageService) {
        this.languageService = languageService;
    }

    @Override
    public void run(String... args) {
        loadLanguages();
    }


    private void loadLanguages() {
        log.info("Loading languages...");
        loadLanguage("English", "En");
        loadLanguage("Spanish", "Es");
        loadLanguage("Russian", "Ru");
        log.info("Languages loaded");
    }

    private void loadLanguage(String name, String code) {
        Optional<Language> languageOptional = languageService.findByTwoLetterCode(code);
        if (languageOptional.isEmpty()) {
            Language language = new Language();
            language.setName(name);
            language.setTwoLetterCode(code);
            log.info("Loading [{}] language", name);
            languageService.save(language);
        }
    }
}
