package zh.lingvo.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zh.lingvo.rest.annotations.ApiController;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.services.LanguageService;

import java.util.List;
import java.util.stream.Collectors;

import static zh.lingvo.rest.controllers.ControllersConstants.CONTENT_TYPE;

@ApiController
@RequestMapping(path = "/api/languages", produces = CONTENT_TYPE)
@Slf4j
public class LanguageController {
    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public List<String> getAllLanguages() {
        log.info("Inside of LanguageController.getAllLanguages()");
        return languageService.findAll()
                .stream()
                .map(Language::getName)
                .collect(Collectors.toList());
    }
}
