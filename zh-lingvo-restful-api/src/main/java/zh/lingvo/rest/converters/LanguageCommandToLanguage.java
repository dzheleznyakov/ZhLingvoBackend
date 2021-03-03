package zh.lingvo.rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.Language;
import zh.lingvo.rest.commands.LanguageCommand;

import javax.annotation.Nullable;

@Component
public class LanguageCommandToLanguage implements Converter<LanguageCommand, Language> {
    @Override
    public Language convert(@Nullable LanguageCommand source) {
        return source == null ? null : Language.builder()
                .id(source.getId())
                .name(source.getName())
                .twoLetterCode(source.getCode())
                .build();
    }
}
