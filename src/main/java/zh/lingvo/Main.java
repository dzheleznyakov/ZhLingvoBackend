package zh.lingvo;

import org.eclipse.jetty.rewrite.handler.RedirectPatternRule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.rewrite.handler.RewritePatternRule;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Main extends AbstractHandler {
    @Override
    public void handle(
            String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Hello World<h2>");
        baseRequest.setHandled(true);
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        String resourceBase = "src/main/resources/public";

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(resourceBase);
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setDirAllowed(true);

        ContextHandler contextHandler = new ContextHandler(".");
        contextHandler.setHandler(resourceHandler);

        ContextHandler contextHandler1 = new ContextHandler("./language");
        contextHandler.setHandler(resourceHandler);

//        RewriteHandler rewriteHandler = new RewriteHandler();
//        rewriteHandler.setRewriteRequestURI(true);
//        rewriteHandler.setRewritePathInfo(true);
//
//        RewritePatternRule redirectRule = new RewritePatternRule();
//        redirectRule.setPattern("/dictionary");
//        redirectRule.setReplacement(".");
//        rewriteHandler.addRule(redirectRule);


        server.setHandler(contextHandler);
        server.setHandler(contextHandler1);
//        server.setHandler(rewriteHandler);

        server.start();
        server.join();
    }
}
