package zh.lingvo.controllers;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import zh.lingvo.domain.LingvoException;
import zh.lingvo.domain.words.Word;
import zh.lingvo.persistence.xml.XmlWriter;
import zh.lingvo.rest.Payload;
import zh.lingvo.rest.entities.word.WordRestEntity;
import zh.lingvo.util.ConfigReader;

import java.nio.file.Paths;
import java.util.UUID;

@RestController
@ApiMapping
@RequestMapping("/api/words")
public class WordsController {
    private static final ConfigReader config = ConfigReader.get();

    @Value("${app.dictionaries.location}")
    private String dictionariesLocation;

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
            @PathVariable("lang") String languageCode) throws LingvoException {
        Dictionary dictionary = dictionaryCache.get(languageCode);
        Word word = dictionary.get(id);
        if (word == null)
            throw new LingvoException(String.format("Word for id [%s] not found", id));
        return new WordRestEntity(word, languagesCache.get(languageCode));
    }

    @PostMapping("/{lang}")
    public UUID createWord(
            @PathVariable("lang") String languageCode,
            @RequestBody Payload payload
    ) {
        String wordName = payload.getData();
        Dictionary dictionary = dictionaryCache.get(languageCode);
        if (dictionary.contains(wordName))
            return dictionary.get(wordName).getId();

        UUID id = UUID.randomUUID();
        Word word = new Word(id, wordName);
        dictionary.add(word);
        String fileName = Paths.get(dictionariesLocation).resolve(languageCode.toLowerCase() + "_dictionary.xml").toString();
        writer.saveDictionary(dictionary, fileName);
        return id;
    }


    @DeleteMapping("/{lang}/{id}")
    public WordRestEntity deleteWord(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId
    ) {
        Dictionary dictionary = dictionaryCache.get(languageCode);
        Word removedWord = dictionary.remove(wordId);
        writer.saveDictionary(dictionary, getDictionaryLocation(languageCode));
        return new WordRestEntity(removedWord, languagesCache.get(languageCode));
    }

    @NotNull
    private String getDictionaryLocation(String languageCode) {
        return Paths.get(dictionariesLocation).resolve(languageCode.toLowerCase() + "_dictionary.xml").toString();
    }

}
