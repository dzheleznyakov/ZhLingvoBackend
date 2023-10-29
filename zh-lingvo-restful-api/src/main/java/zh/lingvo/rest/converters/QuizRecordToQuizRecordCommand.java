package zh.lingvo.rest.converters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.QuizExample;
import zh.lingvo.data.model.QuizRecord;
import zh.lingvo.data.model.QuizTranslation;
import zh.lingvo.rest.commands.ExampleCommand;
import zh.lingvo.rest.commands.QuizRecordCommand;
import zh.lingvo.rest.commands.TranslationCommand;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.MoreObjects.firstNonNull;

@Component
public class QuizRecordToQuizRecordCommand implements Converter<QuizRecord, QuizRecordCommand> {
    private final QuizTranslationToTranslationCommand translationConverter;
    private final QuizExampleToExampleCommand exampleConverter;

    public QuizRecordToQuizRecordCommand(
            QuizTranslationToTranslationCommand translationConverter,
            QuizExampleToExampleCommand exampleConverter
    ) {
        this.translationConverter = translationConverter;
        this.exampleConverter = exampleConverter;
    }

    @Override
    public QuizRecordCommand convert(@Nullable QuizRecord source) {
        return source == null ? null : QuizRecordCommand.builder()
                .id(source.getId())
                .wordMainForm(source.getWordMainForm())
                .pos(source.getPos().getShortName())
                .transcription(source.getTranscription())
                .currentScore(source.getCurrentScore())
                .numberOfRuns(source.getNumberOfRuns())
                .numberOfSuccesses(source.getNumberOfSuccesses())
                .translations(convertTranslations(source))
                .examples(convertExamples(source))
                .build();
    }

    private List<TranslationCommand> convertTranslations(QuizRecord source) {
        return firstNonNull(source.getTranslations(), ImmutableSet.<QuizTranslation>of())
                .stream()
                .map(translationConverter::convert)
                .collect(ImmutableList.toImmutableList());
    }

    private List<ExampleCommand> convertExamples(QuizRecord source) {
        return firstNonNull(source.getExamples(), ImmutableSet.<QuizExample>of())
                .stream()
                .map(exampleConverter::convert)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(ExampleCommand::getId))
                .collect(ImmutableList.toImmutableList());
    }
}
