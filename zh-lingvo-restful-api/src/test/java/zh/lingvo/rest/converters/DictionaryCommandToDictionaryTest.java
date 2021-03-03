package zh.lingvo.rest.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.rest.commands.DictionaryCommand;
import zh.lingvo.rest.commands.LanguageCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Test DictionaryCommand to Dictionary converter")
class DictionaryCommandToDictionaryTest {
    private DictionaryCommandToDictionary converter;

    @BeforeEach
    void setUp() {
        converter = new DictionaryCommandToDictionary(new LanguageCommandToLanguage());
    }

    @Test
    @DisplayName("Should convert null to null")
    void convertNull() {
        Dictionary dictionary = converter.convert(null);

        assertThat(dictionary, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert the dictionary command to dictionary")
    void convertDictionaryCommand() {
        LanguageCommand languageCommand = LanguageCommand.builder()
                .id(33)
                .name("Language")
                .code("Ln")
                .build();
        DictionaryCommand dictionaryCommand = DictionaryCommand.builder()
                .id(42L)
                .name("My Dictionary")
                .language(languageCommand)
                .build();

        Dictionary dictionary = converter.convert(dictionaryCommand);

        assertThat(dictionary, is(notNullValue()));
        assertThat(dictionary.getId(), is(dictionaryCommand.getId()));
        assertThat(dictionary.getName(), is(dictionaryCommand.getName()));
        assertThat(dictionary.getLanguage().getId(), is(languageCommand.getId()));
    }
}