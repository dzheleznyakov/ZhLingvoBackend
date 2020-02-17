package zh.lingvo.controllers;

import com.google.common.collect.ImmutableMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zh.lingvo.ApiMapping;
import zh.lingvo.caches.DictionaryCache;
import zh.lingvo.caches.LanguagesCache;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.domain.LinguisticCategory;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.changepatterns.ChangeModel;
import zh.lingvo.domain.languages.Language;
import zh.lingvo.domain.words.Word;
import zh.lingvo.util.Pair;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@RestController
@ApiMapping
@RequestMapping("/api/words/forms")
public class WordFormsController {
    private final LanguagesCache languagesCache;
    private final DictionaryCache dictionaryCache;

    public WordFormsController(LanguagesCache languagesCache, DictionaryCache dictionaryCache) {
        this.languagesCache = languagesCache;
        this.dictionaryCache = dictionaryCache;
    }

    @GetMapping("/models/{lang}/{pos}")
    public ChangeModel getChangeModel(
            @PathVariable("lang") String languageCode,
            @PathVariable("pos") String posName
    ) {
        Language language = languagesCache.get(languageCode);
        return language.getPosNamings()
                .entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), posName))
                .findAny()
                .map(Map.Entry::getKey)
                .map(language::getChangeModel)
                .orElse(ChangeModel.EMPTY);
    }

    @GetMapping("/{lang}/{pos}/{wordId}")
    public Map<String, String> getWordForms(
            @PathVariable("lang") String languageCode,
            @PathVariable("pos") String posName,
            @PathVariable("wordId") UUID wordId
            ) {
        Language language = languagesCache.get(languageCode);
        Dictionary dictionary = dictionaryCache.get(languageCode);
        Word word = dictionary.get(wordId);
        PartOfSpeech pos = language.getPosNamings().entrySet()
                .stream()
                .filter((entry -> Objects.equals(entry.getValue(), posName)))
                .map(Map.Entry::getKey)
                .findAny()
                .orElse(null);
        Map<LinguisticCategory[], String> wordForms = language.getWordForms(word, pos);
        return wordForms.entrySet().stream()
                .map(entry -> {
                    String key = Arrays.stream(entry.getKey())
                            .filter(e -> Enum.class.isAssignableFrom(e.getClass()))
                            .map(Enum.class::cast)
                            .map(Enum::name)
                            .collect(Collectors.joining(";"));
                    return Pair.from(key, entry.getValue());
                })
                .collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond));
    }

    @GetMapping("/{lang}/{pos}")
    public void editWordForm(
            @PathVariable("lang") String languageCode,
            @PathVariable("pos") String posName
    ) {
        // localhost:8080/api/words/forms/En/noun
        System.out.println("languageCode = " + languageCode);
        System.out.println("posName = " + posName);
        Language language = languagesCache.get(languageCode);
        Dictionary dictionary = dictionaryCache.get(languageCode);

        Word word = dictionary.get("man");
        System.out.println(word);
    }
}
