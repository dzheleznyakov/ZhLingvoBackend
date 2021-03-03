package zh.lingvo.rest.exceptions;

public class RequestMalformed extends RuntimeException {
    public RequestMalformed(String message) {
        super(message);
    }
}
