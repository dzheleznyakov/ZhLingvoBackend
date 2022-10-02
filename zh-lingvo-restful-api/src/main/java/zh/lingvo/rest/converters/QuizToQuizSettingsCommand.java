package zh.lingvo.rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.rest.commands.QuizSettingsCommand;

import javax.annotation.Nullable;

@Component
public class QuizToQuizSettingsCommand implements Converter<Quiz, QuizSettingsCommand> {
    @Override
    public QuizSettingsCommand convert(@Nullable Quiz source) {
        return source == null ? null : QuizSettingsCommand.builder()
                .quizId(source.getId())
                .maxScore(source.getMaxScore())
                .quizRegime(convertQuizRegime(source))
                .matchingRegime(convertMatchingRegime(source))
                .build();
    }

    private String convertQuizRegime(Quiz source) {
        return source.getQuizRegime() == null ? null : source.getQuizRegime().getCode();
    }

    private String convertMatchingRegime(Quiz source) {
        return source.getMatchingRegime() == null ? null : source.getMatchingRegime().getCode();
    }
}
