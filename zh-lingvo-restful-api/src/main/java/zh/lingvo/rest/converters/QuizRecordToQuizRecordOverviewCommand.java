package zh.lingvo.rest.converters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.QuizRecord;
import zh.lingvo.data.model.QuizTranslation;
import zh.lingvo.rest.commands.QuizRecordOverviewCommand;
import zh.lingvo.rest.commands.TranslationCommand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.google.common.base.MoreObjects.firstNonNull;

@Component
public class QuizRecordToQuizRecordOverviewCommand implements Converter<QuizRecord, QuizRecordOverviewCommand> {
    private final QuizTranslationToTranslationCommand translationConverter;

    public QuizRecordToQuizRecordOverviewCommand(QuizTranslationToTranslationCommand translationConverter) {
        this.translationConverter = translationConverter;
    }

    @Override
    public QuizRecordOverviewCommand convert(@Nullable QuizRecord source) {
        return source == null ? null : QuizRecordOverviewCommand.builder()
                .id(source.getId())
                .wordMainForm(source.getWordMainForm())
                .currentScore(source.getCurrentScore())
                .translations(convertTranslations(source))
                .build();
    }

    private List<TranslationCommand> convertTranslations(@Nonnull QuizRecord source) {
        return firstNonNull(source.getTranslations(), ImmutableSet.<QuizTranslation>of())
                .stream()
                .map(translationConverter::convert)
                .collect(ImmutableList.toImmutableList());
    }
}
