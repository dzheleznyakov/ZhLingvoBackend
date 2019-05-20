package zh.lingvo;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import zh.lingvo.rest.LanguagesServlet;

public class Main {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler apiContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        apiContext.setContextPath("/api");
        server.setHandler(apiContext);

        apiContext.addServlet(new ServletHolder(new LanguagesServlet()), "/languages");


        WebAppContext webapp = new WebAppContext();
        String resourceBase = "src/main/resources/public";
        webapp.setContextPath("/*");
        webapp.setResourceBase(resourceBase);
        webapp.setParentLoaderPriority(true);
        webapp.setErrorHandler(new MyErrorHandler());

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { apiContext, webapp });

        server.setHandler(contexts);

        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }
}
