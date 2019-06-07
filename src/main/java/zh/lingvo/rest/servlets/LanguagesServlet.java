package zh.lingvo.rest.servlets;

import zh.lingvo.rest.entities.LanguageEntity;
import zh.lingvo.util.ConfigReader;
import zh.lingvo.util.json.JsonFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class LanguagesServlet extends HttpServlet {
    private static final ConfigReader config = ConfigReader.get();
    private static List<LanguageEntity> languages;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<LanguageEntity> languages = getLanguages();
        String json = JsonFactory.toJson(languages);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(json);
    }

    private List<LanguageEntity> getLanguages() {
        if (languages == null)
            initialiseLanguages();
        return languages;
    }

    private void initialiseLanguages() {
        languages = config.getAsList("languages", LanguageEntity::new, Comparator.comparing(LanguageEntity::getCode));
    }
}
