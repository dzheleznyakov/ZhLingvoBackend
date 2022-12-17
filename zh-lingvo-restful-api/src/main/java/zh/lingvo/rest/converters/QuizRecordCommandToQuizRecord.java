package zh.lingvo.rest.converters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.core.domain.PartOfSpeech;
import zh.lingvo.data.model.QuizExample;
import zh.lingvo.data.model.QuizRecord;
import zh.lingvo.data.model.QuizTranslation;
import zh.lingvo.rest.commands.ExampleCommand;
import zh.lingvo.rest.commands.QuizRecordCommand;
import zh.lingvo.rest.commands.TranslationCommand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

import static com.google.common.base.MoreObjects.firstNonNull;

@Component
public class QuizRecordCommandToQuizRecord implements Converter<QuizRecordCommand, QuizRecord> {
    private final TranslationCommandToQuizTranslation translationConverter;
    private final ExampleCommandToQuizExample exampleConverter;

    public QuizRecordCommandToQuizRecord(
            TranslationCommandToQuizTranslation translationConverter,
            ExampleCommandToQuizExample exampleConverter
    ) {
        this.translationConverter = translationConverter;
        this.exampleConverter = exampleConverter;
    }

    @Override
    public QuizRecord convert(@Nullable QuizRecordCommand source) {
        if (source == null)
            return null;
        QuizRecord quizRecord = QuizRecord.builder()
                .id(source.getId())
                .wordMainForm(source.getWordMainForm())
                .pos(convertPos(source))
                .transcription(source.getTranscription())
                .currentScore(source.getCurrentScore())
                .numberOfRuns(source.getNumberOfRuns())
                .numberOfSuccesses(source.getNumberOfSuccesses())
                .translations(convertTranslations(source))
                .examples(convertExamples(source))
                .build();
        quizRecord.getTranslations()
                .forEach(qt -> qt.setRecord(quizRecord));
        quizRecord.getExamples()
                .forEach(qe -> qe.setRecord(quizRecord));
        return quizRecord;
    }

    private PartOfSpeech convertPos(QuizRecordCommand source) {
        return PartOfSpeech.fromShortName(source.getPos());
    }

    private Set<QuizTranslation> convertTranslations(@Nonnull QuizRecordCommand source) {
        return firstNonNull(source.getTranslations(), ImmutableList.<TranslationCommand>of())
                .stream()
                .map(translationConverter::convert)
                .collect(ImmutableSet.toImmutableSet());
    }

    private Set<QuizExample> convertExamples(QuizRecordCommand source) {
        return firstNonNull(source.getExamples(), ImmutableList.<ExampleCommand>of())
                .stream()
                .map(exampleConverter::convert)
                .collect(ImmutableSet.toImmutableSet());
    }
}
