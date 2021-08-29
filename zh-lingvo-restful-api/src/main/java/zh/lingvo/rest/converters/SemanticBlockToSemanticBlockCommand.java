package zh.lingvo.rest.converters;

import com.google.common.collect.ImmutableList;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.SemanticBlock;
import zh.lingvo.rest.commands.MeaningCommand;
import zh.lingvo.rest.commands.SemanticBlockCommand;

import javax.annotation.Nullable;
import java.util.List;

import static zh.lingvo.util.CollectionsHelper.getListSafely;

@Component
public class SemanticBlockToSemanticBlockCommand implements Converter<SemanticBlock, SemanticBlockCommand> {
    private final MeaningToMeaningCommand meaningConverter;

    public SemanticBlockToSemanticBlockCommand(MeaningToMeaningCommand meaningConverter) {
        this.meaningConverter = meaningConverter;
    }

    @Override
    public SemanticBlockCommand convert(@Nullable SemanticBlock source) {
        return source == null ? null : SemanticBlockCommand.builder()
                .id(source.getId())
                .pos(convertPos(source))
                .gender(source.getGender())
                .meanings(convertMeanings(source))
                .build();
    }

    private String convertPos(SemanticBlock source) {
        return source.getPos().getShortName();
    }

    private List<MeaningCommand> convertMeanings(SemanticBlock source) {
        return getListSafely(source::getMeanings)
                .stream()
                .map(meaningConverter::convert)
                .collect(ImmutableList.toImmutableList());
    }
}
