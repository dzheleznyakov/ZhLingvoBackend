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

@DisplayName("Test Language to LanguageCommand converter")
class LanguageToLanguageCommandTest {
    private LanguageToLanguageCommand converter;

    @BeforeEach
    void setUp() {
        converter = new LanguageToLanguageCommand();
    }

    @Test
    @DisplayName("Should convert null to null")
    void convertNull() {
        LanguageCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert the language to a language command")
    void convertLanguage() {
        Language lang = Language.builder()
                .id(42)
                .name("Language")
                .twoLetterCode("Ln")
                .build();

        LanguageCommand command = converter.convert(lang);

        assertThat(command, is(notNullValue()));
        assertThat(command.getId(), is(lang.getId()));
        assertThat(command.getName(), is(lang.getName()));
        assertThat(command.getCode(), is(lang.getTwoLetterCode()));
    }
}