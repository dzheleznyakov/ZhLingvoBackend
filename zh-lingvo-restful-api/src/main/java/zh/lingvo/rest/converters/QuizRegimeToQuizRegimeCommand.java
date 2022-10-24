package zh.lingvo.rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.enums.QuizRegime;
import zh.lingvo.rest.commands.QuizRegimeCommand;

import javax.annotation.Nonnull;

@Component
public class QuizRegimeToQuizRegimeCommand implements Converter<QuizRegime, QuizRegimeCommand> {
    @Override
    public QuizRegimeCommand convert(@Nonnull QuizRegime source) {
        return QuizRegimeCommand.builder()
                .value(source.toString())
                .code(source.getCode())
                .build();
    }
}
