package zh.lingvo.controllers;

import com.google.common.collect.ImmutableList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zh.lingvo.util.ApiMapping;
import zh.lingvo.caches.LanguagesCache;
import zh.lingvo.domain.languages.Language;

import java.util.List;

@RestController
@ApiMapping
@RequestMapping("/api/partsOfSpeeches")
public class PartOfSpeechController {
    private final LanguagesCache languagesCache;

    public PartOfSpeechController(LanguagesCache languagesCache) {
        this.languagesCache = languagesCache;
    }

    @GetMapping("/{lang}")
    public List<String> getPartOfSpeeches(@PathVariable("lang") String languageCode) {
        Language language = languagesCache.get(languageCode);
        return language.getPartsOfSpeech()
                .stream()
                .map(language::getPartsOfSpeechName)
                .collect(ImmutableList.toImmutableList());
    }
}
