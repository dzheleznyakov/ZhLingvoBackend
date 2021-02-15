package zh.lingvo.data.services.impl;

import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Service;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.repositories.LanguageRepository;
import zh.lingvo.data.services.LanguageService;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public List<Language> findAll() {
        return ImmutableList.copyOf(languageRepository.findAll());
    }

    @Override
    public Optional<Language> findByTwoLetterCode(String code) {
        return languageRepository.findByTwoLetterCode(code);
    }

    @Override
    public Language save(Language language) {
        return languageRepository.save(language);
    }
}
