package zh.lingvo.data.model.converters;

import com.google.common.collect.ImmutableList;
import zh.stringlexer.AbstractLexer;
import zh.stringlexer.LexerState;
import zh.stringlexer.LexerTransition;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter(autoApply = true)
public class LongListAttributeConverter implements AttributeConverter<List<Long>, String> {
    @Override
    public String convertToDatabaseColumn(List<Long> list) {
        if (list == null || list.isEmpty())
            return null;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); ++i) {
            sb.append(list.get(i));
            if (i < list.size() - 1)
                sb.append(',');
        }
        return sb.toString();
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank())
            return ImmutableList.of();

        ImmutableList.Builder<Long> builder = ImmutableList.builder();
        Lexer lexer = new Lexer(builder);
        lexer.lex(dbData);
        return builder.build();
    }

    private enum State implements LexerState {
        NEXT_DIGIT
    }

    private static class Lexer extends AbstractLexer<State> {
        private StringBuilder sb = new StringBuilder();

        private Lexer(ImmutableList.Builder<Long> builder) {
            super(State.NEXT_DIGIT);
            initTransitions(getTransitions(builder));
        }

        private List<LexerTransition<State>> getTransitions(ImmutableList.Builder<Long> builder) {
            return ImmutableList.of(
                    new LexerTransition<>(State.NEXT_DIGIT, Character::isDigit, this::storeDigit, State.NEXT_DIGIT),
                    new LexerTransition<>(State.NEXT_DIGIT, ch -> ch == ',', ch -> collectNumber(builder), State.NEXT_DIGIT),
                    new LexerTransition<>(State.NEXT_DIGIT, ch -> ch == NULL_EVENT, ch -> collectNumber(builder), null)
            );
        }

        private void storeDigit(char ch) {
            sb.append(ch);
        }

        private void collectNumber(ImmutableList.Builder<Long> builder) {
            long num = Long.parseLong(sb.toString());
            builder.add(num);
            sb = new StringBuilder();
        }
    }
}
