package zh.lingvo.rest.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.data.model.Example;
import zh.lingvo.rest.commands.ExampleCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

class ExampleCommandToExampleTest {
    private ExampleCommandToExample converter;

    @BeforeEach
    void setUp() {
        converter = new ExampleCommandToExample();
    }

    @Test
    @DisplayName("Should convert null into null")
    void convertNull() {
        Example example = converter.convert(null);

        assertThat(example, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert ExampleCommand into Example")
    void convertExampleCommand() {
        ExampleCommand command = ExampleCommand.builder()
                .id(42L)
                .remark("mock remark")
                .expression("mock expression")
                .explanation("mock explanation")
                .build();

        Example example = converter.convert(command);

        assertThat(example, is(notNullValue()));
        assertThat(example.getId(), is(equalTo(command.getId())));
        assertThat(example.getRemark(), is(equalTo(command.getRemark())));
        assertThat(example.getExpression(), is(equalTo(command.getExpression())));
        assertThat(example.getExplanation(), is(equalTo(command.getExplanation())));
    }
}