package zh.lingvo.rest.converters;

import com.google.common.collect.ImmutableList;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.Meaning;
import zh.lingvo.data.model.PartOfSpeech;
import zh.lingvo.data.model.SemanticBlock;
import zh.lingvo.data.repositories.PartOfSpeechRepository;
import zh.lingvo.rest.commands.SemanticBlockCommand;

import javax.annotation.Nullable;
import java.util.List;

import static zh.lingvo.util.CollectionsHelper.getListSafely;

@Component
public class SemanticBlockCommandToSemanticBlock implements Converter<SemanticBlockCommand, SemanticBlock> {
    private final MeaningCommandToMeaning meaningConverter;
    private final PartOfSpeechRepository posRepository;

    public SemanticBlockCommandToSemanticBlock(MeaningCommandToMeaning meaningConverter, PartOfSpeechRepository posRepository) {
        this.meaningConverter = meaningConverter;
        this.posRepository = posRepository;
    }

    @Override
    public SemanticBlock convert(@Nullable SemanticBlockCommand source) {
        return source == null ? null : SemanticBlock.builder()
                .id(source.getId())
                .gender(source.getGender())
                .pos(convertPos(source))
                .meanings(convertMeanings(source))
                .build();
    }

    private PartOfSpeech convertPos(SemanticBlockCommand command) {
        return posRepository.findByName(command.getPos())
                .orElseThrow(() -> new ZhLingvoConversionException("Part of speech [{}] not found", command.getPos()));
    }

    private List<Meaning> convertMeanings(SemanticBlockCommand command) {
        return getListSafely(command::getMeanings)
                .stream()
                .map(meaningConverter::convert)
                .collect(ImmutableList.toImmutableList());
    }
}
