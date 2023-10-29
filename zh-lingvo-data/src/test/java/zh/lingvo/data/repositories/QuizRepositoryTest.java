package zh.lingvo.data.repositories;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import zh.hamcrest.ZhMatchers;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.User;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static zh.lingvo.data.model.enums.MatchingRegime.RELAXED;
import static zh.lingvo.data.model.enums.QuizRegime.FORWARD;

@ContextConfiguration(classes = QuizRepository.class)
class QuizRepositoryTest extends BaseRepositoryTest<QuizRepository> {
    private final Language LANGUAGE_1 = Language.builder().name("Language 1").twoLetterCode("L1").build();
    private final Language LANGUAGE_2 = Language.builder().name("Language 2").twoLetterCode("L2").build();
    private final User USER_1 = User.builder().name("User 1").build();
    private final User USER_2 = User.builder().name("User 2").build();
    private final User USER_3 = User.builder().name("User 3").build();
    private final Quiz QUIZ_1 = Quiz.builder().name("Quiz 1").language(LANGUAGE_1).user(USER_1)
            .quizRegime(FORWARD).matchingRegime(RELAXED).maxScore(30).build();
    private final Quiz QUIZ_2 = Quiz.builder().name("Quiz 2").language(LANGUAGE_1).user(USER_2)
            .quizRegime(FORWARD).matchingRegime(RELAXED).maxScore(30).build();
    private final Quiz QUIZ_3 = Quiz.builder().name("Quiz 3").language(LANGUAGE_2).user(USER_1)
            .quizRegime(FORWARD).matchingRegime(RELAXED).maxScore(30).build();

    @BeforeEach
    void setUp() {
        ImmutableList.of(
                USER_1, USER_2, USER_3,
                LANGUAGE_1, LANGUAGE_2,
                QUIZ_1, QUIZ_2, QUIZ_3
        )
                .forEach(entityManager::persist);
        entityManager.flush();
    }

    @Test
    @DisplayName("findAllByUser() should return an empty list when there are no quizzes")
    void findAllByUser_NoQuizzes() {
        List<Quiz> quizzes = repository.findAllByUser(USER_3);

        assertThat(quizzes, is(empty()));
    }

    @Test
    @DisplayName("findAllByUser() should return the list of user's quizzes")
    void findAllByUser_ThereAreQuizzes() {
        List<Quiz> quizzes = repository.findAllByUser(USER_1);

        ImmutableSet<String> actualQuizNames = quizzes.stream()
                .map(Quiz::getName)
                .collect(ImmutableSet.toImmutableSet());
        ImmutableSet<String> expectedQuizNames = ImmutableList.of(QUIZ_1, QUIZ_3).stream()
                .map(Quiz::getName)
                .collect(ImmutableSet.toImmutableSet());
        assertThat(actualQuizNames, is(equalTo(expectedQuizNames)));
    }

    @Test
    @DisplayName("findAllByUserAndLanguage()")
    void findAllByUserAndLanguage() {
        List<Quiz> quizzes = repository.findAllByUserAndLanguage(USER_1, LANGUAGE_1);

        ImmutableSet<String> actualQuzNames = quizzes.stream()
                .map(Quiz::getName)
                .collect(ImmutableSet.toImmutableSet());
        ImmutableSet<String> expectedQuizNames = ImmutableList.of(QUIZ_1).stream()
                .map(Quiz::getName)
                .collect(ImmutableSet.toImmutableSet());
        assertThat(actualQuzNames, is(equalTo(expectedQuizNames)));
    }

    @Test
    @DisplayName("findByIdAndUser() should return the quiz if it exists and belongs to the user")
    void findByIdAndUser_QuizExists_BelongsToUser() {
        Optional<Quiz> quizOptional = repository.findByIdAndUser(QUIZ_1.getId(), USER_1);

        assertThat(quizOptional, is(not(ZhMatchers.empty())));
        assertThat(quizOptional.get(), is(equalTo(QUIZ_1)));
    }

    @Test
    @DisplayName("findByIdAndUser() should return nothing if the quiz exists, but belongs to a different user")
    void findByIdAndUser_QuizExists_BelongsToDifferentUser() {
        Optional<Quiz> quizOptional = repository.findByIdAndUser(QUIZ_1.getId(), USER_2);

        assertThat(quizOptional, is(ZhMatchers.empty()));
    }
}