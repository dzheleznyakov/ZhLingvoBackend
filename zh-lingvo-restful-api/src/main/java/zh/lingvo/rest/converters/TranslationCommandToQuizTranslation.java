package zh.lingvo.rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.QuizTranslation;
import zh.lingvo.rest.commands.TranslationCommand;

import javax.annotation.Nullable;

@Component
public class TranslationCommandToQuizTranslation implements Converter<TranslationCommand, QuizTranslation> {
    @Override
    public QuizTranslation convert(@Nullable TranslationCommand source) {
        return source == null ? null : QuizTranslation.builder()
                .id(source.getId())
                .value(source.getValue())
                .elaboration(source.getElaboration())
                .build();
    }
}
