package zh.lingvo.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zh.lingvo.data.services.LanguageService;
import zh.lingvo.rest.annotations.ApiController;
import zh.lingvo.rest.commands.LanguageCommand;
import zh.lingvo.rest.converters.LanguageToLanguageCommand;

import java.util.List;
import java.util.stream.Collectors;

import static zh.lingvo.rest.controllers.ControllersConstants.CONTENT_TYPE;

@ApiController
@RequestMapping(path = "/api/languages", produces = CONTENT_TYPE)
@Slf4j
public class LanguageController {
    private final LanguageService languageService;
    private final LanguageToLanguageCommand languageConverter;

    public LanguageController(
            LanguageService languageService,
            LanguageToLanguageCommand languageConverter
    ) {
        this.languageService = languageService;
        this.languageConverter = languageConverter;
    }

    @GetMapping
    public List<LanguageCommand> getAllLanguages() {
        return languageService.findAll()
                .stream()
                .map(languageConverter::convert)
                .collect(Collectors.toList());
    }
}
