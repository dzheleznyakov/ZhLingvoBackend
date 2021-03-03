package zh.lingvo.rest.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.Language;
import zh.lingvo.rest.commands.DictionaryCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Test Dictionary to DictionaryCommand converter")
class DictionaryToDictionaryCommandTest {
    private static final Long ID = 42L;
    private static final String NAME = "Test Dictionary";
    private static final Language LANGUAGE = Language.builder().name("Language").twoLetterCode("Ln").build();

    private DictionaryToDictionaryCommand converter;

    @BeforeEach
    void setUp() {
        converter = new DictionaryToDictionaryCommand(new LanguageToLanguageCommand());
    }

    @Test
    @DisplayName("Should convert null to null")
    void convertNull() {
        DictionaryCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert the dictionary to a dictionary command")
    void convertDictionary() {
        Dictionary dic = Dictionary.builder()
                .id(ID)
                .name(NAME)
                .language(LANGUAGE)
                .build();

        DictionaryCommand command = converter.convert(dic);

        assertThat(command, is(notNullValue()));
        assertThat(command.getId(), is(ID));
        assertThat(command.getName(), is(NAME));
        assertThat(command.getLanguage().getName(), is(LANGUAGE.getName()));
    }
}