package zh.lingvo.data.repositories;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import zh.lingvo.data.domain.Dictionary;
import zh.lingvo.data.domain.Language;
import zh.lingvo.data.domain.User;
import zh.lingvo.data.domain.Word;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@ContextConfiguration(classes = WordRepository.class)
class WordRepositoryTest extends BaseRepositoryTest<WordRepository> {
    private static final String MAIN_FORM_1 = "word";
    private static final String MAIN_FORM_3 = "draw";
    private static final String TRANSCRIPTION_1 = "t1";
    private static final String TRANSCRIPTION_2 = "t2";
    private static final String TRANSCRIPTION_3 = "t3";

    private final User user = User.builder().name("test").build();
    private final Language language = Language.builder().name("Lang").twoLetterCode("Ln").build();
    private final Dictionary dictionary = Dictionary.builder().language(language).user(user).build();

    @BeforeEach
    void setUpDb() {
        entityManager.persist(user);
        entityManager.persist(language);
        entityManager.persist(dictionary);
        entityManager.persist(getWord(MAIN_FORM_1, TRANSCRIPTION_1));
        entityManager.persist(getWord(MAIN_FORM_1, TRANSCRIPTION_2));
        entityManager.persist(getWord(MAIN_FORM_3, TRANSCRIPTION_3));
        entityManager.flush();
    }

    private Word getWord(String mainForm, String transcription) {
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
    void findAllByMainForm_TwoWords() {
        List<Word> words = repository.findAllByMainForm(MAIN_FORM_1);

        Set<String> actualTranscriptions = words.stream()
                .map(Word::getTranscription)
                .collect(ImmutableSet.toImmutableSet());
        Set<String> expectedTranscriptions = ImmutableSet.of(TRANSCRIPTION_1, TRANSCRIPTION_2);
        assertThat(actualTranscriptions, is(equalTo(expectedTranscriptions)));
    }
}