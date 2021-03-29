package zh.lingvo.rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.Example;
import zh.lingvo.data.model.Meaning;
import zh.lingvo.data.model.Translation;
import zh.lingvo.rest.commands.MeaningCommand;

import javax.annotation.Nullable;
import java.util.Set;

import static zh.lingvo.util.CollectionsHelper.getListSafely;
import static zh.lingvo.util.CollectionsHelper.toLinkedHashSet;

@Component
public class MeaningCommandToMeaning implements Converter<MeaningCommand, Meaning> {
    private final TranslationCommandToTranslation translationConverter;
    private final ExampleCommandToExample exampleConverter;

    public MeaningCommandToMeaning(TranslationCommandToTranslation translationConverter, ExampleCommandToExample exampleConverter) {
        this.translationConverter = translationConverter;
        this.exampleConverter = exampleConverter;
    }

    @Override
    public Meaning convert(@Nullable MeaningCommand source) {
        return source == null ? null : Meaning.builder()
                .id(source.getId())
                .remark(source.getRemark())
                .translations(convertTranslations(source))
                .examples(convertExamples(source))
                .build();
    }

    private Set<Translation> convertTranslations(MeaningCommand command) {
        return getListSafely(command::getTranslations)
                .stream()
                .map(translationConverter::convert)
                .collect(toLinkedHashSet());
    }

    private Set<Example> convertExamples(MeaningCommand command) {
        return getListSafely(command::getExamples)
                .stream()
                .map(exampleConverter::convert)
                .collect(toLinkedHashSet());
    }
}
