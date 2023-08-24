package zh.lingvo.rest.converters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.QuizRun;
import zh.lingvo.data.model.enums.MatchingRegime;
import zh.lingvo.data.model.enums.QuizRegime;
import zh.lingvo.rest.commands.QuizRunCommand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static com.google.common.base.MoreObjects.firstNonNull;

@Component
public class QuizRunCommandToQuizRun implements Converter<QuizRunCommand, QuizRun> {
    @Override
    public QuizRun convert(@Nullable QuizRunCommand source) {
        return source == null ? null : QuizRun.builder()
                .id(source.getId())
                .quizRegime(QuizRegime.fromCode(source.getQuizRegime()))
                .matchingRegime(MatchingRegime.fromCode(source.getMatchingRegime()))
                .records(source.getRecords())
                .doneRecords(getDoneRecords(source))
                .build();
    }

    private Map<Long, Boolean> getDoneRecords(@Nonnull  QuizRunCommand source) {
        return firstNonNull(source.getDoneRecords(), ImmutableList.<QuizRunCommand.DoneRecord>of())
                .stream()
                .collect(new DoneRecordsCollector());
    }

    private static class DoneRecordsCollector implements Collector<
                QuizRunCommand.DoneRecord,
                ImmutableMap.Builder<Long, Boolean>,
                Map<Long, Boolean>> {

        @Override
        public Supplier<ImmutableMap.Builder<Long, Boolean>> supplier() {
            return ImmutableMap::builder;
        }

        @Override
        public BiConsumer<ImmutableMap.Builder<Long, Boolean>, QuizRunCommand.DoneRecord> accumulator() {
            return (builder, entry) -> builder.put(entry.getRecordId(), entry.getCorrect());
        }

        @Override
        public BinaryOperator<ImmutableMap.Builder<Long, Boolean>> combiner() {
            return (b1, b2) -> b1.putAll(b2.build());
        }

        @Override
        public Function<ImmutableMap.Builder<Long, Boolean>, Map<Long, Boolean>> finisher() {
            return ImmutableMap.Builder::build;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return ImmutableSet.of(Characteristics.UNORDERED);
        }
    }
}
