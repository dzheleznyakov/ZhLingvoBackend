package zh.lingvo.rest.converters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.QuizRun;
import zh.lingvo.rest.commands.LanguageCommand;
import zh.lingvo.rest.commands.QuizRunCommand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static com.google.common.base.MoreObjects.firstNonNull;

@Component
public class QuizRunToQuizRunCommand implements Converter<QuizRun, QuizRunCommand> {
    private final LanguageToLanguageCommand languageConverter;

    public QuizRunToQuizRunCommand(LanguageToLanguageCommand languageConverter) {
        this.languageConverter = languageConverter;
    }

    @Override
    public QuizRunCommand convert(@Nullable QuizRun source) {
        return source == null ? null : QuizRunCommand.builder()
                .id(source.getId())
                .quizId(source.getQuiz().getId())
                .targetLanguage(getTargetLanguage(source))
                .quizRegime(source.getQuizRegime().getCode())
                .matchingRegime(source.getMatchingRegime().getCode())
                .ts(getTimestamp(source))
                .records(source.getRecords())
                .doneRecords(getDoneRecords(source))
                .build();
    }

    private LanguageCommand getTargetLanguage(QuizRun source) {
        Language language = source.getQuiz().getLanguage();
        return languageConverter.convert(language);
    }

    private static Long getTimestamp(QuizRun source) {
        return firstNonNull(source.getAccessedTimestamp(), source.getCreatedTimestamp());
    }

    private List<QuizRunCommand.DoneRecord> getDoneRecords(@Nonnull QuizRun source) {
        return firstNonNull(source.getDoneRecords(), ImmutableMap.<Long, Boolean>of())
                .entrySet()
                .stream()
                .collect(new DoneRecordsCollector());
    }

    private static class DoneRecordsCollector implements Collector<
                Map.Entry<Long, Boolean>,
                ImmutableList.Builder<QuizRunCommand.DoneRecord>,
                List<QuizRunCommand.DoneRecord>> {
        @Override
        public Supplier<ImmutableList.Builder<QuizRunCommand.DoneRecord>> supplier() {
            return ImmutableList::builder;
        }

        @Override
        public BiConsumer<ImmutableList.Builder<QuizRunCommand.DoneRecord>, Map.Entry<Long, Boolean>> accumulator() {
            return (builder, entry) -> builder.add(new QuizRunCommand.DoneRecord(entry.getKey(), entry.getValue()));
        }

        @Override
        public BinaryOperator<ImmutableList.Builder<QuizRunCommand.DoneRecord>> combiner() {
            return (b1, b2) -> b1.addAll(b2.build());
        }

        @Override
        public Function<ImmutableList.Builder<QuizRunCommand.DoneRecord>, List<QuizRunCommand.DoneRecord>> finisher() {
            return ImmutableList.Builder::build;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return ImmutableSet.of(Characteristics.UNORDERED);
        }
    }
}
