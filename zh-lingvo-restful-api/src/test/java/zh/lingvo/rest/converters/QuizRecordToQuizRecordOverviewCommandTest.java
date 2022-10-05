package zh.lingvo.rest.converters;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.data.model.QuizRecord;
import zh.lingvo.data.model.QuizTranslation;
import zh.lingvo.rest.commands.QuizRecordOverviewCommand;
import zh.lingvo.rest.commands.TranslationCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Test QuizRecord to QuizRecordOverviewCommand converter")
class QuizRecordToQuizRecordOverviewCommandTest {
    private QuizRecordToQuizRecordOverviewCommand converter;

    @BeforeEach
    void setUp() {
        converter = new QuizRecordToQuizRecordOverviewCommand(new QuizTranslationToTranslationCommand());
    }

    @Test
    @DisplayName("Should convert null to null")
    void convertNull() {
        QuizRecordOverviewCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert a quiz record to a quiz record overview command")
    void convertQuizRecord() {
        Long id = 42L;
        String wordMainForm = "main";

        Long translationId1 = 101L;
        Long translationId2 = 102L;
        QuizTranslation quizTranslation1 = QuizTranslation.builder()
                .id(translationId1)
                .build();

        QuizTranslation quizTranslation2 = QuizTranslation.builder()
                .id(translationId2)
                .build();

        ImmutableSet<QuizTranslation> quizTranslations = ImmutableSet.of(quizTranslation1, quizTranslation2);
        QuizRecord quizRecord = QuizRecord.builder()
                .id(id)
                .wordMainForm(wordMainForm)
                .translations(quizTranslations)
                .build();

        QuizRecordOverviewCommand command = converter.convert(quizRecord);

        assertThat(command, is(notNullValue()));
        assertThat(command.getId(), is(id));
        assertThat(command.getWordMainForm(), is(wordMainForm));

        assertThat(command.getTranslations(), is(notNullValue()));
        assertThat(command.getTranslations(), hasSize(2));
        ImmutableSet<Long> expectedTranslationIds = quizTranslations.stream()
                .map(QuizTranslation::getId)
                .collect(ImmutableSet.toImmutableSet());
        ImmutableSet<Long> actualTranslationIds = command.getTranslations().stream()
                .map(TranslationCommand::getId)
                .collect(ImmutableSet.toImmutableSet());
        assertThat(actualTranslationIds, is(equalTo(expectedTranslationIds)));

    }
}