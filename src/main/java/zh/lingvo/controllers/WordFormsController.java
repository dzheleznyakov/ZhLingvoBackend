package zh.lingvo.controllers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zh.lingvo.caches.DictionaryCache;
import zh.lingvo.caches.LanguagesCache;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.changepatterns.ChangeModel;
import zh.lingvo.domain.forms.WordForms;
import zh.lingvo.domain.languages.Language;
import zh.lingvo.domain.words.Name;
import zh.lingvo.domain.words.Word;
import zh.lingvo.persistence.Writer;
import zh.lingvo.persistence.xml.PersistenceManager;
import zh.lingvo.rest.entities.forms.WordFormsEntity;
import zh.lingvo.util.ApiMapping;
import zh.lingvo.util.CollectionUtils;
import zh.lingvo.util.Pair;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.google.common.base.MoreObjects.firstNonNull;

@SuppressWarnings("unused")
@RestController
@ApiMapping
@RequestMapping("/api/words/forms")
public class WordFormsController {
    @Value("${app.dictionaries.location}")
    private String dictionariesLocation;

    private final LanguagesCache languagesCache;
    private final DictionaryCache dictionaryCache;
    private Writer writer;

    public WordFormsController(LanguagesCache languagesCache, DictionaryCache dictionaryCache, PersistenceManager writer) {
        this.languagesCache = languagesCache;
        this.dictionaryCache = dictionaryCache;
        this.writer = writer;
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
    public WordFormsEntity getWordForms(
            @PathVariable("lang") String languageCode,
            @PathVariable("pos") String posName,
            @PathVariable("wordId") UUID wordId
            ) {
        Language language = languagesCache.get(languageCode);
        Dictionary dictionary = dictionaryCache.get(languageCode);
        Word word = dictionary.get(wordId);
        PartOfSpeech pos = language.getPos(posName);
        return getWordForms(language, word, pos);
    }

    @PutMapping("/{lang}/{pos}/{wordId}")
    public WordFormsEntity editWordForm(
            @PathVariable("lang") String languageCode,
            @PathVariable("pos") String posName,
            @PathVariable("wordId") UUID wordId,
            @RequestBody Map<String, String> forms
    ) {
        Language language = languagesCache.get(languageCode);
        PartOfSpeech pos = language.getPos(posName);
        Dictionary dictionary = dictionaryCache.get(languageCode);
        Word word = dictionary.get("man");
        forms.entrySet().stream()
                .map(entry -> Pair.from(language.getForm(pos, entry.getKey()), entry.getValue()))
                .forEach(formValuePair -> {
                    Map<PartOfSpeech, List<Name>> oldFormExceptions = firstNonNull(word.getFormExceptions(), ImmutableMap.of());
                    List<Name> names = firstNonNull(oldFormExceptions.get(pos), ImmutableList.of());
                    Name nameToUpdate = CollectionUtils.toImmutableList(names).stream()
                            .filter(name -> Arrays.equals(formValuePair.getFirst(), name.getForm()))
                            .findAny()
                            .orElse(new Name());

                    names = CollectionUtils.add(names, nameToUpdate);
                    nameToUpdate.setForm(formValuePair.getFirst());
                    nameToUpdate.setValue(formValuePair.getSecond());

                    ImmutableMap.Builder<PartOfSpeech, List<Name>> newFormExceptionsBuilder = ImmutableMap.builder();
                    oldFormExceptions.entrySet().stream()
                            .filter(entry -> !Objects.equals(pos, entry.getKey()))
                            .forEach(newFormExceptionsBuilder::put);
                    newFormExceptionsBuilder.put(pos, names);
                    ImmutableMap<PartOfSpeech, List<Name>> newFormExceptions = newFormExceptionsBuilder.build();
                    word.setFormExceptions(newFormExceptions);
                });
        writer.saveDictionary(dictionary, getDictionaryLocation(languageCode));
        return getWordForms(language, word, pos);
    }

    @NotNull
    private String getDictionaryLocation(String languageCode) {
        return Paths.get(dictionariesLocation).resolve(languageCode.toLowerCase() + "_dictionary.xml").toString();
    }

    private WordFormsEntity getWordForms(Language language, Word word, PartOfSpeech pos) {
        WordForms wordForms = language.getWordForms(word, pos);
        return new WordFormsEntity(wordForms);
    }
}
