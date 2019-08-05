package zh.lingvo.rest.servlets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import org.jetbrains.annotations.NotNull;
import zh.lingvo.caches.DictionaryCache;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.domain.words.Word;
import zh.lingvo.persistence.Writer;
import zh.lingvo.persistence.xml.XmlWriter;
import zh.lingvo.rest.entities.DictionaryRestEntity;
import zh.lingvo.rest.entities.JsonWordFactory;
import zh.lingvo.rest.entities.word.WordRestEntity;
import zh.lingvo.rest.servlets.fixtures.HttpAction;
import zh.lingvo.util.ConfigReader;
import zh.lingvo.util.json.JsonFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DictionariesServlet extends HttpServlet {
    private static final ConfigReader config = ConfigReader.get();
    private static final String dictionariesLocation = config.getString("dictionariesLocation");

    private final Gson GSON = new Gson();
    private final Writer writer = new XmlWriter();

    private final PublishSubject<HttpAction<HttpServletRequest>> getDictionarySubject = PublishSubject.create();
    private final Disposable getDictionarySubscription = getDictionarySubject.subscribeOn(Schedulers.io())
            .map(action -> action.to(this::getLanguageCode))
            .map(action -> action.to(this::getDictionaryRestEntity))
            .observeOn(Schedulers.computation())
            .map(action -> action.to(JsonFactory::toJson))
            .observeOn(Schedulers.io())
            .subscribe(this::dispatchGetResponse, e -> onError(e, "GET", "/api/dictionaries/{lang}"));

    private final PublishSubject<HttpAction<HttpServletRequest>> postDictionarySubject = PublishSubject.create();
    private final Disposable postDictionarySubscription = postDictionarySubject.subscribeOn(Schedulers.io())
            .map(action -> action.to(this::parsePostRequest))
            .observeOn(Schedulers.computation())
            .map(action -> action.to(this::getUpdatedDictionary))
            .observeOn(Schedulers.single())
            .map(action -> action.to(this::saveDictionary))
            .observeOn(Schedulers.io())
            .subscribe(this::dispatchPostRespond, e -> onError(e, "POST", "/api/dictionaries/{lang}"));

    private void onError(Throwable error, String restVerb, String path) {
        System.out.println(String.format("Exception when processing [%s] request to [%s]", restVerb, path));
        error.printStackTrace();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Request received: [" + req + "]");
        dispatchAction(getDictionarySubject, req, resp);
    }

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        System.out.println("Request received: [" + req + "]");
//        String[] pathSegments = req.getRequestURI().split("/");
//        String languageCode = pathSegments[pathSegments.length - 1];
//        Dictionary dictionary = DictionaryCache.get(languageCode);
//        DictionaryRestEntity restDictionary = new DictionaryRestEntity(dictionary);
//        String json = JsonFactory.toJson(restDictionary);
//
//        resp.setStatus(HttpServletResponse.SC_OK);
//        resp.setCharacterEncoding("UTF-8");
//        resp.getWriter().println(json);
//    }

    @NotNull
    private DictionaryRestEntity getDictionaryRestEntity(String languageCode) {
        Dictionary dictionary = DictionaryCache.get(languageCode);
        return new DictionaryRestEntity(dictionary);
    }

    private String getLanguageCode(@NotNull HttpServletRequest req) {
        String[] pathSegments = req.getRequestURI().split("/");
        return pathSegments[pathSegments.length - 1];
    }

    private void dispatchGetResponse(@NotNull HttpAction<String> action) throws IOException {
        action.dispatchResponse(HttpServletResponse.SC_OK);
        System.out.println("[GET] request to [/api/dictionaries/{lang}] has been processed");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Request received: [" + req + "]");
        dispatchAction(this.postDictionarySubject, req, resp);
    }

    private void dispatchAction(@NotNull Observer<HttpAction<HttpServletRequest>> observer, HttpServletRequest req, HttpServletResponse resp) {
        observer.onNext(new HttpAction<>(resp, req));
    }

    private ImmutableMap<String, String> parsePostRequest(@NotNull HttpServletRequest req) throws IOException {
        String languageCode = getLanguageCode(req);
        String jsonDictionary = getBody(req);
        return ImmutableMap.of("code", languageCode, "json", jsonDictionary);
    }

    private String getBody(@NotNull HttpServletRequest req) throws IOException {
        return req.getReader().lines()
                .collect(Collectors.joining("\n"));
    }

    private Dictionary getUpdatedDictionary(@NotNull ImmutableMap<String, String> params) {
        String languageCode = params.get("code");
        String jsonDictionary = params.get("json");
        List<WordRestEntity> jsonWords = GSON.fromJson(jsonDictionary, new TypeToken<List<WordRestEntity>>() {}.getType());
        List<Word> words = jsonWords.stream()
                .map(jsonWord -> JsonWordFactory.getWord(jsonWord, languageCode))
                .collect(ImmutableList.toImmutableList());
        Dictionary dictionary = DictionaryCache.get(languageCode);
        dictionary.setWords(words);
        return dictionary;
    }

    private String saveDictionary(@NotNull Dictionary dictionary) {
        String languageCode = dictionary.getLanguage().getCode();
        writer.saveDictionary(dictionary, dictionariesLocation + languageCode.toLowerCase() + "_dictionary.xml");
        return languageCode;
    }

    private void dispatchPostRespond(@NotNull HttpAction<String> action) throws IOException {
        String languageCode = action.getValue();
        action.dispatchResponse(HttpServletResponse.SC_OK);
        System.out.println(String.format("[POST] request to [/api/dictionaries/%s] has been processed", languageCode));
    }

    public void close() {
        getDictionarySubscription.dispose();
        postDictionarySubscription.dispose();
    }
}
