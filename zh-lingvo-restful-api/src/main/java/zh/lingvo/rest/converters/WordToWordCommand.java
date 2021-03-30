package zh.lingvo.rest.converters;

import com.google.common.collect.ImmutableList;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.Word;
import zh.lingvo.rest.commands.SemanticBlockCommand;
import zh.lingvo.rest.commands.WordCommand;

import javax.annotation.Nullable;
import java.util.List;

import static zh.lingvo.util.CollectionsHelper.getListSafely;

@Component
public class WordToWordCommand implements Converter<Word, WordCommand> {
    private final SemanticBlockToSemanticBlockCommand sbConverter;

    public WordToWordCommand(SemanticBlockToSemanticBlockCommand sbConverter) {
        this.sbConverter = sbConverter;
    }

    @Override
    public WordCommand convert(@Nullable Word source) {
        return source == null ? null : WordCommand.builder()
                .id(source.getId())
                .mainForm(source.getMainForm())
                .transcription(source.getTranscription())
                .typeOfIrregularity(source.getTypeOfIrregularity())
                .semBlocks(convertSemanticBlocks(source))
                .build();
    }

    private List<SemanticBlockCommand> convertSemanticBlocks(Word word) {
        return getListSafely(word::getSemanticBlocks)
                .stream()
                .map(sbConverter::convert)
                .collect(ImmutableList.toImmutableList());
    }
}
