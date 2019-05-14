package zh.lingvo;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context0 = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context0.setContextPath("/ctx0");
        server.setHandler(context0);

        context0.addServlet(new ServletHolder(new HelloServlet()), "/*");
        context0.addServlet(new ServletHolder(new HelloServlet("Buongiorno Mondo")), "/it/*");
        context0.addServlet(new ServletHolder(new HelloServlet("Bunjour le Monde")), "/fr/*");


        WebAppContext webapp = new WebAppContext();
        String resourceBase = "src/main/resources/public";
        webapp.setContextPath("/*");
        webapp.setResourceBase(resourceBase);
        webapp.setParentLoaderPriority(true);
        webapp.setErrorHandler(new MyErrorHandler());

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { context0, webapp });

        server.setHandler(contexts);

        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }
}
