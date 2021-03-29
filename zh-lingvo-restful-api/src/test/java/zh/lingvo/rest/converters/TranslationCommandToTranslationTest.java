package zh.lingvo.rest.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.data.model.Translation;
import zh.lingvo.rest.commands.TranslationCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Test TranslationCommand to Translation converter")
class TranslationCommandToTranslationTest {
    private TranslationCommandToTranslation converter;

    @BeforeEach
    void setUp() {
        converter = new TranslationCommandToTranslation();
    }

    @Test
    @DisplayName("Should convert null into null")
    void convertNull() {
        Translation translation = converter.convert(null);

        assertThat(translation, is(nullValue()));
    }

    @Test
    @DisplayName("")
    void convertTranslationCommand() {
        TranslationCommand command = TranslationCommand.builder()
                .id(42L)
                .value("command value")
                .elaboration("command elaboration")
                .build();

        Translation translation = converter.convert(command);

        assertThat(translation, is(notNullValue()));
        assertThat(translation.getId(), is(command.getId()));
        assertThat(translation.getValue(), is(equalTo(command.getValue())));
        assertThat(translation.getElaboration(), is(equalTo(command.getElaboration())));
    }
}