package zh.lingvo.rest.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.data.model.QuizTranslation;
import zh.lingvo.rest.commands.TranslationCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Test QuizTranslation to TranslationCommand converter")
class QuizTranslationToTranslationCommandTest {
    private QuizTranslationToTranslationCommand converter;

    @BeforeEach
    void setUp() {
        converter = new QuizTranslationToTranslationCommand();
    }

    @Test
    @DisplayName("Convert null to null")
    void convertNull() {
        TranslationCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert QuizTranslation to TranslationCommand")
    void convertQuizTranslation() {
        Long id = 42L;
        String value = "test translation";
        String elaboration = "test elaboration";
        QuizTranslation quizTranslation = QuizTranslation.builder()
                .id(id)
                .value(value)
                .elaboration(elaboration)
                .build();

        TranslationCommand command = converter.convert(quizTranslation);

        assertThat(command, is(notNullValue()));
        assertThat(command.getId(), is(id));
        assertThat(command.getValue(), is(value));
        assertThat(command.getElaboration(), is(elaboration));
    }
}