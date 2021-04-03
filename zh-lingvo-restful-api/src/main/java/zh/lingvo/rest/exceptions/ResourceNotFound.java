package zh.lingvo.rest.exceptions;

public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound(String message) {
        super(message);
    }

    public ResourceNotFound(Throwable cause, String message) {
        super(message, cause);
    }
}
