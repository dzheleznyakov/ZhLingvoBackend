package zh.lingvo.rest.servlets;

import com.google.common.collect.ImmutableList;
import zh.lingvo.util.ConfigReader;
import zh.lingvo.util.json.JsonFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PartOfSpeechServlet extends HttpServlet {
    private static final ConfigReader config = ConfigReader.get();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] pathSegments = req.getRequestURI().split("/");
        String languageCode = pathSegments[pathSegments.length - 1];
        List<String> partsOfSpeech = getPartsOfSpeeches(languageCode);
        String json = JsonFactory.toJson(partsOfSpeech);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(json);
    }

    private List<String> getPartsOfSpeeches(String languageCode) {
        String partsOfSpeechesPath = "partsOfSpeeches";
        Map<String, List<String>> map = config.getAsMap("languages",
                c -> c.getAsString("code"),
                c -> c.hasPath(partsOfSpeechesPath) ? c.getAsStringList(partsOfSpeechesPath) : ImmutableList.of());
        return map.getOrDefault(languageCode, ImmutableList.of());
    }
}
