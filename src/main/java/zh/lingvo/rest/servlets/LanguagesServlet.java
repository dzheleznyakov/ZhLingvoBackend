package zh.lingvo.rest.servlets;

import com.google.common.collect.ImmutableList;
import zh.lingvo.caches.LanguagesCache;
import zh.lingvo.rest.entities.LanguageRestEntity;
import zh.lingvo.util.json.JsonFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class LanguagesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Request received: [" + req + "]");
        List<LanguageRestEntity> languages = getLanguages();
        String json = JsonFactory.toJson(languages);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(json);
    }

    private List<LanguageRestEntity> getLanguages() {
        return LanguagesCache.get().stream()
                .map(LanguageRestEntity::new)
                .collect(ImmutableList.toImmutableList());
    }
}
