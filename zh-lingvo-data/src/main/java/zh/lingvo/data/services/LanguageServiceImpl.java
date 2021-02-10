package zh.lingvo.data.services;

import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Service;
import zh.lingvo.data.domain.Language;
import zh.lingvo.data.repositories.LanguageRepository;

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
