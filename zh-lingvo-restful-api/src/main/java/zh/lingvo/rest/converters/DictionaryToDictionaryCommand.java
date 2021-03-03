package zh.lingvo.rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.rest.commands.DictionaryCommand;

import javax.annotation.Nullable;

@Component
public class DictionaryToDictionaryCommand implements Converter<Dictionary, DictionaryCommand> {
    private final LanguageToLanguageCommand languageConverter;

    public DictionaryToDictionaryCommand(LanguageToLanguageCommand languageConverter) {
        this.languageConverter = languageConverter;
    }

    @Override
    public DictionaryCommand convert(@Nullable Dictionary source) {
        return source == null ? null : DictionaryCommand.builder()
                .id(source.getId())
                .name(source.getName())
                .language(languageConverter.convert(source.getLanguage()))
                .build();
    }
}
