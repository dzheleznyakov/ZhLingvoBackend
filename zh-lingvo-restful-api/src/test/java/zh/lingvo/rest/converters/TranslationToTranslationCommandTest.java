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

@DisplayName("Test Translation to TranslationCommand converter")
class TranslationToTranslationCommandTest {
    private TranslationToTranslationCommand converter;

    @BeforeEach
    void setUp() {
        converter = new TranslationToTranslationCommand();
    }

    @Test
    @DisplayName("Should convert null into null")
    void convertNull() {
        TranslationCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert Translation into TranslationCommand")
    void convertTranslation() {
        Translation translation = Translation.builder()
                .id(42L)
                .value("mock translation")
                .elaboration("mock elaboration")
                .build();

        TranslationCommand command = converter.convert(translation);

        assertThat(command, is(notNullValue()));
        assertThat(command.getId(), is(translation.getId()));
        assertThat(command.getValue(), is(equalTo(translation.getValue())));
        assertThat(command.getElaboration(), is(equalTo(translation.getElaboration())));
    }
}