package zh.lingvo.controllers;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zh.lingvo.ApiMapping;
import zh.lingvo.caches.DictionaryCache;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.domain.words.Word;
import zh.lingvo.rest.entities.DictionaryRestEntity;
import zh.lingvo.rest.entities.JsonWordFactory;
import zh.lingvo.rest.entities.word.WordRestEntity;

import java.util.List;

@RestController
@ApiMapping
@RequestMapping("/api/dictionaries")
public class DictionariesController {

    @Value("${app.dictionaries.location}")
    private String dictionariesLocation;

    @Autowired
    private DictionaryCache dictionaryCache;

    @GetMapping("/{lang}")
    public DictionaryRestEntity getDictionary(@PathVariable("lang") String languageCode) {
        Dictionary dictionary = dictionaryCache.get(languageCode);
        return new DictionaryRestEntity(dictionary);
    }

    @PutMapping("/{lang}")
    public void saveDictionary(@PathVariable("lang") String languageCode, @RequestBody List<WordRestEntity> jsonWords) {
        List<Word> words = jsonWords.stream()
                .map(jsonWord -> JsonWordFactory.getWord(jsonWord, languageCode))
                .collect(ImmutableList.toImmutableList());
        Dictionary dictionary = dictionaryCache.get(languageCode);
        dictionary.setWords(words);
    }
}
