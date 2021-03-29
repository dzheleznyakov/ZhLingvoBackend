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

@DisplayName("Test Example to ExampleCommand converter")
class ExampleToExampleCommandTest {
    private ExampleToExampleCommand converter;

    @BeforeEach
    void setUp() {
        converter = new ExampleToExampleCommand();
    }

    @Test
    @DisplayName("Should convert null to null")
    void convertNull() {
        ExampleCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert Example into ExampleCommand")
    void convertExample() {
        Example example = Example.builder()
                .id(42L)
                .explanation("mock explanation")
                .expression("mock expressiopn")
                .remark("mock remark")
                .build();

        ExampleCommand command = converter.convert(example);

        assertThat(command, is(notNullValue()));
        assertThat(command.getId(), is(example.getId()));
        assertThat(command.getRemark(), is(equalTo(example.getRemark())));
        assertThat(command.getExpression(), is(equalTo(example.getExpression())));
        assertThat(command.getExplanation(), is(equalTo(example.getExplanation())));
    }
}