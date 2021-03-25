package zh.lingvo.data.exceptions;

public class FailedToPersist extends RuntimeException {
    public FailedToPersist(Throwable cause, String pattern, Object... params) {
        super(String.format(pattern, params), cause);
    }

    public FailedToPersist(String pattern, Object... params) {
        super(String.format(pattern, params));
    }
}
