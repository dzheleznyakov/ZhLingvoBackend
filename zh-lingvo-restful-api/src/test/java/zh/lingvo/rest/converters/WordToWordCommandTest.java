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
@DisplayName("Test Word to WordCommand converter")
class WordToWordCommandTest {
    @Mock
    private SemanticBlockToSemanticBlockCommand sbConverter;

    private WordToWordCommand converter;

    @BeforeEach
    void setUp() {
        converter = new WordToWordCommand(sbConverter);
    }

    @Test
    @DisplayName("Should convert null into null")
    void convertNull() {
        WordCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert Word into WordCommand")
    void convertWord() {
        long sbId1 = 101L;
        long sbId2 = 102L;
        SemanticBlock sBlock1 = SemanticBlock.builder().id(sbId1).build();
        SemanticBlock sBlock2 = SemanticBlock.builder().id(sbId2).build();

        Word word = Word.builder()
                .id(42L)
                .mainForm("word")
                .transcription("woh-d")
                .typeOfIrregularity("pretty irregular")
                .semanticBlocks(ImmutableList.of(sBlock1, sBlock2))
                .build();

        when(sbConverter.convert(sBlock1)).thenReturn(SemanticBlockCommand.builder().id(sbId1).build());
        when(sbConverter.convert(sBlock2)).thenReturn(SemanticBlockCommand.builder().id(sbId2).build());

        WordCommand command = converter.convert(word);

        assertThat(command, is(notNullValue()));
        assertThat(command.getId(), is(word.getId()));
        assertThat(command.getMainForm(), is(equalTo(word.getMainForm())));
        assertThat(command.getTranscription(), is(equalTo(word.getTranscription())));
        assertThat(command.getTypeOfIrregularity(), is(equalTo(word.getTypeOfIrregularity())));

        assertThat(command.getSemBlocks(), is(notNullValue()));
        assertThat(command.getSemBlocks(), hasSize(2));
        assertThat(command.getSemBlocks().get(0).getId(), is(sbId1));
        assertThat(command.getSemBlocks().get(1).getId(), is(sbId2));

        verify(sbConverter, times(2)).convert(any(SemanticBlock.class));
    }
}