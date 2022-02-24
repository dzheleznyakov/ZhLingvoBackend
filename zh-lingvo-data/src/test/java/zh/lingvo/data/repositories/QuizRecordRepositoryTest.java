package zh.lingvo.data.repositories;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import zh.lingvo.core.domain.PartOfSpeech;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.QuizExample;
import zh.lingvo.data.model.QuizRecord;
import zh.lingvo.data.model.QuizTranslation;
import zh.lingvo.data.model.User;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static zh.lingvo.data.model.enums.MatchingRegime.RELAXED;
import static zh.lingvo.data.model.enums.QuizRegime.FORWARD;

@ContextConfiguration(classes = QuizRecordRepository.class)
class QuizRecordRepositoryTest extends BaseRepositoryTest<QuizRecordRepository> {
    private final Language LANGUAGE_1 = Language.builder().name("Language 1").twoLetterCode("L1").build();
    private final Language LANGUAGE_2 = Language.builder().name("Language 2").twoLetterCode("L2").build();
    private final User USER_1 = User.builder().name("User 1").build();
    private final User USER_2 = User.builder().name("User 2").build();
    private final Quiz QUIZ_1_WITH_RECORDS = Quiz.builder().name("Quiz 1").language(LANGUAGE_1).user(USER_1)
            .quizRegime(FORWARD).matchingRegime(RELAXED).maxScore(30).build();
    private final Quiz QUIZ_2_WITH_NO_RECORDS = Quiz.builder().name("Quiz 2").language(LANGUAGE_2).user(USER_2)
            .quizRegime(FORWARD).matchingRegime(RELAXED).maxScore(30).build();

    private final QuizRecord RECORD_1 = QuizRecord.builder()
            .quiz(QUIZ_1_WITH_RECORDS).currentScore(0f).numberOfRuns(0).numberOfSuccesses(0)
            .wordMainForm("main1").pos(PartOfSpeech.NOUN)
            .build();
    private final QuizRecord RECORD_2 = QuizRecord.builder()
            .quiz(QUIZ_1_WITH_RECORDS).currentScore(0f).numberOfRuns(0).numberOfSuccesses(0)
            .wordMainForm("main2").pos(PartOfSpeech.VERB)
            .build();

    private final QuizTranslation TRANSLATION_1_1 = QuizTranslation.builder()
            .record(RECORD_1)
            .elaboration("el11").value("v11").build();
    private final QuizTranslation TRANSLATION_1_2 = QuizTranslation.builder()
            .record(RECORD_1)
            .elaboration("el12").value("v12").build();
    private final QuizTranslation TRANSLATION_2_1 = QuizTranslation.builder()
            .record(RECORD_2)
            .elaboration("el21").value("v21").build();
    private final QuizTranslation TRANSLATION_2_2 = QuizTranslation.builder()
            .record(RECORD_2)
            .elaboration("el22").value("v22").build();

    private final QuizExample EXAMPLE_1_1 = QuizExample.builder()
            .record(RECORD_1)
            .explanation("expl11").expression("expr11").build();
    private final QuizExample EXAMPLE_1_2 = QuizExample.builder()
            .record(RECORD_1)
            .explanation("expl12").expression("expr12").build();
    private final QuizExample EXAMPLE_2_1 = QuizExample.builder()
            .record(RECORD_2)
            .explanation("expl21").expression("expr21").build();
    private final QuizExample EXAMPLE_2_2 = QuizExample.builder()
            .record(RECORD_2)
            .explanation("expl22").expression("expr22").build();


    @BeforeEach
    void setUp() {
        ImmutableList.of(
                LANGUAGE_1, LANGUAGE_2,
                USER_1, USER_2,
                QUIZ_1_WITH_RECORDS, QUIZ_2_WITH_NO_RECORDS,
                RECORD_1, RECORD_2,
                TRANSLATION_1_1, TRANSLATION_1_2, TRANSLATION_2_1, TRANSLATION_2_2,
                EXAMPLE_1_1, EXAMPLE_1_2, EXAMPLE_2_1, EXAMPLE_2_2
        ).forEach(entityManager::persist);
        entityManager.flush();
    }

    @Test
    @DisplayName("If the quiz has no records, an empty list should be returned")
    void findAllByQuiz_NoRecords() {
        List<QuizRecord> records = repository.findAllByQuiz(QUIZ_2_WITH_NO_RECORDS);

        assertThat(records, is(empty()));
    }

    @Test
    @DisplayName("If the quiz has records, they all are returned")
    void findAllByQuiz_ThereAreRecords() {
        List<QuizRecord> records = repository.findAllByQuiz(QUIZ_1_WITH_RECORDS);

        Set<String> expectedRecordMainForms =
                mapToAttributes(ImmutableSet.of(RECORD_1, RECORD_2), QuizRecord::getWordMainForm);
        Set<String> actualRecordMainForms =
                mapToAttributes(records, QuizRecord::getWordMainForm);
        assertThat(actualRecordMainForms, is(equalTo(expectedRecordMainForms)));
    }
}