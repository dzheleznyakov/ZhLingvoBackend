package zh.lingvo.controllers;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zh.lingvo.ApiMapping;
import zh.lingvo.caches.DictionaryCache;
import zh.lingvo.caches.LanguagesCache;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.domain.words.Word;
import zh.lingvo.persistence.xml.XmlWriter;
import zh.lingvo.rest.entities.word.WordRestEntity;
import zh.lingvo.util.ConfigReader;

import java.util.UUID;

@RestController
@ApiMapping
@RequestMapping("/api/words")
public class WordsController {
    private static final Gson GSON = new Gson();
    private static final ConfigReader config = ConfigReader.get();
    private static final String dictionariesLocation = config.getString("dictionariesLocation");

    private DictionaryCache dictionaryCache;
    private LanguagesCache languagesCache;
    private XmlWriter writer;

    public WordsController(DictionaryCache dictionaryCache, LanguagesCache languagesCache, XmlWriter writer) {
        this.dictionaryCache = dictionaryCache;
        this.languagesCache = languagesCache;
        this.writer = writer;
    }

    @GetMapping("/{lang}/{id}")
    public WordRestEntity getWord(
            @PathVariable("id") UUID id,
            @PathVariable("lang") String languageCode) {
        Dictionary dictionary = dictionaryCache.get(languageCode);
        Word word = dictionary.get(id);

        return new WordRestEntity(word, languagesCache.get(languageCode));
    }

    @PostMapping("/{wordName}/{lang}")
    public UUID createWord(
            @PathVariable("wordName") String wordName,
            @PathVariable("lang") String languageCode) {
        UUID id = UUID.randomUUID();
        Word word = new Word(id, wordName);
        Dictionary dictionary = dictionaryCache.get(languageCode);
        dictionary.add(word);
        writer.saveDictionary(dictionary, dictionariesLocation + languageCode.toLowerCase() + "_dictionary.xml");
        return id;
    }

    @PutMapping("/{lang}")
    public WordRestEntity saveWord(
            @PathVariable("lang") String languageCode,
            @RequestBody String wordJson) {
        System.out.println("languageCode = " + languageCode);
        System.out.println("wordJson = " + wordJson);
//        Word word = GSON.fromJson(wordJson, Word.class);
//        Dictionary dictionary = dictionaryCache.get(languageCode);
//        dictionary.set(word);
//        writer.saveDictionary(dictionary, dictionariesLocation + languageCode.toLowerCase() + "_dictionary.xml");
//        return new WordRestEntity(word, languagesCache.get(languageCode));
        return null;
    }

}
