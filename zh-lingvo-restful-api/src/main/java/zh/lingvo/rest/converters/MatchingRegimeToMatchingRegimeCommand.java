package zh.lingvo.rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.enums.MatchingRegime;
import zh.lingvo.rest.commands.MatchingRegimeCommand;

import javax.annotation.Nonnull;

@Component
public class MatchingRegimeToMatchingRegimeCommand implements Converter<MatchingRegime, MatchingRegimeCommand> {
    @Override
    public MatchingRegimeCommand convert(@Nonnull MatchingRegime source) {
        return MatchingRegimeCommand.builder()
                .value(source.toString())
                .code(source.getCode())
                .build();
    }
}
