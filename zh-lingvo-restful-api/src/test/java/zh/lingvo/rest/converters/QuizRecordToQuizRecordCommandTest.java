package zh.lingvo.rest.converters;

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

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Test QuizRecord to QuizRecordCommand converter")
class QuizRecordToQuizRecordCommandTest {
    private QuizRecordToQuizRecordCommand converter;

    @BeforeEach
    void setUp() {
        converter = new QuizRecordToQuizRecordCommand(
                new QuizTranslationToTranslationCommand(),
                new QuizExampleToExampleCommand());
    }

    @Test
    @DisplayName("Should convert null to null")
    void convertNull() {
        QuizRecordCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert a quiz record to a quiz record command")
    void convertQuizRecord() {
        Long translationId1 = 101L;
        Long translationId2 = 102L;
        QuizTranslation quizTranslation1 = QuizTranslation.builder().id(translationId1).build();
        QuizTranslation quizTranslation2 = QuizTranslation.builder().id(translationId2).build();

        Long exampleId1 = 201L;
        Long exampleId2 = 202L;
        QuizExample quizExample1 = QuizExample.builder().id(exampleId1).build();
        QuizExample quizExample2 = QuizExample.builder().id(exampleId2).build();

        Long id = 42L;
        String wordMainForm = "form";
        PartOfSpeech pos = PartOfSpeech.NOUN;
        String transcription = "test transcription";
        Integer numberOfRuns = 10;
        Integer numberOfSuccesses = 5;
        Set<QuizTranslation> translations = ImmutableSet.of(quizTranslation1, quizTranslation2);
        Set<QuizExample> examples = ImmutableSet.of(quizExample1, quizExample2);
        QuizRecord quizRecord = QuizRecord.builder()
                .id(id)
                .wordMainForm(wordMainForm)
                .pos(pos)
                .transcription(transcription)
                .numberOfRuns(numberOfRuns)
                .numberOfSuccesses(numberOfSuccesses)
                .translations(translations)
                .examples(examples)
                .build();

        QuizRecordCommand command = converter.convert(quizRecord);

        assertThat(command, is(notNullValue()));
        assertThat(command.getId(), is(id));
        assertThat(command.getWordMainForm(), is(wordMainForm));
        assertThat(command.getPos(), is(pos.getShortName()));
        assertThat(command.getTranscription(), is(transcription));
        assertThat(command.getNumberOfRuns(), is(numberOfRuns));
        assertThat(command.getNumberOfSuccesses(), is(numberOfSuccesses));

        assertThat(command.getTranslations(), is(notNullValue()));
        Set<Long> expectedTranslationIds = ImmutableSet.of(translationId1, translationId2);
        Set<Long> actualTranslationIds = command.getTranslations().stream()
                .map(TranslationCommand::getId)
                .collect(ImmutableSet.toImmutableSet());
        assertThat(actualTranslationIds, is(equalTo(expectedTranslationIds)));

        assertThat(command.getExamples(), is(notNullValue()));
        Set<Long> expectedExampleIds = ImmutableSet.of(exampleId1, exampleId2);
        Set<Long> actualExampleIds = command.getExamples().stream()
                .map(ExampleCommand::getId)
                .collect(ImmutableSet.toImmutableSet());
        assertThat(actualExampleIds, is(equalTo(expectedExampleIds)));
    }
}