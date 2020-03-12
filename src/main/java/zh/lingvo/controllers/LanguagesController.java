package zh.lingvo.controllers;

import com.google.common.collect.ImmutableList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zh.lingvo.caches.LanguagesCache;
import zh.lingvo.domain.languages.Language;
import zh.lingvo.rest.entities.LanguageConstantsEntity;
import zh.lingvo.rest.entities.LanguageRestEntity;
import zh.lingvo.util.ApiMapping;

import java.util.List;

@RestController
@ApiMapping
@RequestMapping("/api/languages")
public class LanguagesController {
    private LanguagesCache languagesCache;

    public LanguagesController(LanguagesCache languagesCache) {
        this.languagesCache = languagesCache;
    }

    @GetMapping
    public List<LanguageRestEntity> getLanguages() {
        return languagesCache.get().stream()
                .map(LanguageRestEntity::new)
                .collect(ImmutableList.toImmutableList());
    }

    @GetMapping("/constants/{lang}")
    public LanguageConstantsEntity getPartOfSpeeches(@PathVariable("lang") String languageCode) {
        Language language = languagesCache.get(languageCode);
        ImmutableList<String> genders = language.getGenders()
                .stream()
                .map(language::getGenderName)
                .collect(ImmutableList.toImmutableList());

        LanguageConstantsEntity languageConstants = new LanguageConstantsEntity();
        languageConstants.setPos(language.getPosNamings());
        languageConstants.setGenders(language.getGenderNamings());
        return languageConstants;
    }
}
