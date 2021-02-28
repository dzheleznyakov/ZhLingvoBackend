package zh.lingvo.rest.exceptions;

public class RequestNotAuthorised extends RuntimeException {
    public RequestNotAuthorised(String message) {
        super(message);
    }
}
