package zh.lingvo.controllers;

import com.google.common.collect.ImmutableMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zh.lingvo.ApiMapping;
import zh.lingvo.caches.LanguagesCache;
import zh.lingvo.domain.changepatterns.ChangeModel;
import zh.lingvo.domain.languages.Language;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@ApiMapping
@RequestMapping("/api/words/forms")
public class WordFormsController {
    private final LanguagesCache languagesCache;

    public WordFormsController(LanguagesCache languagesCache) {
        this.languagesCache = languagesCache;
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
                .map(language::getChangePattern)
                .orElse(ChangeModel.EMPTY);
    }

    @GetMapping("/{lang}/{pos}/{wordId}")
    public Object getWordForms(
            @PathVariable("lang") String languageCode,
            @PathVariable("pos") String posName,
            @PathVariable("wordId") UUID wordId
            ) {
        return ImmutableMap.of(
                "SINGULAR;NOMINATIVE", "box",
                "PLURAL;NOMINATIVE", "boxes",
                "SINGULAR;POSSESSIVE", "box's",
                "PLURAL;POSSESSIVE", "boxes'"
        );
    }
}
