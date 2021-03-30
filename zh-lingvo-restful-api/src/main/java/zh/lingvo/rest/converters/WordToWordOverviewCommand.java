package zh.lingvo.rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.Word;
import zh.lingvo.rest.commands.WordOverviewCommand;

import javax.annotation.Nullable;

@Component
public class WordToWordOverviewCommand implements Converter<Word, WordOverviewCommand> {
    @Override
    public WordOverviewCommand convert(@Nullable Word source) {
        return source == null ? null : WordOverviewCommand.builder()
                .id(source.getId())
                .mainForm(source.getMainForm())
                .build();
    }
}
