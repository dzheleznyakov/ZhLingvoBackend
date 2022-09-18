package zh.lingvo.rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.QuizExample;
import zh.lingvo.rest.commands.ExampleCommand;

import javax.annotation.Nullable;

@Component
public class QuizExampleToExampleCommand implements Converter<QuizExample, ExampleCommand> {
    @Override
    public ExampleCommand convert(@Nullable QuizExample source) {
        return source == null ? null : ExampleCommand.builder()
                .id(source.getId())
                .remark(source.getRemark())
                .expression(source.getExpression())
                .explanation(source.getExplanation())
                .build();
    }
}
