package zh.lingvo.rest.exceptions;

public class InternalError extends RuntimeException {
    public InternalError(String message) {
        super(message);
    }
}
