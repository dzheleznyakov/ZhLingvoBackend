package zh.lingvo.data.integrationtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.enums.MatchingRegime;
import zh.lingvo.data.model.enums.QuizRegime;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static zh.hamcrest.ZhMatchers.empty;
import static zh.hamcrest.ZhMatchers.hasPropertySatisfying;

@DisplayName("Test QuizService related workflows")
public class QuizServiceIT extends BaseDataIntegrationTest{
    private User user;
    private User anotherUser;
    private Language language;

    @BeforeEach
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void setUp() {
        user = userRepository.findById(1L).get();
        anotherUser = userRepository.findById(2L).get();
        language = languageRepository.findById(1).get();
    }

    @Test
    @DisplayName("Should persist a new quiz")
    void testSavingQuiz() {
        String quizName = "Test quiz";
        Quiz quiz = newQuiz(quizName);

        Optional<Quiz> savedOptional = quizService.save(quiz, user);

        assertThat(savedOptional, is(not(empty())));
        assertThat(savedOptional, hasPropertySatisfying(Quiz::getId, Objects::nonNull));
        assertThat(savedOptional, hasPropertySatisfying(Quiz::getName, quizName::equals));
    }

    @Test
    @DisplayName("Should update quiz")
    void testUpdatingQuiz() {
        Quiz quiz = setUpPersistedQuiz("Test quiz: update");

        String updatedName = "New name";
        quiz.setName(updatedName);
        Optional<Quiz> updatedQuiz = quizService.save(quiz, user);

        assertThat(updatedQuiz, is(not(empty())));
        assertThat(updatedQuiz, hasPropertySatisfying(Quiz::getId, quiz.getId()::equals));
        assertThat(updatedQuiz, hasPropertySatisfying(Quiz::getName, updatedName::equals));
    }

    @Test
    @DisplayName("Should not update quiz if it belongs to another user")
    void testUpdateQuizOfAnotherUser() {
        String quizName = "Test quiz: update of another user";
        Quiz quiz = setUpPersistedQuiz(quizName);

        quiz.setName("New name");
        Optional<Quiz> updatedQuiz = quizService.save(quiz, anotherUser);

        assertThat(updatedQuiz, is(empty()));
    }

    @Test
    @DisplayName("Should delete quiz by id")
    void testDeleteQuiz() {
        Quiz quiz = setUpPersistedQuiz("Test quiz: delete");
        Long id = quiz.getId();

        boolean deleted = quizService.deleteById(id, user);

        assertThat(deleted, is(true));
        Quiz dbQuiz = findEntity(Quiz.class, id);
        assertThat(dbQuiz, is(nullValue()));
    }

    @Test
    @DisplayName("Should not delete quiz if it belongs to another user")
    void testDeleteQuizOfAnotherUser() {
        Quiz quiz = setUpPersistedQuiz("Test quiz: delete of another user");

        Long id = quiz.getId();
        boolean deleted = quizService.deleteById(id, anotherUser);

        assertThat(deleted, is(true));
        Quiz dbQuiz = findEntity(Quiz.class, id);
        assertThat(dbQuiz, is(notNullValue()));
    }

    @Test
    @DisplayName("Should find quiz by id")
    void testFindDictionary() {
        String quizName = "Test quiz: find";
        Quiz quiz = setUpPersistedQuiz(quizName);

        Optional<Quiz> foundQuiz = quizService.findById(quiz.getId(), user);

        assertThat(foundQuiz, is(not(empty())));
        assertThat(foundQuiz, hasPropertySatisfying(Quiz::getId, quiz.getId()::equals));
        assertThat(foundQuiz, hasPropertySatisfying(Quiz::getName, quizName::equals));
    }

    @Test
    @DisplayName("Should not find the quiz if it belongs to another user")
    void testFindQuizOfAnotherUser() {
        Quiz quiz = setUpPersistedQuiz("Test quiz: find of another user");

        Optional<Quiz> foundQuiz = quizService.findById(quiz.getId(), anotherUser);

        assertThat(foundQuiz, is(empty()));
    }

    private Quiz setUpPersistedQuiz(String name) {
        Quiz quiz = newQuiz(name);
        return quizRepository.save(quiz);
    }

    private Quiz newQuiz(String name) {
        return Quiz.builder()
                .name(name)
                .language(language)
                .user(user)
                .matchingRegime(MatchingRegime.RELAXED)
                .quizRegime(QuizRegime.FORWARD)
                .maxScore(30)
                .quizRecords(new ArrayList<>())
                .build();
    }
}