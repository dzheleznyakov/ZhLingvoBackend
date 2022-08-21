package zh.lingvo.rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.rest.commands.QuizCommand;

import javax.annotation.Nullable;

@Component
public class QuizToQuizCommand implements Converter<Quiz, QuizCommand> {
    private final LanguageToLanguageCommand languageConverter;

    public QuizToQuizCommand(LanguageToLanguageCommand languageConverter) {
        this.languageConverter = languageConverter;
    }

    @Override
    public QuizCommand convert(@Nullable Quiz source) {
        return source == null ? null : QuizCommand.builder()
                .id(source.getId())
                .name(source.getName())
                .targetLanguage(languageConverter.convert(source.getLanguage()))
                .build();
    }
}
