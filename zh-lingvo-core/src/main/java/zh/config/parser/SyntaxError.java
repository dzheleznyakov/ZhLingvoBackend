package zh.config.parser;

import com.google.common.base.Objects;

public class SyntaxError {
    final Type type;
    final String msg;
    final int lineNumber;
    final int position;

    public SyntaxError(Type type, String msg, int lineNumber, int position) {
        this.type = type;
        this.msg = msg;
        this.lineNumber = lineNumber;
        this.position = position;


    }

    @Override
    public String toString() {
        return String.format("Syntax Error Line: %d Position: %d.  (%s) %s",
                lineNumber, position, type.name(), msg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SyntaxError)) return false;
        SyntaxError that = (SyntaxError) o;
        return lineNumber == that.lineNumber && position == that.position && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, lineNumber, position);
    }

    enum Type {
        MISMATCHING_BRACE,
        MISMATCHING_BRACKET,
        MAP_NAME_MISSING,
        SYNTAX
    }
}
