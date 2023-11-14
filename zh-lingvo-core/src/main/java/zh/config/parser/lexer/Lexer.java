package zh.config.parser.lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static zh.config.parser.lexer.Lexer.BasicState.COMMENT;
import static zh.config.parser.lexer.Lexer.BasicState.EOF;
import static zh.config.parser.lexer.Lexer.BasicState.ESCAPE;
import static zh.config.parser.lexer.Lexer.BasicState.MACRO_START;
import static zh.config.parser.lexer.Lexer.BasicState.NAME;
import static zh.config.parser.lexer.Lexer.BasicState.NEW_TOKEN;
import static zh.config.parser.lexer.Lexer.BasicState.SIMPLE_STRING;
import static zh.config.parser.lexer.Lexer.BasicState.SLASH;
import static zh.config.parser.lexer.Lexer.BasicState.STRING;

public class Lexer {
    private static final char NULL_EVENT = '\0';
    private static final char DOUBLE_QUOT = '"';
    private static final char BACKSLASH = '\\';
    private static final char FORWARDSLASH = '/';
    private static final char CLOSED_BRACE = '}';
    private static final char OPEN_BRACE = '{';
    private static final char OPEN_BRACKET = '[';
    private static final char CLOSED_BRACKET = ']';
    private static final char LINE_FEED = '\n';
    private static final char UNDERSCORE = '_';

    final TokenCollector collector;
    private Map<State, List<Transition>> transitions;
    private Transition errorTransition;
    private State state = NEW_TOKEN;
    int line = -1;
    int linePos = -1;
    int totalPos = -1;
    int stringishStart = -1;
    String input = "";
    String stringToken = "";
    private String nameToken = "";

    public Lexer(TokenCollector collector) {
        this.collector = collector;
        initTransitions();
    }

    public void lex(String input) {
        init(input);
        for (int i = 0; i < input.length(); ++i)
            handleEvent(input.charAt(i));
        handleEvent(NULL_EVENT);
    }

    private void init(String input) {
        this.input = input;
        line = 1;
        linePos = 0;
        totalPos = 0;
        state = NEW_TOKEN;
    }

    private void handleEvent(char event) {
        transitions.getOrDefault(state, new ArrayList<>()).stream()
                .filter(tr -> tr.eventTest.test(event))
                .findFirst()
                .orElse(errorTransition)
                .run();
    }

    interface State {}

    enum BasicState implements State {
        NEW_TOKEN, NAME, SIMPLE_STRING, STRING, ESCAPE, SLASH, MACRO_START, COMMENT, EOF
    }

    class Transition {
        private final State currentState;
        private final Predicate<Character> eventTest;
        private final State nextState;
        private final Runnable action;

        Transition(
                State currentState,
                Predicate<Character> eventTest,
                State nextState,
                Runnable action
        ) {
            this.currentState = currentState;
            this.eventTest = eventTest;
            this.nextState = nextState;
            this.action = action;
        }

        void run() {
            state = nextState;
            action.run();
            ++totalPos;
        }
    }

    final Transition newTransition(
            State currentState,
            Predicate<Character> eventTest,
            State nextState,
            Runnable action
    ) {
        return new Transition(currentState, eventTest, nextState, action);
    }

    final void initTransitions() {
        errorTransition = new Transition(null, ch -> true, NEW_TOKEN, () -> collector.error(line, linePos));

        var transitions = Arrays.stream(new Transition[]{
                new Transition(NEW_TOKEN, this::isOpenBrace, NEW_TOKEN, () -> collector.openBrace(line, moveForward())),
                new Transition(NEW_TOKEN, this::isClosedBrace, NEW_TOKEN, () -> collector.closedBrace(line, moveForward())),
                new Transition(NEW_TOKEN, this::isOpenBracket, NEW_TOKEN, () -> collector.openBracket(line, moveForward())),
                new Transition(NEW_TOKEN, this::isClosedBracket, NEW_TOKEN, () -> collector.closedBracket(line, moveForward())),
                new Transition(NEW_TOKEN, this::isNewLine, NEW_TOKEN, this::newLine),
                new Transition(NEW_TOKEN, this::isInlinedWhitespace, NEW_TOKEN, this::moveForward),
                new Transition(NEW_TOKEN, this::isStartOfNameChar, NAME, () -> markNameStartAt(totalPos)),
                new Transition(NEW_TOKEN, this::isStartOfSimpleStringChar, SIMPLE_STRING, () -> markNameStartAt(totalPos)),
                new Transition(NEW_TOKEN, this::isDoubleQuotation, STRING, () -> markNameStartAt(totalPos + 1)),
                new Transition(NEW_TOKEN, this::isForwardslash, SLASH, this::moveForward),
                new Transition(NEW_TOKEN, this::isBackslash, MACRO_START, this::moveForward),
                new Transition(NEW_TOKEN, this::isNullChar, EOF, this::finish),

                new Transition(NAME, this::isNameChar, NAME, this::moveForward),
                new Transition(NAME, this::isNewLine, NEW_TOKEN, () -> {
                    collectName();
                    newLine();
                }),
                new Transition(NAME, this::isInlinedWhitespace, NEW_TOKEN, this::collectName),
                new Transition(NAME, this::isNotNameChar, SIMPLE_STRING, this::moveForward),
                new Transition(NAME, this::isOpenBracket, NEW_TOKEN, () -> {
                    collectName();
                    collector.openBracket(line, linePos);
                }),
                new Transition(NAME, this::isClosedBracket, NEW_TOKEN, () -> {
                    collectName();
                    collector.closedBracket(line, linePos);
                }),
                new Transition(NAME, this::isOpenBrace, NEW_TOKEN, () -> {
                    collectName();
                    collector.openBrace(line, linePos);
                }),
                new Transition(NAME, this::isClosedBrace, NEW_TOKEN, () -> {
                    collectName();
                    collector.closedBrace(line, linePos);
                }),
                new Transition(NAME, this::isForwardslash, SLASH, () -> nameToken = input.substring(stringishStart, linePos++)),
                new Transition(NAME, this::isNullChar, EOF, () -> {
                    collectName();
                    finish();
                }),


                new Transition(SIMPLE_STRING, this::isSimpleStringChar, SIMPLE_STRING, this::moveForward),
                new Transition(SIMPLE_STRING, this::isInlinedWhitespace, NEW_TOKEN, this::collectString),
                new Transition(SIMPLE_STRING, this::isNewLine, NEW_TOKEN, () -> {
                    collectString();
                    newLine();
                }),
                new Transition(SIMPLE_STRING, this::isForwardslash, SLASH, () -> stringToken = input.substring(stringishStart, linePos++)),
                new Transition(SIMPLE_STRING, this::isOpenBracket, NEW_TOKEN, () -> {
                    collectString();
                    collector.openBracket(line, linePos);
                }),
                new Transition(SIMPLE_STRING, this::isClosedBracket, NEW_TOKEN, () -> {
                    collectString();
                    collector.closedBracket(line, linePos);
                }),
                new Transition(SIMPLE_STRING, this::isOpenBrace, NEW_TOKEN, () -> {
                    collectString();
                    collector.openBrace(line, linePos);
                }),
                new Transition(SIMPLE_STRING, this::isClosedBrace, NEW_TOKEN, () -> {
                    collectString();
                    collector.closedBrace(line, linePos);
                }),
                new Transition(SIMPLE_STRING, this::isNullChar, EOF, () -> {
                    collectString();
                    finish();
                }),

                new Transition(STRING, this::isStringChar, STRING, this::moveForward),
                new Transition(STRING, this::isBackslash, ESCAPE, this::prepareEscape),
                new Transition(STRING, this::isDoubleQuotation, NEW_TOKEN, this::collectString),
                new Transition(STRING, this::isNullChar, EOF, () -> collector.error(line, linePos)),


                new Transition(ESCAPE, this::isDoubleQuotation, STRING, () -> escape(DOUBLE_QUOT)),
                new Transition(ESCAPE, ch -> ch.equals('n'), STRING, () -> escape(LINE_FEED)),
                new Transition(ESCAPE, ch -> ch.equals('t'), STRING, () -> escape('\t')),
                new Transition(ESCAPE, this::isBackslash, STRING, () -> escape(BACKSLASH)),


                new Transition(SLASH, this::isForwardslash, COMMENT, this::collectNameOrString),
                new Transition(SLASH, ch -> !isForwardslash(ch), SIMPLE_STRING, () -> {
                    nameToken = "";
                    stringToken = "";
                    ++linePos;
                }),


                new Transition(COMMENT, ch -> !isNewLine(ch) && !isNullChar(ch), COMMENT, this::moveForward),
                new Transition(COMMENT, this::isNewLine, NEW_TOKEN, this::newLine),
                new Transition(COMMENT, this::isNullChar, EOF, this::finish),
        });
        var regexpLexer = new RegExpMacroLexer(this);

        transitions = regexpLexer.streamTransitions(transitions);
        this.transitions = transitions.collect(Collectors.groupingBy(tr -> tr.currentState));
    }

    private void collectNameOrString() {
        if (!nameToken.isEmpty())
            collector.name(nameToken, line, ++linePos);
        if (!stringToken.isEmpty())
            collector.string(stringToken, line, ++linePos);
        stringToken = "";
        nameToken = "";
    }

    private boolean isBackslash(Character ch) {
        return ch.equals(BACKSLASH);
    }

    private boolean isForwardslash(Character ch) {
        return ch.equals(FORWARDSLASH);
    }

    private boolean isDoubleQuotation(Character ch) {
        return ch.equals(DOUBLE_QUOT);
    }

    boolean isOpenBrace(Character ch) {
        return ch.equals(OPEN_BRACE);
    }

    boolean isClosedBrace(Character ch) {
        return ch.equals(CLOSED_BRACE);
    }

    private boolean isOpenBracket(Character ch) {
        return ch.equals(OPEN_BRACKET);
    }

    private boolean isClosedBracket(Character ch) {
        return ch.equals(CLOSED_BRACKET);
    }

    boolean isNullChar(Character ch) {
        return ch.equals(NULL_EVENT);
    }

    private boolean isNewLine(Character ch) {
        return ch.equals(LINE_FEED);
    }

    private boolean isStringChar(char ch) {
        return !isDoubleQuotation(ch) &&
                !isBackslash(ch) &&
                !isNullChar(ch);
    }

    private boolean isSimpleStringChar(char ch) {
        return !Character.isWhitespace(ch) &&
                !isNullChar(ch) &&
                !isForwardslash(ch) &&
                !isOpenBracket(ch) &&
                !isClosedBracket(ch) &&
                !isOpenBrace(ch) &&
                !isClosedBrace(ch);
    }

    private boolean isInlinedWhitespace(Character ch) {
        return !isNewLine(ch) && Character.isWhitespace(ch);
    }

    private boolean isStartOfNameChar(char ch) {
        return Character.isLetter(ch) || ch == UNDERSCORE;
    }

    private boolean isNameChar(char ch) {
        return isStartOfNameChar(ch) || Character.isDigit(ch);
    }

    private boolean isNotNameChar(char ch) {
        return !isNameChar(ch) &&
                !Character.isWhitespace(ch) &&
                !isNullChar(ch) &&
                !isForwardslash(ch) &&
                !isOpenBracket(ch) &&
                !isClosedBracket(ch) &&
                !isOpenBrace(ch) &&
                !isClosedBrace(ch);
    }

    private boolean isStartOfSimpleStringChar(char ch) {
        return (
                isNotNameChar(ch) &&
                !isDoubleQuotation(ch) &&
                !isForwardslash(ch) &&
                !isBackslash(ch)
        ) || Character.isDigit(ch);
    }

    void markNameStartAt(int totalPos) {
        stringishStart = totalPos;
        moveForward();
    }

    int moveForward() {
        return ++linePos;
    }

    private void newLine() {
        ++line;
        linePos = 0;
    }

    private void collectName() {
        String name = input.substring(stringishStart, totalPos);
        collector.name(name, line, ++linePos);
    }

    private void collectString() {
        String string = stringToken + input.substring(stringishStart, totalPos);
        stringToken = "";
        collector.string(string, line, ++linePos);
    }

    private void prepareEscape() {
        stringToken += input.substring(stringishStart, totalPos);
        ++linePos;
    }

    private void escape(char ch) {
        stringToken += ch;
        stringishStart = totalPos + 1;
        ++linePos;
    }

    private void finish() {
        collector.done(line, linePos);
    }
}
