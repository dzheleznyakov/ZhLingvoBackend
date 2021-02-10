package zh.lingvo.data.services;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zh.lingvo.data.domain.Language;
import zh.lingvo.data.repositories.LanguageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Test Language service")
@ExtendWith(MockitoExtension.class)
class LanguageServiceImplTest {
    @Mock
    private LanguageRepository languageRepository;

    private LanguageServiceImpl service;

    @BeforeEach
    void setUpService() {
        service = new LanguageServiceImpl(languageRepository);
    }

    @Test
    @DisplayName("Test find all languages")
    void findAll() {
        Language language1 = new Language();
        Language language2 = new Language();
        List<Language> languages = new ArrayList<>();
        languages.add(language1);
        languages.add(language2);

        when(languageRepository.findAll()).thenReturn(languages);

        List<Language> foundLanguages = service.findAll();

        assertThat(foundLanguages, is(equalTo(ImmutableList.of(language1, language2))));
        verify(languageRepository, only()).findAll();
    }

    @Test
    @DisplayName("Test no language found for the code")
    void findByCode_NothingFound() {
        String code = "En";

        when(languageRepository.findByTwoLetterCode(code)).thenReturn(Optional.empty());

        Optional<Language> languageOptional = service.findByTwoLetterCode(code);

        assertThat(languageOptional.isEmpty(), is(true));
        verify(languageRepository, only()).findByTwoLetterCode(code);
    }

    @Test
    @DisplayName("Test a language found for the code")
    void findByCode_Found() {
        String code = "En";
        Language lan = new Language();
        lan.setTwoLetterCode(code);

        when(languageRepository.findByTwoLetterCode(code)).thenReturn(Optional.of(lan));

        Optional<Language> languageOptional = service.findByTwoLetterCode(code);

        assertThat(languageOptional.isPresent(), is(true));
        assertThat(languageOptional.get().getTwoLetterCode(), is(code));
        verify(languageRepository, only()).findByTwoLetterCode(code);
    }

    @Test
    @DisplayName("Test saving a language")
    void testSave() {
        Language langToSave = new Language();

        Language persistedLang = new Language();
        int persistedLangId = 0;
        persistedLang.setId(persistedLangId);

        when(languageRepository.save(langToSave)).thenReturn(persistedLang);

        Language savedLang = service.save(langToSave);

        assertThat(savedLang, is(notNullValue()));
        assertThat(savedLang.getId(), is(equalTo(persistedLangId)));
        verify(languageRepository, only()).save(langToSave);
    }
}