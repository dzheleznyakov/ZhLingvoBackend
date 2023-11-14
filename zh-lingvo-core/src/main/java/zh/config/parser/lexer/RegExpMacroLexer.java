package zh.config.parser.lexer;

import java.util.Arrays;
import java.util.stream.Stream;

import static zh.config.parser.lexer.Lexer.BasicState.EOF;
import static zh.config.parser.lexer.Lexer.BasicState.MACRO_START;
import static zh.config.parser.lexer.Lexer.BasicState.NEW_TOKEN;
import static zh.config.parser.lexer.RegExpMacroLexer.RegExpMacroState.MATCHER;
import static zh.config.parser.lexer.RegExpMacroLexer.RegExpMacroState.SUBSTITUTION;
import static zh.config.parser.lexer.RegExpMacroLexer.RegExpMacroState.TRANSITION;

class RegExpMacroLexer {
    private static final char EQUALITY = '=';
    private static final char GOE = '>';

    private final Lexer lexer;

    RegExpMacroLexer(Lexer lexer) {
        this.lexer = lexer;
    }

    final Stream<Lexer.Transition> streamTransitions(Stream<Lexer.Transition> transitions) {
        return Stream.concat(transitions, Arrays.stream(new Lexer.Transition[]{
                lexer.newTransition(MACRO_START, lexer::isOpenBrace, MATCHER, () -> lexer.markNameStartAt(lexer.totalPos + 1)),
                lexer.newTransition(MATCHER, this::isRegexpChar, MATCHER, lexer::moveForward),
                lexer.newTransition(MATCHER, this::isEqualityChar, TRANSITION, this::storeMatcher),
                lexer.newTransition(TRANSITION, this::isGreatOrEqualChar, SUBSTITUTION, () -> lexer.markNameStartAt(lexer.totalPos + 1)),
                lexer.newTransition(SUBSTITUTION, this::isRegexpChar, SUBSTITUTION, lexer::moveForward),
                lexer.newTransition(SUBSTITUTION, lexer::isClosedBrace, NEW_TOKEN, this::collectRegExp),
                lexer.newTransition(MATCHER, lexer::isNullChar, EOF, () -> lexer.collector.error(lexer.line, lexer.linePos)),
                lexer.newTransition(SUBSTITUTION, lexer::isNullChar, EOF, () -> lexer.collector.error(lexer.line, lexer.linePos))
        }));
    }

    enum RegExpMacroState implements Lexer.State {
        MATCHER, SUBSTITUTION, TRANSITION
    }

    private boolean isRegexpChar(char ch) {
        return !lexer.isClosedBrace(ch)
                && !lexer.isNullChar(ch)
                && !this.isEqualityChar(ch);
    }

    private boolean isEqualityChar(Character ch) {
        return ch.equals(EQUALITY);
    }

    private boolean isGreatOrEqualChar(Character ch) {
        return ch.equals(GOE);
    }

    private void storeMatcher() {
        lexer.stringToken = lexer.input.substring(lexer.stringishStart, lexer.totalPos);
        lexer.moveForward();
    }

    private void collectRegExp() {
        String matcher = lexer.stringToken.trim();
        String substitution = lexer.input.substring(lexer.stringishStart, lexer.totalPos).trim();
        lexer.collector.regexp(matcher, substitution, lexer.line, ++lexer.linePos);
    }
}
