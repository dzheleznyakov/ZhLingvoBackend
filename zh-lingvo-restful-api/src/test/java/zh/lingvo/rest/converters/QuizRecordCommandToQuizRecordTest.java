package zh.lingvo.rest.converters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.core.domain.PartOfSpeech;
import zh.lingvo.data.model.QuizExample;
import zh.lingvo.data.model.QuizRecord;
import zh.lingvo.data.model.QuizTranslation;
import zh.lingvo.rest.commands.ExampleCommand;
import zh.lingvo.rest.commands.QuizRecordCommand;
import zh.lingvo.rest.commands.TranslationCommand;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Test QuizRecordCommand to QuizRecord converter")
class QuizRecordCommandToQuizRecordTest {
    private QuizRecordCommandToQuizRecord converter;

    @BeforeEach
    void setUp() {
        converter = new QuizRecordCommandToQuizRecord(
                new TranslationCommandToQuizTranslation(),
                new ExampleCommandToQuizExample());
    }

    @Test
    @DisplayName("Should convert null to null")
    void convertNull() {
        QuizRecord quizRecord = converter.convert(null);

        assertThat(quizRecord, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert a quiz record command to a quiz record")
    void convertQuizRecordCommand() {
        Long translationId1 = 101L;
        Long translationId2 = 102L;
        TranslationCommand translationCommand1 = TranslationCommand.builder().id(translationId1).build();
        TranslationCommand translationCommand2 = TranslationCommand.builder().id(translationId2).build();

        Long exampleId1 = 201L;
        Long exampleId2 = 202L;
        ExampleCommand exampleCommand1 = ExampleCommand.builder().id(exampleId1).build();
        ExampleCommand exampleCommand2 = ExampleCommand.builder().id(exampleId2).build();

        Long id = 42L;
        String wordMainForm = "word";
        String pos = "n";
        String transcription = "wəːd";
        Integer numberOfRuns = 10;
        Integer numberOfSuccesses = 5;
        List<TranslationCommand> translations = ImmutableList.of(translationCommand1, translationCommand2);
        List<ExampleCommand> examples = ImmutableList.of(exampleCommand1, exampleCommand2);
        QuizRecordCommand command = QuizRecordCommand.builder()
                .id(id)
                .wordMainForm(wordMainForm)
                .pos(pos)
                .transcription(transcription)
                .numberOfRuns(numberOfRuns)
                .numberOfSuccesses(numberOfSuccesses)
                .translations(translations)
                .examples(examples)
                .build();

        QuizRecord quizRecord = converter.convert(command);

        assertThat(quizRecord, is(notNullValue()));
        assertThat(quizRecord.getId(), is(id));
        assertThat(quizRecord.getWordMainForm(), is(wordMainForm));
        assertThat(quizRecord.getPos(), is(PartOfSpeech.NOUN));
        assertThat(quizRecord.getTranscription(), is(transcription));
        assertThat(quizRecord.getNumberOfRuns(), is(numberOfRuns));
        assertThat(quizRecord.getNumberOfSuccesses(), is(numberOfSuccesses));

        assertThat(quizRecord.getTranslations(), is(notNullValue()));
        Set<Long> expectedTranslationIds = ImmutableSet.of(translationId1, translationId2);
        Set<Long> actualTranslationIds = quizRecord.getTranslations()
                .stream()
                .map(QuizTranslation::getId)
                .collect(ImmutableSet.toImmutableSet());
        assertThat(actualTranslationIds, is(equalTo(expectedTranslationIds)));

        assertThat(quizRecord.getExamples(), is(notNullValue()));
        Set<Long> expectedExampleIds = ImmutableSet.of(exampleId1, exampleId2);
        Set<Long> actualExampleIds = quizRecord.getExamples()
                .stream()
                .map(QuizExample::getId)
                .collect(ImmutableSet.toImmutableSet());
        assertThat(actualExampleIds, is(equalTo(expectedExampleIds)));
    }
}