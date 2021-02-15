package zh.lingvo.data.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.test.context.ContextConfiguration;
import zh.lingvo.data.model.Language;

import javax.persistence.PersistenceException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static zh.hamcrest.ZhMatchers.empty;
import static zh.hamcrest.ZhMatchers.hasPropertySatisfying;

@ContextConfiguration(classes = LanguageRepository.class)
class LanguageRepositoryTest extends BaseRepositoryTest<LanguageRepository> {
    private static final String NAME_1 = "Language 1";
    private static final String NAME_2 = "Language 2";
    private static final String NAME_3 = "Language 3";
    private static final String CODE_1 = "L1";
    private static final String CODE_2 = "L2";
    private static final String CODE_3 = "L3";

    @BeforeEach
    void setUpDb() {
        entityManager.persist(getLanguage(NAME_1, CODE_1));
        entityManager.persist(getLanguage(NAME_2, CODE_2));
        entityManager.persist(getLanguage(NAME_3, CODE_3));
        entityManager.flush();
    }

    private Language getLanguage(String name, String code) {
        return Language.builder()
                .name(name)
                .twoLetterCode(code)
                .build();
    }

    @Test
    @DisplayName("Should return nothing when there is no language for the code")
    void findByTwoLetterCode_NoLanguage() {
        Optional<Language> languageOptional = repository.findByTwoLetterCode("Lg");

        assertThat(languageOptional, is(empty()));
    }

    @Test
    @DisplayName("Should return the language by the code if it is in the DB")
    void findByTwoLetterCode_LanguageExists() {
        Optional<Language> languageOptional = repository.findByTwoLetterCode(CODE_2);

        assertThat(languageOptional, is(not(empty())));
        assertThat(languageOptional, hasPropertySatisfying(Language::getName, NAME_2::equals));
        assertThat(languageOptional, hasPropertySatisfying(Language::getTwoLetterCode, CODE_2::equals));
    }

    @Test
    @DisplayName("Should throw when trying to persist a language with too long code")
    void persistTwoLongCode_Throw() {
        assertThrows(PersistenceException.class,
                () -> entityManager.persistAndFlush(getLanguage("Lang", "aaa")));
    }

    @Test
    @DisplayName("Should trow when trying to persist a language with not unique code")
    void persistDuplicatedCode_Throw() {
        assertThrows(PersistenceException.class,
                () -> entityManager.persistAndFlush(getLanguage("Lang", CODE_1)));
    }
}