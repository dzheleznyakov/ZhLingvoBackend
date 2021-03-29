package zh.lingvo.rest.converters;

import com.google.common.collect.ImmutableList;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.Meaning;
import zh.lingvo.rest.commands.ExampleCommand;
import zh.lingvo.rest.commands.MeaningCommand;
import zh.lingvo.rest.commands.TranslationCommand;

import javax.annotation.Nullable;
import java.util.List;

import static zh.lingvo.util.CollectionsHelper.getSetSafely;

@Component
public class MeaningToMeaningCommand implements Converter<Meaning, MeaningCommand> {
    private final TranslationToTranslationCommand translationConverter;
    private final ExampleToExampleCommand exampleConverter;

    public MeaningToMeaningCommand(TranslationToTranslationCommand translationConverter, ExampleToExampleCommand exampleConverter) {
        this.translationConverter = translationConverter;
        this.exampleConverter = exampleConverter;
    }

    @Override
    public MeaningCommand convert(@Nullable Meaning source) {
        return source == null ? null : MeaningCommand.builder()
                .id(source.getId())
                .remark(source.getRemark())
                .translations(convertTranslations(source))
                .examples(convertExamples(source))
                .build();
    }

    private List<TranslationCommand> convertTranslations(Meaning meaning) {
        return getSetSafely(meaning::getTranslations)
                .stream()
                .map(translationConverter::convert)
                .collect(ImmutableList.toImmutableList());
    }

    private List<ExampleCommand> convertExamples(Meaning meaning) {
        return getSetSafely(meaning::getExamples)
                .stream()
                .map(exampleConverter::convert)
                .collect(ImmutableList.toImmutableList());
    }
}
