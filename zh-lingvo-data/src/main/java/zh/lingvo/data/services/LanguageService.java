package zh.lingvo.data.services;

import zh.lingvo.data.domain.Language;

import java.util.List;
import java.util.Optional;

public interface LanguageService {
    List<Language> findAll();

    Optional<Language> findByTwoLetterCode(String code);

    Language save(Language language);
}
