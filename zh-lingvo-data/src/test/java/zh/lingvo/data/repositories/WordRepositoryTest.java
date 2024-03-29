package zh.lingvo.data.repositories;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import zh.lingvo.core.domain.PartOfSpeech;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.Word;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@ContextConfiguration(classes = WordRepository.class)
@DisplayName("Test JPA WordRepository")
class WordRepositoryTest extends BaseRepositoryTest<WordRepository> {
    private static final String MAIN_FORM_1 = "word";
    private static final String MAIN_FORM_3 = "draw";
    private static final String MAIN_FORM_4 = "cook";
    private static final String TRANSCRIPTION_1 = "t1";
    private static final String TRANSCRIPTION_2 = "t2";
    private static final String TRANSCRIPTION_3 = "t3";
    private static final String TRANSCRIPTION_4 = "t4";
    private static final String TRANSCRIPTION_5 = "t5";

    private final User user = User.builder().name("test").build();
    private final Language language = Language.builder().name("Lang").twoLetterCode("Ln").build();
    private final Dictionary dictionary = Dictionary.builder().name("Dictionary").language(language).user(user).build();
    private final Dictionary dictionary2 = Dictionary.builder().name("Dictionary 2").language(language).user(user).build();
    private final PartOfSpeech pos = PartOfSpeech.VERB;

    @BeforeEach
    void setUpDb() {
        entityManager.persist(user);
        entityManager.persist(language);
        entityManager.persist(dictionary);
        entityManager.persist(dictionary2);
        entityManager.persist(getWord(MAIN_FORM_1, TRANSCRIPTION_1));
        entityManager.persist(getWord(MAIN_FORM_1, TRANSCRIPTION_2));
        entityManager.persist(getWord(MAIN_FORM_3, TRANSCRIPTION_3));
        entityManager.persist(getWord(MAIN_FORM_4, TRANSCRIPTION_4, dictionary2));
        entityManager.persist(getWord(MAIN_FORM_1, TRANSCRIPTION_5, dictionary2));
        entityManager.flush();
    }

    private Word getWord(String mainForm, String transcription) {
        return getWord(mainForm, transcription, dictionary);
    }

    private Word getWord(String mainForm, String transcription, Dictionary dictionary) {
        return Word.builder()
                .mainForm(mainForm)
                .transcription(transcription)
                .dictionary(dictionary)
                .build();
    }

    @Test
    @DisplayName("Should return an empty list when there are not words for the main form")
    void findAllByMainForm_NoWords() {
        List<Word> words = repository.findAllByMainForm("missing");

        assertThat(words, is(empty()));
    }

    @Test
    @DisplayName("Should return the word matching the main form")
    void findAllByMainForm_OneWord() {
        List<Word> words = repository.findAllByMainForm(MAIN_FORM_3);

        assertThat(words, hasSize(1));
        assertThat(words.get(0).getTranscription(), is(equalTo(TRANSCRIPTION_3)));
    }

    @Test
    @DisplayName("Should return all words matching the main form")
    void findAllByMainForm_ThreeWords() {
        List<Word> words = repository.findAllByMainForm(MAIN_FORM_1);

        Set<String> actualTranscriptions = words.stream()
                .map(Word::getTranscription)
                .collect(ImmutableSet.toImmutableSet());
        Set<String> expectedTranscriptions = ImmutableSet.of(TRANSCRIPTION_1, TRANSCRIPTION_2, TRANSCRIPTION_5);
        assertThat(actualTranscriptions, is(equalTo(expectedTranscriptions)));
    }

    @Test
    @DisplayName("Should return all words from the given dictionary")
    void findAllByDictionary() {
        List<Word> words = repository.findAllByDictionary(dictionary);

        Set<String> actualTranscriptions = words.stream()
                .map(Word::getTranscription)
                .collect(ImmutableSet.toImmutableSet());
        Set<String> expectedTranscriptions = ImmutableSet.of(TRANSCRIPTION_1, TRANSCRIPTION_2, TRANSCRIPTION_3);
        assertThat(actualTranscriptions, is(equalTo(expectedTranscriptions)));

        words = repository.findAllByDictionary(dictionary2);

        actualTranscriptions = words.stream()
                .map(Word::getTranscription)
                .collect(ImmutableSet.toImmutableSet());
        expectedTranscriptions = ImmutableSet.of(TRANSCRIPTION_4, TRANSCRIPTION_5);
        assertThat(actualTranscriptions, is(equalTo(expectedTranscriptions)));
    }

    @Test
    @DisplayName("Should return all words from the dictionary matching the main form")
    void findAllByMainFormAndDictionary() {
        List<Word> words = repository.findAllByMainFormAndDictionary(MAIN_FORM_1, dictionary);

        Set<String> actualTranscriptions = words.stream()
                .map(Word::getTranscription)
                .collect(ImmutableSet.toImmutableSet());
        Set<String> expectedTranscriptions = ImmutableSet.of(TRANSCRIPTION_1, TRANSCRIPTION_2);
        assertThat(actualTranscriptions, is(equalTo(expectedTranscriptions)));
    }
}