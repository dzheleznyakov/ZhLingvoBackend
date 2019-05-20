package zh.lingvo.rest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class LanguagesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("HERE");
        resp.setStatus(HttpServletResponse.SC_OK);
        ImmutableMap.Builder<Object, Object> map = ImmutableMap.builder()
                .put(1, 'a')
                .put(2, 'b');
        Gson gson = new Gson();
        String s = gson.toJson(map);
        System.out.println("------");
        System.out.println(s);
        System.out.println("------");
        JsonObject json = new JsonObject();
        json.addProperty("1", "a");
        json.addProperty("b", 300);

        JsonElement json1 = new JsonPrimitive(100500);
        s = gson.toJson(json1);
        System.out.println("------");
        System.out.println(s);
        System.out.println("------");

        resp.getWriter().println(s);
    }
}
