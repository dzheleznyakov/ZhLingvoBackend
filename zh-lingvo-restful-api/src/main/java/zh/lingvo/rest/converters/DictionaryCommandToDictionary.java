package zh.lingvo.rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.rest.commands.DictionaryCommand;

import javax.annotation.Nullable;

@Component
public class DictionaryCommandToDictionary implements Converter<DictionaryCommand, Dictionary> {
    private final LanguageCommandToLanguage languageConverter;

    public DictionaryCommandToDictionary(LanguageCommandToLanguage languageConverter) {
        this.languageConverter = languageConverter;
    }

    @Override
    public Dictionary convert(@Nullable DictionaryCommand source) {
        return source == null ? null : Dictionary.builder()
                .id(source.getId())
                .name(source.getName())
                .language(languageConverter.convert(source.getLanguage()))
                .build();
    }
}
