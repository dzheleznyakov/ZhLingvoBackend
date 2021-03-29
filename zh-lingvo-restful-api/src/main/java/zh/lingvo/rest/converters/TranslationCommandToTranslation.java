package zh.lingvo.rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.Translation;
import zh.lingvo.rest.commands.TranslationCommand;

import javax.annotation.Nullable;

@Component
public class TranslationCommandToTranslation implements Converter<TranslationCommand, Translation> {
    @Override
    public Translation convert(@Nullable TranslationCommand source) {
        return source == null ? null : Translation.builder()
                .id(source.getId())
                .value(source.getValue())
                .elaboration(source.getElaboration())
                .build();
    }
}
