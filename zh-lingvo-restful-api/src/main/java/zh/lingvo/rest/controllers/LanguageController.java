package zh.lingvo.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.services.LanguageService;
import zh.lingvo.rest.annotations.ApiController;
import zh.lingvo.rest.util.RequestContext;

import java.util.List;
import java.util.stream.Collectors;

import static zh.lingvo.rest.controllers.ControllersConstants.CONTENT_TYPE;

@ApiController
@RequestMapping(path = "/api/languages", produces = CONTENT_TYPE)
@Slf4j
public class LanguageController {
    private final LanguageService languageService;
    private final RequestContext requestContext;

    public LanguageController(LanguageService languageService, RequestContext requestContext) {
        this.languageService = languageService;
        this.requestContext = requestContext;
    }

    @GetMapping
    public List<String> getAllLanguages() {
        log.info(requestContext.getUser().toString());
        return languageService.findAll()
                .stream()
                .map(Language::getName)
                .collect(Collectors.toList());
    }
}
