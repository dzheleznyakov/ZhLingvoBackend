package zh.lingvo.rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.QuizTranslation;
import zh.lingvo.rest.commands.TranslationCommand;

import javax.annotation.Nullable;

@Component
public class QuizTranslationToTranslationCommand implements Converter<QuizTranslation, TranslationCommand> {
    @Override
    public TranslationCommand convert(@Nullable QuizTranslation source) {
        return source == null ? null : TranslationCommand.builder()
                .id(source.getId())
                .value(source.getValue())
                .elaboration(source.getElaboration())
                .build();
    }
}
