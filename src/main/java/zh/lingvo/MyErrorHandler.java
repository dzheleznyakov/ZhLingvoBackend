package zh.lingvo;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class MyErrorHandler extends ErrorHandler {
    private boolean firstRequest = true;

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String redirectRoute = "/";
        if (response.getStatus() == HttpServletResponse.SC_NOT_FOUND && firstRequest) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(redirectRoute);
            if (dispatcher != null) {
                response.reset();
                firstRequest = false;
                dispatch(target, baseRequest, request, response, dispatcher);
            } else if (response.getStatus() == HttpServletResponse.SC_NOT_FOUND && !firstRequest) {
                System.out.println("[ERROR]: Cannot find internal redirect route '" + redirectRoute + "' on 404 error. Will show system 404 page");
            } else {
                super.handle(target, baseRequest, request, response);
            }
        }
    }

    private void dispatch(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response, RequestDispatcher dispatcher) throws IOException {
        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            super.handle(target, baseRequest, request, response);
        } finally {
            firstRequest = true;
        }
    }

    @Override
    protected void writeErrorPageBody(HttpServletRequest request, Writer writer, int code, String message, boolean showStacks) throws IOException {
        writeErrorPageMessage(request, writer, code, message, request.getRequestURI());
    }

    @Override
    protected void writeErrorPageMessage(HttpServletRequest request, Writer writer, int code, String message, String uri) throws IOException {
        String statusMessage = code + " " + message;
        System.out.println("[ERROR]: Problem accessing " + uri + ". " + statusMessage);
        writer.write(statusMessage);
    }
}
