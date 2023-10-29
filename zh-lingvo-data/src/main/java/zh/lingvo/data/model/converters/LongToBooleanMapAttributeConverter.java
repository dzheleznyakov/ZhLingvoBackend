package zh.lingvo.data.model.converters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zh.stringlexer.AbstractLexer;
import zh.stringlexer.LexerState;
import zh.stringlexer.LexerTransition;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Converter(autoApply = true)
public class LongToBooleanMapAttributeConverter implements AttributeConverter<Map<Long, Boolean>, String> {
    @Override
    public String convertToDatabaseColumn(Map<Long, Boolean> map) {
        if (map == null || map.isEmpty())
            return null;

        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<Long, Boolean>> entries = map.entrySet();
        for (Iterator<Map.Entry<Long, Boolean>> it = entries.iterator(); it.hasNext(); ) {
            Map.Entry<Long, Boolean> entry = it.next();
            sb.append(entry.getKey())
                    .append(':')
                    .append(entry.getValue() ? '1' : '0');
            if (it.hasNext())
                sb.append(',');
        }
        return sb.toString();
    }

    @Override
    public Map<Long, Boolean> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty())
            return ImmutableMap.of();

        ImmutableMap.Builder<Long, Boolean> builder = ImmutableMap.builder();
        Lexer lexer = new Lexer(builder);
        lexer.lex(dbData);
        return builder.build();
    }

    private enum State implements LexerState {
        READING_KEY,
        READING_VALUE
    }

    private static class Lexer extends AbstractLexer<State> {
        private Long key;
        private StringBuilder sb = new StringBuilder();

        protected Lexer(ImmutableMap.Builder<Long, Boolean> builder) {
            super(State.READING_KEY);
            initTransitions(getTransitions(builder));
        }

        private List<LexerTransition<State>> getTransitions(ImmutableMap.Builder<Long, Boolean> builder) {
            return ImmutableList.of(
                    new LexerTransition<>(State.READING_KEY, Character::isDigit, this::storeDigit, State.READING_KEY),
                    new LexerTransition<>(State.READING_KEY, ch -> ch == ':', ch -> collectKey(), State.READING_VALUE),
                    new LexerTransition<>(State.READING_VALUE, ch -> ch == '0', ch -> collectFalseEntry(builder), State.READING_VALUE),
                    new LexerTransition<>(State.READING_VALUE, ch -> ch == '1', ch -> collectTrueEntry(builder), State.READING_VALUE),
                    new LexerTransition<>(State.READING_VALUE, ch -> ch == ',', ch -> {}, State.READING_KEY),
                    new LexerTransition<>(State.READING_VALUE, ch -> ch == NULL_EVENT, ch -> {}, null)
            );
        }

        private void storeDigit(char ch) {
            sb.append(ch);
        }

        private void collectKey() {
            key = Long.parseLong(sb.toString());
            sb = new StringBuilder();
        }

        private void collectFalseEntry(ImmutableMap.Builder<Long, Boolean> builder) {
            builder.put(key, false);
        }

        private void collectTrueEntry(ImmutableMap.Builder<Long, Boolean> builder) {
            builder.put(key, true);
        }

    }
}
