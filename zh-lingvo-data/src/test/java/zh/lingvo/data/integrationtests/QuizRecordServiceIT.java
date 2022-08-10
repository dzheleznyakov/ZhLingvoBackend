package zh.lingvo.data.integrationtests;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.core.domain.PartOfSpeech;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.QuizRecord;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.enums.MatchingRegime;
import zh.lingvo.data.model.enums.QuizRegime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static zh.hamcrest.ZhMatchers.empty;
import static zh.hamcrest.ZhMatchers.hasPropertySatisfying;
import static zh.lingvo.core.domain.PartOfSpeech.NOUN;
import static zh.lingvo.core.domain.PartOfSpeech.VERB;

@DisplayName("Test QuizRecordService related workflows")
public class QuizRecordServiceIT extends BaseDataIntegrationTest {
    private static final String QUIZ_NAME = "QuizRecordServiceIT quiz";
    private static final String ANOTHER_QUIZ_NAME = "QuizRecordServiceIT another quiz";

    private User user;
    private User anotherUser;
    private Language language;
    private Quiz quiz;
    private Quiz anotherQuiz;
    private Long quizId;
    private Long anotherQuizId;

    @BeforeEach
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void setUp() {
        user = userRepository.findById(1L).get();
        anotherUser = userRepository.findById(2L).get();
        language = languageRepository.findById(1).get();
        quiz = getPersistedQuiz(QUIZ_NAME);
        anotherQuiz = getPersistedQuiz(ANOTHER_QUIZ_NAME);
        quizId = quiz.getId();
        anotherQuizId = anotherQuiz.getId();
    }

    @Test
    @DisplayName("Should persist a new quiz record")
    void testSavingQuizRecord() {
        PartOfSpeech pos = NOUN;
        String wordMainForm = "word";
        QuizRecord quizRecord = newQuizRecord(pos, wordMainForm);

        Optional<QuizRecord> savedRecord = quizRecordService.create(quizRecord, quizId, user);

        assertThat(savedRecord, is(not(empty())));
        assertThat(savedRecord, hasPropertySatisfying(QuizRecord::getId, Objects::nonNull));
        assertThat(savedRecord, hasPropertySatisfying(QuizRecord::getQuiz, q -> Objects.equals(q.getId(), quiz.getId())));
        assertThat(savedRecord, hasPropertySatisfying(QuizRecord::getPos, pos::equals));
        assertThat(savedRecord, hasPropertySatisfying(QuizRecord::getWordMainForm, wordMainForm::equals));

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        Quiz quizWithRecords = quizRepository.findByIdWithRecords(quiz.getId()).get();
        assertThat(quizWithRecords.getQuizRecords(), hasSize(1));
        QuizRecord recordPersistedForQuiz = quizWithRecords.getQuizRecords().get(0);
        assertThat(savedRecord, hasPropertySatisfying(QuizRecord::getId, recordPersistedForQuiz.getId()::equals));
    }

    @Test
    @DisplayName("Nothing should happen if trying to persist already existing quiz record")
    void testSavingExistingQuizRecord() {
        QuizRecord quizRecord = newQuizRecord(VERB, "do");

        Optional<QuizRecord> savedRecord = quizRecordService.create(quizRecord, quizId, user);
        assertThat(savedRecord, hasPropertySatisfying(QuizRecord::getId, Objects::nonNull));
        Long quizRecordId = quizRecord.getId();

        savedRecord = quizRecordService.create(quizRecord, quizId, user);
        assertThat(savedRecord, is(empty()));
        assertThat(quizRecord.getId(), is(equalTo(quizRecordId)));
    }

    @Test
    @DisplayName("Should update quiz record")
    void testUpdatingQuizRecord() {
        QuizRecord quizRecord = getPersistedQuizRecord("worf");
        Long recordId = quizRecord.getId();
        assertThat(recordId, is(notNullValue()));

        String updatedWordMainForm = "word";
        quizRecord.setWordMainForm(updatedWordMainForm);

        QuizRecord updatedRecord = quizRecordService.update(quizRecord, quizId, user);

        assertThat(updatedRecord.getId(), is(equalTo(recordId)));
        assertThat(updatedRecord.getWordMainForm(), is(equalTo(updatedWordMainForm)));
    }

    @Test
    @DisplayName("Should not update the quiz record if it belongs to the wrong user")
    void testShouldNotUpdateRecordIfItBelongsToDifferentUser() {
        String oldWormMainForm = "worf";
        QuizRecord quizRecord = getPersistedQuizRecord(oldWormMainForm);

        quizRecord.setWordMainForm("word");
        QuizRecord updatedRecord = quizRecordService.update(quizRecord, quizId, anotherUser);

        assertThat(updatedRecord, is(nullValue()));
    }

    @Test
    @DisplayName("Should not update the quiz record if it is from a different quiz")
    void testShouldNotUpdateRecordIfItIsFromDifferentQuiz() {
        String oldWormMainForm = "worf";
        QuizRecord quizRecord = getPersistedQuizRecord(oldWormMainForm);

        quizRecord.setWordMainForm("word");
        QuizRecord updatedRecord = quizRecordService.update(quizRecord, anotherQuizId, user);

        assertThat(updatedRecord, is(nullValue()));
    }

    @Test
    @DisplayName("Should delete quiz record")
    void testShouldDeleteQuizRecordById() {
        QuizRecord quizRecord = getPersistedQuizRecord("home");
        Long id = quizRecord.getId();

        boolean successful = quizRecordService.deleteById(id, user);

        assertThat(successful, is(true));
        QuizRecord dbQuizRecord = findEntity(QuizRecord.class, id);
        assertThat(dbQuizRecord, is(nullValue()));
    }

    @Test
    @DisplayName("Should not delete quiz record if it is not persisted")
    void testShouldNotDeleteQuizRecordIfItIsNotPersisted() {
        QuizRecord quizRecord = newQuizRecord(NOUN, "dog");

        boolean successful = quizRecordService.deleteById(quizRecord.getId(), user);

        assertThat(quizRecord.getId(), is(nullValue()));
        assertThat(successful, is(false));
    }

    @Test
    @DisplayName("Should not really delete quiz record if it belongs to another user")
    void testShouldNotDeleteQuizRecordOfDifferentUser() {
        QuizRecord quizRecord = getPersistedQuizRecord("cat");
        Long id = quizRecord.getId();

        boolean successful = quizRecordService.deleteById(id, anotherUser);

        assertThat(successful, is(true));
        QuizRecord dbQuizRecord = findEntity(QuizRecord.class, id);
        assertThat(dbQuizRecord, is(notNullValue()));
    }

    @Test
    @DisplayName("Should return success if deleting the record that does not exist")
    void testShouldReturnSuccessIfDeletingRecordThatDoesNotExist() {
        Long id = Long.MAX_VALUE;
        QuizRecord dbRecord = findEntity(QuizRecord.class, id);
        assertThat(dbRecord, is(nullValue()));

        boolean success = quizRecordService.deleteById(id, user);

        assertThat(success, is(true));
    }

    @Test
    @DisplayName("Should find quiz record by id")
    void testShouldFindQuizRecordById() {
        String wordMainForm = "word";
        QuizRecord record = getPersistedQuizRecord(wordMainForm);

        Optional<QuizRecord> foundRecord = quizRecordService.findById(record.getId(), user);

        assertThat(foundRecord, is(not(empty())));
        assertThat(foundRecord, hasPropertySatisfying(QuizRecord::getWordMainForm, wordMainForm::equals));
    }

    @Test
    @DisplayName("Should not find quiz record if it belongs to another user")
    void testShouldNotFindRecordOfAnotherUser() {
        QuizRecord record = getPersistedQuizRecord("house");

        Optional<QuizRecord> foundRecord = quizRecordService.findById(record.getId(), anotherUser);

        assertThat(foundRecord, is(empty()));
    }

    @Test
    @DisplayName("Should not find record if id is null")
    void testShouldNotFindRecordByNullId() {
        Optional<QuizRecord> foundRecord = quizRecordService.findById(null, user);

        assertThat(foundRecord, is(empty()));
    }

    @Test
    @DisplayName("Should return an empty list of there are no records in the quiz")
    void testFindAllNoRecords() {
        var quizRecords = ImmutableList.copyOf(quizRecordRepository.findAll())
                .stream()
                .filter(qr -> qr.getQuiz() != null)
                .filter(qr -> Objects.equals(qr.getQuiz().getId(), quizId))
                .collect(ImmutableList.toImmutableList());
        assertThat(quizRecords, hasSize(0));

        List<QuizRecord> foundRecords = quizRecordService.findAll(quizId, user);

        assertThat(foundRecords, hasSize(0));
    }

    @Test
    @DisplayName("Should return all records for the quiz")
    void testFindAll() {
        getPersistedQuizRecord("cat", quizId, user);
        getPersistedQuizRecord("dog", quizId, user);
        getPersistedQuizRecord("cow", anotherQuizId, user);

        List<QuizRecord> foundRecords = quizRecordService.findAll(quizId, user);
        assertThat(foundRecords, hasSize(2));

        var foundRecordWords = foundRecords.stream()
                .map(QuizRecord::getWordMainForm)
                .collect(ImmutableSet.toImmutableSet());
        assertThat(foundRecordWords, is(equalTo(ImmutableSet.of("cat", "dog"))));
    }

    @Test
    @DisplayName("Should return empty list if the quiz belongs to another user")
    void testFindAllForAnotherUser() {
        getPersistedQuizRecord("cat", quizId, user);
        getPersistedQuizRecord("dog", quizId, user);

        List<QuizRecord> foundRecords = quizRecordService.findAll(quizId, anotherUser);

        assertThat(foundRecords, hasSize(0));
    }

    private QuizRecord getPersistedQuizRecord(String mainForm) {
        return getPersistedQuizRecord(mainForm, quizId, user);
    }

    private QuizRecord getPersistedQuizRecord(String mainForm, Long quizId, User user) {
        QuizRecord quizRecord = newQuizRecord(NOUN, mainForm);
        quizRecordService.create(quizRecord, quizId, user);
        return quizRecord;
    }

    private QuizRecord newQuizRecord(PartOfSpeech pos, String wordMainForm) {
        return QuizRecord.builder()
                .currentScore(0.0f)
                .numberOfRuns(0)
                .numberOfSuccesses(0)
                .pos(pos)
                .wordMainForm(wordMainForm)
                .translations(new HashSet<>())
                .examples(new HashSet<>())
                .build();
    }

    private Quiz getPersistedQuiz(String quizName) {
        return getPersistedQuiz(quizName, user);
    }

    private Quiz getPersistedQuiz(String quizName, User user) {
        Quiz quiz = Quiz.builder()
                .name(quizName)
                .language(language)
                .user(user)
                .matchingRegime(MatchingRegime.RELAXED)
                .quizRegime(QuizRegime.FORWARD)
                .maxScore(30)
                .quizRecords(new ArrayList<>())
                .build();
        return quizRepository.save(quiz);
    }
}
