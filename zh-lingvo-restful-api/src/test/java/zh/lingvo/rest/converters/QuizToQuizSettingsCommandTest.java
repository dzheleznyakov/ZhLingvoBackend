package zh.lingvo.rest.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.enums.MatchingRegime;
import zh.lingvo.data.model.enums.QuizRegime;
import zh.lingvo.rest.commands.QuizSettingsCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Test Quiz to QuizSettingsCommand converter")
class QuizToQuizSettingsCommandTest {
    private QuizToQuizSettingsCommand converter;

    @BeforeEach
    void setUp() {
        converter = new QuizToQuizSettingsCommand();
    }

    @Test
    @DisplayName("Should convert null to null")
    void convertNull() {
        QuizSettingsCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert quiz to quiz settings")
    void convertQuiz() {
        Long quizId = 42L;
        Integer maxScore = 666;
        QuizRegime quizRegime = QuizRegime.FORWARD;
        MatchingRegime matchingRegime = MatchingRegime.STRICT;
        Quiz quiz = Quiz.builder()
                .id(quizId)
                .maxScore(maxScore)
                .quizRegime(quizRegime)
                .matchingRegime(matchingRegime)
                .build();

        QuizSettingsCommand command = converter.convert(quiz);

        assertThat(command, is(notNullValue()));
        assertThat(command.getQuizId(), is(quizId));
        assertThat(command.getMaxScore(), is(maxScore));
        assertThat(command.getQuizRegime(), is(quizRegime.name()));
        assertThat(command.getMatchingRegime(), is(matchingRegime.name()));
    }
}