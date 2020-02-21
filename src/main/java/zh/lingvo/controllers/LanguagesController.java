package zh.lingvo.controllers;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zh.lingvo.util.ApiMapping;
import zh.lingvo.caches.LanguagesCache;
import zh.lingvo.rest.entities.LanguageRestEntity;

import java.util.List;

@RestController
@ApiMapping
@RequestMapping("/api/languages")
public class LanguagesController {
    @Autowired
    private LanguagesCache languagesCache;

    @GetMapping
    public List<LanguageRestEntity> getLanguages() {
        return languagesCache.get().stream()
                .map(LanguageRestEntity::new)
                .collect(ImmutableList.toImmutableList());
    }
}
