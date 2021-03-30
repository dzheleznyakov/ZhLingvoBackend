package zh.lingvo.rest.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.data.model.Word;
import zh.lingvo.rest.commands.WordOverviewCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Test Word to WordOverviewCommand conversion")
class WordToWordOverviewCommandTest {
    private WordToWordOverviewCommand converter;

    @BeforeEach
    void setUp() {
        converter = new WordToWordOverviewCommand();
    }

    @Test
    @DisplayName("Should convert null into null")
    void convertNull() {
        WordOverviewCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert Word into WordOverviewCommand")
    void convertWord() {
        Word word = Word.builder()
                .id(42L)
                .mainForm("word")
                .build();

        WordOverviewCommand command = converter.convert(word);

        assertThat(command, is(notNullValue()));
        assertThat(command.getId(), is(word.getId()));
        assertThat(command.getMainForm(), is(word.getMainForm()));
    }
}