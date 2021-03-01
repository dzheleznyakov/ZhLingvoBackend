package zh.lingvo.data.repositories;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@ContextConfiguration(classes = DictionaryRepository.class)
class DictionaryRepositoryTest extends BaseRepositoryTest<DictionaryRepository> {
    private final Language LANGUAGE_1 = Language.builder().name("Language 1").twoLetterCode("L1").build();
    private final Language LANGUAGE_2 = Language.builder().name("Language 2").twoLetterCode("L2").build();
    private final User USER_1 = User.builder().name("User 1").build();
    private final User USER_2 = User.builder().name("User 2").build();
    private final User USER_3 = User.builder().name("User 3").build();
    private final Dictionary DICTIONARY_1_1 = Dictionary.builder().language(LANGUAGE_1).name("Dictionary 1.1").user(USER_1).build();
    private final Dictionary DICTIONARY_1_2 = Dictionary.builder().language(LANGUAGE_1).name("Dictionary 1.2").user(USER_1).build();
    private final Dictionary DICTIONARY_1_3 = Dictionary.builder().language(LANGUAGE_2).name("Dictionary 1.3").user(USER_1).build();
    private final Dictionary DICTIONARY_2_1 = Dictionary.builder().language(LANGUAGE_1).name("Dictionary 2.1").user(USER_2).build();

    @BeforeEach
    void setUp() {
        ImmutableList.of(
                USER_1, USER_2, USER_3,
                LANGUAGE_1, LANGUAGE_2,
                DICTIONARY_1_1, DICTIONARY_1_2, DICTIONARY_1_3, DICTIONARY_2_1
        )
                .forEach(entityManager::persist);
        entityManager.flush();
    }

    @Test
    @DisplayName("findByUser() should return an empty list when there are no dictionaries")
    void findByUser_NoDictionaries() {
        List<Dictionary> dictionaries = repository.findByUser(USER_3);

        assertThat(dictionaries, is(empty()));
    }

    @Test
    @DisplayName("findByUser() should return the list of user's dictionaries")
    void findByUser_ThereAreDictionaries() {
        List<Dictionary> dictionaries = repository.findByUser(USER_1);

        ImmutableSet<String> actualDictionaryNames = dictionaries.stream()
                .map(Dictionary::getName)
                .collect(ImmutableSet.toImmutableSet());
        ImmutableSet<String> expectedDictionaryNames = ImmutableList.of(DICTIONARY_1_1, DICTIONARY_1_2, DICTIONARY_1_3).stream()
                .map(Dictionary::getName)
                .collect(ImmutableSet.toImmutableSet());
        assertThat(actualDictionaryNames, is(equalTo(expectedDictionaryNames)));
    }
}