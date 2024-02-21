package zh.lingvo.rest.converters;

import com.google.common.collect.ImmutableList;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.core.domain.PartOfSpeech;
import zh.lingvo.data.model.Meaning;
import zh.lingvo.data.model.SemanticBlock;
import zh.lingvo.rest.commands.SemanticBlockCommand;

import javax.annotation.Nullable;
import java.util.List;

import static zh.lingvo.util.CollectionsHelper.getListSafely;

@Component
public class SemanticBlockCommandToSemanticBlock implements Converter<SemanticBlockCommand, SemanticBlock> {
    private final MeaningCommandToMeaning meaningConverter;

    public SemanticBlockCommandToSemanticBlock(MeaningCommandToMeaning meaningConverter) {
        this.meaningConverter = meaningConverter;
    }

    @Override
    public SemanticBlock convert(@Nullable SemanticBlockCommand source) {
        return source == null ? null : SemanticBlock.builder()
                .id(source.getId())
                .pos(convertPos(source))
                .meanings(convertMeanings(source))
                .build();
    }

    private PartOfSpeech convertPos(SemanticBlockCommand command) {
        return PartOfSpeech.fromShortName(command.getPos());
    }

    private List<Meaning> convertMeanings(SemanticBlockCommand command) {
        return getListSafely(command::getMeanings)
                .stream()
                .map(meaningConverter::convert)
                .collect(ImmutableList.toImmutableList());
    }
}
