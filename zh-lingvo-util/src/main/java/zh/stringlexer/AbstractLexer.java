package zh.stringlexer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractLexer<S extends LexerState> {
    protected static final char NULL_EVENT = '\0';

    private final Map<S, List<LexerTransition<S>>> transitionsByState;
    private S state;

    protected AbstractLexer(S initialState) {
        transitionsByState = new HashMap<>();
        state = initialState;
    }

    protected void initTransitions(List<LexerTransition<S>> transitions) {
        transitions.forEach(transition -> {
            List<LexerTransition<S>> addedTransitions = transitionsByState.getOrDefault(transition.state, new ArrayList<>());
            addedTransitions.add(transition);
            transitionsByState.put(transition.state, addedTransitions);
        });
    }

    public void lex(@Nonnull String input) {
        for (int i = 0; i < input.length(); ++i)
            handle(input.charAt(i));
        handle(NULL_EVENT);
    }

    private void handle(char ch) {
        LexerTransition<S> transition = transitionsByState.getOrDefault(state, new ArrayList<>())
                        .stream()
                        .filter(tr -> tr.test(ch))
                        .findFirst()
                        .orElseThrow(() -> new NotFoundTransition(state, ch));
        state = transition.apply(ch);
    }

    public static class NotFoundTransition extends RuntimeException {
        private NotFoundTransition(LexerState state, char ch) {
            super(String.format("Cannot find transition for state [%s] and char [%s]", state.toString(), ch));
        }
    }
}
