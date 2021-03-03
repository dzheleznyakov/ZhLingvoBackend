package zh.lingvo.rest.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.data.model.Language;
import zh.lingvo.rest.commands.LanguageCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Test LanguageCommand to Language converter")
class LanguageCommandToLanguageTest {
    private LanguageCommandToLanguage converter;

    @BeforeEach
    void setUp() {
        converter = new LanguageCommandToLanguage();
    }

    @Test
    @DisplayName("Should convert null to null")
    void convertNull() {
        Language language = converter.convert(null);

        assertThat(language, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert the language command to a language")
    void convertLanguage() {
        LanguageCommand command = LanguageCommand.builder()
                .id(42)
                .name("Language")
                .code("Ln")
                .build();

        Language language = converter.convert(command);

        assertThat(language, is(notNullValue()));
        assertThat(language.getId(), is(command.getId()));
        assertThat(language.getName(), is(command.getName()));
        assertThat(language.getTwoLetterCode(), is(command.getCode()));

    }
}