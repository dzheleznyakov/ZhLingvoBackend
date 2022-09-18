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

@DisplayName("Test ExampleCommand to QuizExample converter")
class ExampleCommandToQuizExampleTest {
    private ExampleCommandToQuizExample converter;

    @BeforeEach
    void setUp() {
        converter = new ExampleCommandToQuizExample();
    }

    @Test
    @DisplayName("Should convert null to null")
    void convertNull() {
        QuizExample quizExample = converter.convert(null);

        assertThat(quizExample, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert an example command to a quiz example")
    void convertExampleCommand() {
        Long id = 42L;
        String remark = "test remark";
        String expression = "test expression";
        String explanation = "test explanation";

        ExampleCommand command = ExampleCommand.builder()
                .id(id)
                .remark(remark)
                .expression(expression)
                .explanation(explanation)
                .build();

        QuizExample quizExample = converter.convert(command);

        assertThat(quizExample, is(notNullValue()));
        assertThat(quizExample.getId(), is(id));
        assertThat(quizExample.getRemark(), is(remark));
        assertThat(quizExample.getExpression(), is(expression));
        assertThat(quizExample.getExplanation(), is(explanation));
    }
}