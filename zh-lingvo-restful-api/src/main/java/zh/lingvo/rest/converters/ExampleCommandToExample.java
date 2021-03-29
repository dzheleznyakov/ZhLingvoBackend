package zh.lingvo.rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.Example;
import zh.lingvo.rest.commands.ExampleCommand;

import javax.annotation.Nullable;

@Component
public class ExampleCommandToExample implements Converter<ExampleCommand, Example> {
    @Override
    public Example convert(@Nullable ExampleCommand source) {
        return source == null ? null : Example.builder()
                .id(source.getId())
                .remark(source.getRemark())
                .expression(source.getExpression())
                .explanation(source.getExplanation())
                .build();
    }
}
