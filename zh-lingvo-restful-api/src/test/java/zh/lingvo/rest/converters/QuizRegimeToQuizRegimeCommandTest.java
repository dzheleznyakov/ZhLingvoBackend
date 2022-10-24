package zh.lingvo.rest.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.data.model.enums.QuizRegime;
import zh.lingvo.rest.commands.QuizRegimeCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Test QuizRegime to QuizRegimeCommand converter")
class QuizRegimeToQuizRegimeCommandTest {
    private QuizRegimeToQuizRegimeCommand converter;

    @BeforeEach
    void setUp() {
        converter = new QuizRegimeToQuizRegimeCommand();
    }

    @Test
    @DisplayName("Should convert a quiz regime in a quiz regime command")
    void convertQuizRegime() {
        QuizRegime quizRegime = QuizRegime.FORWARD;

        QuizRegimeCommand command = converter.convert(quizRegime);

        assertThat(command, is(notNullValue()));
        assertThat(command.getValue(), is(equalTo(quizRegime.toString())));
        assertThat(command.getCode(), is(equalTo(quizRegime.getCode())));
    }
}