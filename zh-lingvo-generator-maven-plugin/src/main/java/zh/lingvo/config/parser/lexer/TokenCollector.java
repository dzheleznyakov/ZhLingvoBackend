package zh.lingvo.config.parser.lexer;

public interface TokenCollector {
    void openBrace(int line, int pos);
    void closedBrace(int line, int pos);
    void openBracket(int line, int pos);
    void closedBracket(int line, int pos);
    void name(String name, int line, int pos);
    void string(String str, int line, int pos);
    void error(int line, int pos);

    default void done(int line, int pos) {}
}
