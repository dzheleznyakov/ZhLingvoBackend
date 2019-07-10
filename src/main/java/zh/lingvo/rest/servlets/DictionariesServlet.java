package zh.lingvo.rest.servlets;

import zh.lingvo.caches.DictionaryCache;
import zh.lingvo.rest.entities.DictionaryRestEntity;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.util.json.JsonFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DictionariesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Request received: [" + req + "]");
        String[] pathSegments = req.getRequestURI().split("/");
        String languageCode = pathSegments[pathSegments.length - 1];
        Dictionary dictionary = DictionaryCache.get(languageCode);
        DictionaryRestEntity restDictionary = new DictionaryRestEntity(dictionary);
        String json = JsonFactory.toJson(restDictionary);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(json);
    }
}
