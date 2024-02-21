package zh.lingvo.rest.converters;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zh.lingvo.data.model.Meaning;
import zh.lingvo.data.model.SemanticBlock;
import zh.lingvo.rest.commands.MeaningCommand;
import zh.lingvo.rest.commands.SemanticBlockCommand;

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
import static zh.lingvo.core.domain.PartOfSpeech.NOUN;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test SemanticBlock to SemanticBlockCommand converter")
class SemanticBlockToSemanticBlockCommandTest {
    @Mock
    private MeaningToMeaningCommand meaningConverter;

    private SemanticBlockToSemanticBlockCommand converter;

    @BeforeEach
    void setUp() {
        converter = new SemanticBlockToSemanticBlockCommand(meaningConverter);
    }

    @Test
    @DisplayName("Should convert null into null")
    void convertNull() {
        SemanticBlockCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert SemanticBlock into SemanticBlockCommand")
    void convertSemanticBlock() {
        Long meaningId1 = 101L;
        Long meaningId2 = 102L;

        Meaning meaning1 = Meaning.builder().id(meaningId1).build();
        Meaning meaning2 = Meaning.builder().id(meaningId2).build();

        when(meaningConverter.convert(meaning1))
                .thenReturn(MeaningCommand.builder().id(meaningId1).build());
        when(meaningConverter.convert(meaning2))
                .thenReturn(MeaningCommand.builder().id(meaningId2).build());

        SemanticBlock sBlock = SemanticBlock.builder()
                .id(42L)
                .pos(NOUN)
                .meanings(ImmutableList.of(meaning1, meaning2))
                .build();

        SemanticBlockCommand command = converter.convert(sBlock);

        assertThat(command, is(notNullValue()));
        assertThat(command.getId(), is(sBlock.getId()));
        assertThat(command.getPos(), is(equalTo(NOUN.getShortName())));

        assertThat(command.getMeanings(), is(notNullValue()));
        assertThat(command.getMeanings(), hasSize(2));
        assertThat(command.getMeanings().get(0).getId(), is(meaningId1));
        assertThat(command.getMeanings().get(1).getId(), is(meaningId2));

        verify(meaningConverter, times(2)).convert(any(Meaning.class));
    }
}