package zh.stringlexer;

public class LexerTransition<S extends LexerState> {
    final S state;
    private final CharPredicate condition;
    private final CharConsumer action;
    private final S nextState;

    public LexerTransition(S state, CharPredicate condition, CharConsumer action, S nextState) {
        this.state = state;
        this.condition = condition;
        this.action = action;
        this.nextState = nextState;
    }

    public boolean test(char ch) {
        return condition.test(ch);
    }

    public S apply(char ch) {
        action.accept(ch);
        return nextState;
    }
}
