package zh.lingvo.rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.Language;
import zh.lingvo.rest.commands.LanguageCommand;

import javax.annotation.Nullable;

@Component
public class LanguageToLanguageCommand implements Converter<Language, LanguageCommand> {
    @Override
    public LanguageCommand convert(@Nullable Language source) {
        return source == null ? null : LanguageCommand.builder()
                .id(source.getId())
                .name(source.getName())
                .code(source.getTwoLetterCode())
                .build();
    }
}
