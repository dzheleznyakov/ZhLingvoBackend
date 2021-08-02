package zh.config.parser;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static zh.config.parser.Lexer.State.COMMENT;
import static zh.config.parser.Lexer.State.EOF;
import static zh.config.parser.Lexer.State.ESCAPE;
import static zh.config.parser.Lexer.State.NAME;
import static zh.config.parser.Lexer.State.NEW_TOKEN;
import static zh.config.parser.Lexer.State.SIMPLE_STRING;
import static zh.config.parser.Lexer.State.SLASH;
import static zh.config.parser.Lexer.State.STRING;

class Lexer {
    private static final char NULL_EVENT = '\0';
    private static final char DOUBLE_QUOT = '"';
    private static final char BACKSLASH = '\\';
    private static final char FORWARDSLASH = '/';

    private final TokenCollector collector;
    private Transition[] transitions;
    private Transition errorTransition;
    private State state = NEW_TOKEN;
    private int line = -1;
    private int linePos = -1;
    private int totalPos = -1;
    private int nameStart = -1;
    private String input = "";
    private String stringToken = "";
    private String nameToken = "";

    public Lexer(TokenCollector collector) {
        this.collector = collector;
        initTransitions();
    }

    public void lex(String input) {
        init(input);
        for (int i = 0; i < input.length(); ++i) {
            handleEvent(input.charAt(i));
        }
        handleEvent(NULL_EVENT);
    }

    private void init(String input) {
        this.input = input;
        line = 1;
        linePos = 0;
        totalPos = 0;
    }

    private void handleEvent(char event) {
        Transition transition = Arrays.stream(transitions)
                .filter(tr -> tr.currentState == state)
                .filter(tr -> tr.eventTest.test(event))
                .findFirst()
                .orElse(errorTransition);
        transition.run(event);
    }

    enum State {
        NEW_TOKEN, NAME, SIMPLE_STRING, STRING, ESCAPE, SLASH, COMMENT, EOF
    }

    private class Transition {
        private final State currentState;
        private final Predicate<Character> eventTest;
        private final State nextState;
        private final Consumer<Character> action;

        private Transition(State currentState, Predicate<Character> eventTest, State nextState, Consumer<Character> action) {
            this.currentState = currentState;
            this.eventTest = eventTest;
            this.nextState = nextState;
            this.action = action;
        }

        void run(char event) {
            state = nextState;
            action.accept(event);
            ++totalPos;
        }
    }

    final void initTransitions() {
        errorTransition = new Transition(null, ch -> true, NEW_TOKEN, ch -> collector.error(line, linePos));

        transitions = new Transition[]{
                new Transition(NEW_TOKEN, ch -> ch.equals('{'), NEW_TOKEN, ch -> collector.openBrace(line, moveForward())),
                new Transition(NEW_TOKEN, ch -> ch.equals('}'), NEW_TOKEN, ch -> collector.closedBrace(line, moveForward())),
                new Transition(NEW_TOKEN, ch -> ch.equals('['), NEW_TOKEN, ch -> collector.openBracket(line, moveForward())),
                new Transition(NEW_TOKEN, ch -> ch.equals(']'), NEW_TOKEN, ch -> collector.closedBracket(line, moveForward())),
                new Transition(NEW_TOKEN, this::isNewLine, NEW_TOKEN, ch -> newLine()),
                new Transition(NEW_TOKEN, this::isInlinedWhitespace, NEW_TOKEN, ch -> moveForward()),
                new Transition(NEW_TOKEN, this::isStartOfNameChar, NAME, ch -> markNameStartAt(totalPos)),
                new Transition(NEW_TOKEN, this::isStartOfSimpleStringChar, SIMPLE_STRING, ch -> markNameStartAt(totalPos)),
                new Transition(NEW_TOKEN, ch -> ch.equals(DOUBLE_QUOT), STRING, ch -> markNameStartAt(totalPos + 1)),
                new Transition(NEW_TOKEN, ch -> ch.equals(FORWARDSLASH), SLASH, ch -> moveForward()),
                new Transition(NEW_TOKEN, this::isNullChar, EOF, ch -> {}),


                new Transition(NAME, this::isNameChar, NAME, ch -> moveForward()),
                new Transition(NAME, this::isNewLine, NEW_TOKEN, ch -> {
                    collectName();
                    newLine();
                }),
                new Transition(NAME, this::isInlinedWhitespace, NEW_TOKEN, ch -> collectName()),
                new Transition(NAME, this::isNotNameChar, SIMPLE_STRING, ch -> moveForward()),
                new Transition(NAME, ch -> ch.equals('['), NEW_TOKEN, ch -> {
                   collectName();
                   collector.openBracket(line, linePos);
                }),
                new Transition(NAME, ch -> ch.equals(']'), NEW_TOKEN, ch -> {
                    collectName();
                    collector.closedBracket(line, linePos);
                }),
                new Transition(NAME, ch -> ch.equals('{'), NEW_TOKEN, ch -> {
                    collectName();
                    collector.openBrace(line, linePos);
                }),
                new Transition(NAME, ch -> ch.equals('}'), NEW_TOKEN, ch -> {
                    collectName();
                    collector.closedBrace(line, linePos);
                }),
                new Transition(NAME, ch -> ch.equals(FORWARDSLASH), SLASH, ch -> nameToken = input.substring(nameStart, linePos++)),
                new Transition(NAME, this::isNullChar, EOF, ch -> collectName()),


                new Transition(SIMPLE_STRING, this::isSimpleStringChar, SIMPLE_STRING, ch -> moveForward()),
                new Transition(SIMPLE_STRING, this::isInlinedWhitespace, NEW_TOKEN, ch -> collectString()),
                new Transition(SIMPLE_STRING, this::isNewLine, NEW_TOKEN, ch -> {
                    collectString();
                    newLine();
                }),
                new Transition(SIMPLE_STRING, ch -> ch.equals(FORWARDSLASH), SLASH, ch -> stringToken = input.substring(nameStart, linePos++)),
                new Transition(SIMPLE_STRING, ch -> ch.equals('['), NEW_TOKEN, ch -> {
                    collectString();
                    collector.openBracket(line, linePos);
                }),
                new Transition(SIMPLE_STRING, ch -> ch.equals(']'), NEW_TOKEN, ch -> {
                    collectString();
                    collector.closedBracket(line, linePos);
                }),
                new Transition(SIMPLE_STRING, ch -> ch.equals('{'), NEW_TOKEN, ch -> {
                    collectString();
                    collector.openBrace(line, linePos);
                }),
                new Transition(SIMPLE_STRING, ch -> ch.equals('}'), NEW_TOKEN, ch -> {
                    collectString();
                    collector.closedBrace(line, linePos);
                }),
                new Transition(SIMPLE_STRING, this::isNullChar, EOF, ch -> collectString()),

                new Transition(STRING, this::isStringChar, STRING, ch -> moveForward()),
                new Transition(STRING, ch -> ch.equals(BACKSLASH), ESCAPE, ch -> prepareEscape()),
                new Transition(STRING, ch -> ch.equals(DOUBLE_QUOT), NEW_TOKEN, ch -> collectString()),


                new Transition(ESCAPE, ch -> ch.equals(DOUBLE_QUOT), STRING, ch -> escape(DOUBLE_QUOT)),
                new Transition(ESCAPE, ch -> ch.equals('n'), STRING, ch -> escape('\n')),
                new Transition(ESCAPE, ch -> ch.equals('t'), STRING, ch -> escape('\t')),
                new Transition(ESCAPE, ch -> ch.equals(BACKSLASH), STRING, ch -> escape(BACKSLASH)),


                new Transition(SLASH, ch -> ch.equals(FORWARDSLASH), COMMENT, ch -> {
                    if (nameToken.length() > 0) {
                        collector.name(nameToken, line, ++linePos);
                        nameToken = "";
                    }
                    if (stringToken.length() > 0) {
                        collector.string(stringToken, line, ++linePos);
                        stringToken = "";
                    }
                }),
                new Transition(SLASH, ch -> !ch.equals(FORWARDSLASH), SIMPLE_STRING, ch -> {
                    nameToken = "";
                    stringToken = "";
                    ++linePos;
                }),


                new Transition(COMMENT, ch -> !isNewLine(ch) && !isNullChar(ch), COMMENT, ch -> moveForward()),
                new Transition(COMMENT, this::isNewLine, NEW_TOKEN, ch -> newLine()),
                new Transition(COMMENT, this::isNullChar, EOF, ch -> {})
        };
    }

    private void markNameStartAt(int totalPos) {
        nameStart = totalPos;
        moveForward();
    }

    private int moveForward() {
        return ++linePos;
    }

    private boolean isStringChar(char ch) {
        return ch != DOUBLE_QUOT && ch != BACKSLASH && ch != NULL_EVENT;
    }

    private boolean isSimpleStringChar(char ch) {
        return !Character.isWhitespace(ch) && ch != NULL_EVENT && ch != FORWARDSLASH &&
                ch != '[' && ch != ']' && ch != '{' && ch != '}';
    }

    private boolean isNewLine(Character ch) {
        return ch.equals('\n');
    }

    private boolean isNullChar(Character ch) {
        return ch.equals(NULL_EVENT);
    }

    private boolean isInlinedWhitespace(Character ch) {
        return !isNewLine(ch) && Character.isWhitespace(ch);
    }

    private boolean isStartOfNameChar(char ch) {
        return Character.isLetter(ch) || ch == '_';
    }

    private boolean isNameChar(char ch) {
        return isStartOfNameChar(ch) || Character.isDigit(ch);
    }

    private boolean isNotNameChar(char ch) {
        return !isNameChar(ch) && !Character.isWhitespace(ch) && ch != NULL_EVENT && ch != FORWARDSLASH
                && ch != '[' && ch != ']' && ch != '{' && ch != '}';
    }

    private boolean isStartOfSimpleStringChar(char ch) {
        return isNotNameChar(ch) && ch != DOUBLE_QUOT && ch != FORWARDSLASH;
    }

    private void newLine() {
        ++line;
        linePos = 0;
    }

    private void collectName() {
        String name = input.substring(nameStart, totalPos);
        collector.name(name, line, ++linePos);
    }

    private void collectString() {
        String string = stringToken + input.substring(nameStart, totalPos);
        collector.string(string, line, ++linePos);
    }

    private void prepareEscape() {
        stringToken += input.substring(nameStart, totalPos);
        ++linePos;
    }

    private void escape(char ch) {
        stringToken += ch;
        nameStart = totalPos + 1;
        ++linePos;
    }
}
