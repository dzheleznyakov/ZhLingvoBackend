package zh.lingvo.data.repositories;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.QuizRun;
import zh.lingvo.data.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static zh.hamcrest.ZhMatchers.empty;
import static zh.hamcrest.ZhMatchers.hasPropertySatisfying;
import static zh.lingvo.data.model.enums.MatchingRegime.RELAXED;
import static zh.lingvo.data.model.enums.QuizRegime.FORWARD;

@ContextConfiguration(classes = QuizRunRepository.class)
class QuizRunRepositoryTest extends BaseRepositoryTest<QuizRunRepository> {
    private final Language LANGUAGE_1 = Language.builder().name("Language 1").twoLetterCode("L1").build();
    private final User USER_1 = User.builder().name("User 1").build();
    private final User USER_2 = User.builder().name("User 2").build();
    private final User USER_3 = User.builder().name("User 3").build();
    private final Quiz QUIZ_1 = Quiz.builder().name("Quiz 1").language(LANGUAGE_1).user(USER_1).build();
    private final Quiz QUIZ_2 = Quiz.builder().name("Quiz 2").language(LANGUAGE_1).user(USER_1).build();
    private final Quiz QUIZ_3 = Quiz.builder().name("Quiz 3").language(LANGUAGE_1).user(USER_3).build();

    private final QuizRun QUIZ_RUN_1 = QuizRun.builder().quiz(QUIZ_1).quizRegime(FORWARD).matchingRegime(RELAXED).build();
    private final QuizRun QUIZ_RUN_2 = QuizRun.builder().quiz(QUIZ_1).quizRegime(FORWARD).matchingRegime(RELAXED).build();
    private Long quizRunId1;
    private Long quizRunId2;

    @BeforeEach
    void setUp() {
        ImmutableList.of(
                LANGUAGE_1,
                USER_1, USER_2, USER_3,
                QUIZ_1, QUIZ_2, QUIZ_3,
                QUIZ_RUN_1, QUIZ_RUN_2
        )
                .forEach(entityManager::persist);
        entityManager.flush();

        quizRunId1 = QUIZ_RUN_1.getId();
        quizRunId2 = QUIZ_RUN_2.getId();
    }

    @Test
    @DisplayName("findByIdAndUser() should return nothing if the quiz if from different user")
    void findByIdAndUser_WrongUser() {
        Optional<QuizRun> possibleQuizRun = repository.findByIdAndUser(quizRunId1, USER_2);
        assertThat(possibleQuizRun, is(empty()));
    }

    @Test
    @DisplayName("findByIdAndUser() should return the quiz run if the quiz belongs to the user")
    void findByIdAndUser_RightUser() {
        Optional<QuizRun> possibleQuizRun = repository.findByIdAndUser(quizRunId1, USER_1);

        assertThat(possibleQuizRun, is(not(empty())));
        assertThat(possibleQuizRun, hasPropertySatisfying(QuizRun::getId, quizRunId1::equals));
    }

    @Test
    @DisplayName("findAllByQuizAndUser() should return an empty list if the quiz does not have runs")
    void findAllByQuizAndUser_QuizWithNoRuns() {
        List<QuizRun> quizRuns = repository.findAllByQuizAndUser(QUIZ_2.getId(), QUIZ_2.getUser());

        assertThat(quizRuns, is(notNullValue()));
        assertThat(quizRuns, hasSize(0));
    }

    @Test
    @DisplayName("findAllByQuizAndUser() should return an empty list if the quiz belongs to a different user")
    void findAllByQuizAndUser_WrongUser() {
        List<QuizRun> quizRuns = repository.findAllByQuizAndUser(QUIZ_1.getId(), USER_2);

        assertThat(quizRuns, is(notNullValue()));
        assertThat(quizRuns, hasSize(0));
    }

    @Test
    @DisplayName("findAllByQuizAndUser() should return the quiz runs for a given quiz and user")
    void findAllByQuizAndUser_HappyPath() {
        List<QuizRun> quizRuns = repository.findAllByQuizAndUser(QUIZ_1.getId(), USER_1);

        assertThat(quizRuns, is(notNullValue()));

        Set<Long> expectedQuizRunsIds = ImmutableSet.of(quizRunId1, quizRunId2);
        Set<Long> actualQuizRunsIds = mapToAttributes(quizRuns, QuizRun::getId);
        assertThat(expectedQuizRunsIds, is(actualQuizRunsIds));
    }

    @Test
    @DisplayName("findAllByUser() should return an empty list if the user does not have quizzes")
    void findAllByUser_UserWithNoQuizzes() {
        List<QuizRun> quizRuns = repository.findAllByUser(USER_2);

        assertThat(quizRuns, hasSize(0));
    }

    @Test
    @DisplayName("findAllByUser() should return an empty list if the user has quizzes, but no pending runs")
    void findAllByUser_UserWithNoQuizRuns() {
        List<QuizRun> quizRuns = repository.findAllByUser(USER_3);

        assertThat(quizRuns, hasSize(0));
    }

    @Test
    @DisplayName("findAllByUser() should return all pending quiz runs for a given user")
    void findAllByUser_HappyPath() {
        List<QuizRun> quizRuns = repository.findAllByUser(USER_1);

        assertThat(quizRuns, hasSize(2));

        Set<Long> expectedQuizRunsIds = ImmutableSet.of(quizRunId1, quizRunId2);
        Set<Long> actualQuizRunsIds = mapToAttributes(quizRuns, QuizRun::getId);
        assertThat(expectedQuizRunsIds, is(actualQuizRunsIds));
    }
}