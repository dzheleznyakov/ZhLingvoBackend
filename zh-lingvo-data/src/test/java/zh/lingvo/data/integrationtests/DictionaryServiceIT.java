package zh.lingvo.data.integrationtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.User;

import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static zh.hamcrest.ZhMatchers.empty;
import static zh.hamcrest.ZhMatchers.hasPropertySatisfying;

@DisplayName("Test DictionaryService related workflows")
public class DictionaryServiceIT extends BaseDataIntegrationTest {
    private User user;
    private Language language;

    @BeforeEach
    void setUp() {
        user = userRepository.findById(1L).get();
        language = languageRepository.findById(1).get();
    }

    @Test
    @DisplayName("Should persist a new dictionary")
    void testSavingDictionary() {
        String dictionaryName = "Test dictionary";
        Dictionary dictionary = Dictionary.builder()
                .name(dictionaryName)
                .language(language)
                .user(user)
                .build();

        Optional<Dictionary> savedOptional = dictionaryService.save(dictionary, user);

        assertThat(savedOptional, is(not(empty())));
        assertThat(savedOptional, hasPropertySatisfying(Dictionary::getId, Objects::nonNull));
        assertThat(savedOptional, hasPropertySatisfying(Dictionary::getName, dictionaryName::equals));
    }

    @Test
    @DisplayName("Should update the dictionary name")
    void testUpdatingDictionaryName() {
        Dictionary dictionary = setupPersistedDictionary("Test dictionary");

        String updatedName = "New name";
        dictionary.setName(updatedName);
        Optional<Dictionary> updatedOptional = dictionaryService.save(dictionary, user);

        assertThat(updatedOptional, is(not(empty())));
        assertThat(updatedOptional, hasPropertySatisfying(Dictionary::getId, dictionary.getId()::equals));
        assertThat(updatedOptional, hasPropertySatisfying(Dictionary::getName, updatedName::equals));
    }

    @Test
    @DisplayName("Should delete dictionary by id")
    void testDeleteDictionary() {
        Dictionary dictionary = setupPersistedDictionary("Test dictionary: delete");
        Long id = dictionary.getId();

        boolean deleted = dictionaryService.deleteById(id, user);

        assertThat(deleted, is(true));
        Dictionary dbDictionary = findEntity(Dictionary.class, id);
        assertThat(dbDictionary, is(nullValue()));
    }

    @Test
    @DisplayName("Should find dictionary by id")
    void testFindDictionary() {
        String dictionaryName = "Test dictionary: find";
        Dictionary dictionary = setupPersistedDictionary(dictionaryName);

        Optional<Dictionary> foundDictionary = dictionaryService.findById(dictionary.getId(), user);

        assertThat(foundDictionary, is(not(empty())));
        assertThat(foundDictionary, hasPropertySatisfying(Dictionary::getId, dictionary.getId()::equals));
        assertThat(foundDictionary, hasPropertySatisfying(Dictionary::getName, dictionaryName::equals));
    }

    private Dictionary setupPersistedDictionary(String name) {
        Dictionary dictionary = Dictionary.builder()
                .name(name)
                .language(language)
                .user(user)
                .build();
        return dictionaryService.save(dictionary, user)
                .orElseThrow(RuntimeException::new);
    }
}
