package zh.lingvo.rest.converters;

import com.google.common.collect.ImmutableSet;
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
@DisplayName("Test Meaning to MeaningCommand converter")
class MeaningToMeaningCommandTest {
    @Mock
    private TranslationToTranslationCommand translationConverter;

    @Mock
    private ExampleToExampleCommand exampleConverter;

    private MeaningToMeaningCommand converter;

    @BeforeEach
    void setUp() {
        converter = new MeaningToMeaningCommand(translationConverter, exampleConverter);
    }

    @Test
    @DisplayName("Should convert null into null")
    void convertNull() {
        MeaningCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert Meaning into MeaningCommand")
    void convertMeaning() {
        Long translation1Id = 101L;
        Long translation2Id = 102L;
        Long example1Id = 201L;
        Long example2Id = 202L;

        Translation translation1 = Translation.builder().id(translation1Id).build();
        Translation translation2 = Translation.builder().id(translation2Id).build();
        Example example1 = Example.builder().id(example1Id).build();
        Example example2 = Example.builder().id(example2Id).build();

        Meaning meaning = Meaning.builder()
                .id(42L)
                .remark("meaning remark")
                .translations(ImmutableSet.of(translation1, translation2))
                .examples(ImmutableSet.of(example1, example2))
                .build();

        when(translationConverter.convert(translation1))
                .thenReturn(TranslationCommand.builder().id(translation1Id).build());
        when(translationConverter.convert(translation2))
                .thenReturn(TranslationCommand.builder().id(translation2Id).build());
        when(exampleConverter.convert(example1))
                .thenReturn(ExampleCommand.builder().id(example1Id).build());
        when(exampleConverter.convert(example2))
                .thenReturn(ExampleCommand.builder().id(example2Id).build());

        MeaningCommand command = converter.convert(meaning);

        assertThat(command, is(notNullValue()));
        assertThat(command.getId(), is(meaning.getId()));
        assertThat(command.getRemark(), is(equalTo(meaning.getRemark())));

        assertThat(command.getTranslations(), is(notNullValue()));
        assertThat(command.getTranslations(), hasSize(2));
        assertThat(command.getTranslations().get(0).getId(), is(translation1Id));
        assertThat(command.getTranslations().get(1).getId(), is(translation2Id));

        assertThat(command.getExamples(), is(notNullValue()));
        assertThat(command.getExamples(), hasSize(2));
        assertThat(command.getExamples().get(0).getId(), is(example1Id));
        assertThat(command.getExamples().get(1).getId(), is(example2Id));

        verify(translationConverter, times(2)).convert(any(Translation.class));
        verify(exampleConverter, times(2)).convert(any(Example.class));
    }
}