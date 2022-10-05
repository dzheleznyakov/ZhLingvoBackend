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

@DisplayName("Test TranslationCommand to QuizTranslation converter")
class TranslationCommandToQuizTranslationTest {
    private TranslationCommandToQuizTranslation converter;

    @BeforeEach
    void setUp() {
        converter = new TranslationCommandToQuizTranslation();
    }

    @Test
    @DisplayName("Should convert null to null")
    void convertNull() {
        QuizTranslation quizTranslation = converter.convert(null);

        assertThat(quizTranslation, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert a translation command to a quiz translation")
    void convertTranslationCommand() {
        Long id = 42L;
        String value = "test translation";
        String elaboration = "test elaboration";

        TranslationCommand command = TranslationCommand.builder()
                .id(id)
                .value(value)
                .elaboration(elaboration)
                .build();

        QuizTranslation quizTranslation = converter.convert(command);

        assertThat(quizTranslation, is(notNullValue()));
        assertThat(quizTranslation.getId(), is(id));
        assertThat(quizTranslation.getValue(), is(value));
        assertThat(quizTranslation.getElaboration(), is(elaboration));
    }
}