package zh.lingvo.rest.servlets.fixtures;

import io.reactivex.functions.Function;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpAction<E> {
    private final HttpServletResponse response;
    private final E value;

    public HttpAction(HttpServletResponse response, E value) {
        this.response = response;
        this.value = value;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public E getValue() {
        return value;
    }

    public <T> HttpAction<T> to(T value) {
        return new HttpAction<>(response, value);
    }

    public <T> HttpAction<T> to(Function<E, T> mapper) throws Exception {
        T newValue = mapper.apply(value);
        return to(newValue);
    }

    public void dispatchResponse(int statusCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(statusCode);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(value);
        response.getWriter().close();
    }
}
