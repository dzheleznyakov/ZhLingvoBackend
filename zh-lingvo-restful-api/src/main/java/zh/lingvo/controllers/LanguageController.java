package zh.lingvo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zh.lingvo.annotations.ApiController;
import zh.lingvo.data.domain.Language;
import zh.lingvo.data.services.LanguageService;

import java.util.List;
import java.util.stream.Collectors;

import static zh.lingvo.controllers.ControllersConstants.CONTENT_TYPE;

@ApiController
@RequestMapping(path = "/api/languages", produces = CONTENT_TYPE)
public class LanguageController {
    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public List<String> getAllLanguages() {
        return languageService.findAll()
                .stream()
                .map(Language::getName)
                .collect(Collectors.toList());
    }
}
