package zh.lingvo.rest.converters;

import com.google.common.collect.ImmutableList;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.SemanticBlock;
import zh.lingvo.data.model.Word;
import zh.lingvo.rest.commands.WordCommand;

import javax.annotation.Nullable;
import java.util.List;

import static zh.lingvo.util.CollectionsHelper.getListSafely;

@Component
public class WordCommandToWord implements Converter<WordCommand, Word> {
    private final SemanticBlockCommandToSemanticBlock sbConverter;

    public WordCommandToWord(SemanticBlockCommandToSemanticBlock sbConverter) {
        this.sbConverter = sbConverter;
    }

    @Override
    public Word convert(@Nullable WordCommand source) {
        return source == null ? null : Word.builder()
                .id(source.getId())
                .mainForm(source.getMainForm())
                .transcription(source.getTranscription())
                .typeOfIrregularity(source.getTypeOfIrregularity())
                .semanticBlocks(convertSemanticBlocks(source))
                .build();
    }

    private List<SemanticBlock> convertSemanticBlocks(WordCommand command) {
        return getListSafely(command::getSemBlocks)
                .stream()
                .map(sbConverter::convert)
                .collect(ImmutableList.toImmutableList());
    }
}
