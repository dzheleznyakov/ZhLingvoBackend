package zh.lingvo.rest.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.data.model.QuizExample;
import zh.lingvo.rest.commands.ExampleCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Test QuizExample to ExampleCommand converter")
class QuizExampleToExampleCommandTest {
    private QuizExampleToExampleCommand converter;

    @BeforeEach
    void setUp() {
        converter = new QuizExampleToExampleCommand();
    }

    @Test
    @DisplayName("Should convert null to null")
    void convertNull() {
        ExampleCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert a quiz example to an example command")
    void convertQuizExample() {
        Long id = 42L;
        String remark = "test remark";
        String expression = "test expression";
        String explanation = "test explanation";
        QuizExample quizExample = QuizExample.builder()
                .id(id)
                .remark(remark)
                .expression(expression)
                .explanation(explanation)
                .build();

        ExampleCommand command = converter.convert(quizExample);

        assertThat(command, is(notNullValue()));
        assertThat(command.getId(), is(id));
        assertThat(command.getRemark(), is(remark));
        assertThat(command.getExpression(), is(expression));
        assertThat(command.getExplanation(), is(explanation));
    }
}