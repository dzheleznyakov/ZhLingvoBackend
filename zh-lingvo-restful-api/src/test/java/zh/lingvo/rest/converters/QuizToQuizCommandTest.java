package zh.lingvo.rest.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.rest.commands.QuizCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Test Quiz to QuizCommand converter")
class QuizToQuizCommandTest {
    private static final Long ID = 42L;
    private static final String NAME = "Test Quiz";
    private static final Language LANGUAGE = Language.builder().name("Language").twoLetterCode("Ln").build();

    private QuizToQuizCommand converter;

    @BeforeEach
    void setUp() {
        converter = new QuizToQuizCommand(new LanguageToLanguageCommand());
    }

    @Test
    @DisplayName("Should convert null to null")
    void convertNull() {
        QuizCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert quiz to quiz command")
    void convertQuiz() {
        Quiz quiz = Quiz.builder()
                .id(ID)
                .name(NAME)
                .language(LANGUAGE)
                .build();

        QuizCommand command = converter.convert(quiz);

        assertThat(command, is(notNullValue()));
        assertThat(command.getId(), is(ID));
        assertThat(command.getName(), is(NAME));
        assertThat(command.getTargetLanguage().getName(), is(LANGUAGE.getName()));
    }
}