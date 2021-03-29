package zh.lingvo.rest.converters;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zh.lingvo.data.model.Example;
import zh.lingvo.data.model.Meaning;
import zh.lingvo.data.model.Translation;
import zh.lingvo.rest.commands.ExampleCommand;
import zh.lingvo.rest.commands.MeaningCommand;
import zh.lingvo.rest.commands.TranslationCommand;

import java.util.Iterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test MeaningCommand to Meaning converter")
class MeaningCommandToMeaningTest {
    @Mock
    private TranslationCommandToTranslation translationConverter;

    @Mock
    private ExampleCommandToExample exampleConverter;

    private MeaningCommandToMeaning converter;

    @BeforeEach
    void setUp() {
        converter = new MeaningCommandToMeaning(translationConverter, exampleConverter);
    }

    @Test
    @DisplayName("Should convert null into null")
    void convertNull() {
        Meaning meaning = converter.convert(null);

        assertThat(meaning, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert MeaningCommand into Meaning")
    void convertMeaningCommand() {
        long translationId1 = 101L;
        long translationId2 = 102L;
        long exampleId1 = 201L;
        long exampleId2 = 202L;

        TranslationCommand tCommand1 = TranslationCommand.builder().id(translationId1).build();
        TranslationCommand tCommand2 = TranslationCommand.builder().id(translationId2).build();
        ExampleCommand eCommand1 = ExampleCommand.builder().id(exampleId1).build();
        ExampleCommand eCommand2 = ExampleCommand.builder().id(exampleId2).build();

        when(translationConverter.convert(tCommand1))
                .thenReturn(Translation.builder().id(translationId1).build());
        when(translationConverter.convert(tCommand2))
                .thenReturn(Translation.builder().id(translationId2).build());
        when(exampleConverter.convert(eCommand1))
                .thenReturn(Example.builder().id(exampleId1).build());
        when(exampleConverter.convert(eCommand2))
                .thenReturn(Example.builder().id(exampleId2).build());

        MeaningCommand command = MeaningCommand.builder()
                .id(42L)
                .remark("meaning remark")
                .translations(ImmutableList.of(tCommand1, tCommand2))
                .examples(ImmutableList.of(eCommand1, eCommand2))
                .build();

        Meaning meaning = converter.convert(command);

        assertThat(meaning, is(notNullValue()));
        assertThat(meaning.getId(), is(command.getId()));
        assertThat(meaning.getRemark(), is(command.getRemark()));

        assertThat(meaning.getTranslations(), is(notNullValue()));
        assertThat(meaning.getTranslations(), hasSize(2));
        Iterator<Translation> translations = meaning.getTranslations().iterator();
        assertThat(translations.next().getId(), is(translationId1));
        assertThat(translations.next().getId(), is(translationId2));

        assertThat(meaning.getExamples(), is(notNullValue()));
        assertThat(meaning.getExamples(), hasSize(2));
        Iterator<Example> examples = meaning.getExamples().iterator();
        assertThat(examples.next().getId(), is(exampleId1));
        assertThat(examples.next().getId(), is(exampleId2));

        verify(translationConverter, times(2)).convert(any(TranslationCommand.class));
        verify(exampleConverter, times(2)).convert(any(ExampleCommand.class));
    }
}