package zh.lingvo.rest.converters;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zh.lingvo.core.domain.PartOfSpeech;
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

@ExtendWith(MockitoExtension.class)
@DisplayName("Test SemanticBlockCommand to SemanticBlock converter")
class SemanticBlockCommandToSemanticBlockTest {
    @Mock
    private MeaningCommandToMeaning meaningConverter;

    private SemanticBlockCommandToSemanticBlock converter;

    @BeforeEach
    void setUp() {
        converter = new SemanticBlockCommandToSemanticBlock(meaningConverter);
    }

    @Test
    @DisplayName("Should convert null into null")
    void convertNull() {
        SemanticBlock sb = converter.convert(null);

        assertThat(sb, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert SemanticBlockCommand into SemanticBlock")
    void convertSemanticBlockCommand() {
        long meaningId1 = 101L;
        long meaningId2 = 102L;
        String posName = "n";

        MeaningCommand mCommand1 = MeaningCommand.builder().id(meaningId1).build();
        MeaningCommand mCommand2 = MeaningCommand.builder().id(meaningId2).build();

        when(meaningConverter.convert(mCommand1))
                .thenReturn(Meaning.builder().id(meaningId1).build());
        when(meaningConverter.convert(mCommand2))
                .thenReturn(Meaning.builder().id(meaningId2).build());

        SemanticBlockCommand sbCommand = SemanticBlockCommand.builder()
                .id(42L)
                .pos(posName)
                .gender("f")
                .meanings(ImmutableList.of(mCommand1, mCommand2))
                .build();

        SemanticBlock sBlock = converter.convert(sbCommand);

        assertThat(sBlock, is(notNullValue()));
        assertThat(sBlock.getId(), is(sbCommand.getId()));
        assertThat(sBlock.getPos(), is(equalTo(PartOfSpeech.NOUN)));
        assertThat(sBlock.getGender(), is(equalTo(sbCommand.getGender())));

        assertThat(sBlock.getMeanings(), is(notNullValue()));
        assertThat(sBlock.getMeanings(), hasSize(2));
        assertThat(sBlock.getMeanings().get(0).getId(), is(meaningId1));
        assertThat(sBlock.getMeanings().get(1).getId(), is(meaningId2));

        verify(meaningConverter, times(2)).convert(any(MeaningCommand.class));
    }
}