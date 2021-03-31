package zh.lingvo.rest.converters;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zh.lingvo.data.model.SemanticBlock;
import zh.lingvo.data.model.Word;
import zh.lingvo.rest.commands.SemanticBlockCommand;
import zh.lingvo.rest.commands.WordCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test WordCommand to Word converter")
class WordCommandToWordTest {
    @Mock
    private SemanticBlockCommandToSemanticBlock sbConverter;

    private WordCommandToWord converter;

    @BeforeEach
    void setUp() {
        converter = new WordCommandToWord(sbConverter);
    }

    @Test
    @DisplayName("Should convert null into null")
    void convertNull() {
        Word word = converter.convert(null);

        assertThat(word, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert WordCommand")
    void convertWordCommand() {
        long sbId1 = 101L;
        long sbId2 = 102L;

        SemanticBlockCommand sbCommand1 = SemanticBlockCommand.builder().id(sbId1).build();
        SemanticBlockCommand sbCommand2 = SemanticBlockCommand.builder().id(sbId2).build();

        WordCommand command = WordCommand.builder()
                .id(42L)
                .mainForm("word")
                .transcription("woh-d")
                .typeOfIrregularity("somewhat irregular")
                .semBlocks(ImmutableList.of(sbCommand1, sbCommand2))
                .build();

        when(sbConverter.convert(sbCommand1))
                .thenReturn(SemanticBlock.builder().id(sbId1).build());
        when(sbConverter.convert(sbCommand2))
                .thenReturn(SemanticBlock.builder().id(sbId2).build());

        Word word = converter.convert(command);

        assertThat(word, is(notNullValue()));
        assertThat(word.getId(), is(command.getId()));
        assertThat(word.getMainForm(), is(equalTo(command.getMainForm())));
        assertThat(word.getTranscription(), is(equalTo(command.getTranscription())));
        assertThat(word.getTypeOfIrregularity(), is(equalTo(command.getTypeOfIrregularity())));

        assertThat(word.getSemanticBlocks(), is(notNullValue()));
        assertThat(word.getSemanticBlocks(), hasSize(2));
        assertThat(word.getSemanticBlocks().get(0).getId(), is(sbId1));
        assertThat(word.getSemanticBlocks().get(1).getId(), is(sbId2));

        verify(sbConverter, times(2)).convert(any(SemanticBlockCommand.class));
    }
}