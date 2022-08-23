package zh.lingvo.rest.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.enums.MatchingRegime;
import zh.lingvo.data.model.enums.QuizRegime;
import zh.lingvo.data.services.QuizService;
import zh.lingvo.rest.annotations.ApiController;
import zh.lingvo.rest.commands.QuizSettingsCommand;
import zh.lingvo.rest.converters.QuizToQuizSettingsCommand;
import zh.lingvo.rest.exceptions.ResourceNotFound;
import zh.lingvo.rest.util.RequestContext;

@ApiController
@RequestMapping("/api/quizzes/{id}/settings")
public class QuizSettingsController {
    private final QuizService quizService;
    private final QuizToQuizSettingsCommand quizConverter;
    private final RequestContext context;

    public QuizSettingsController(
            QuizService quizService,
            QuizToQuizSettingsCommand quizConverter,
            RequestContext context
    ) {
        this.quizService = quizService;
        this.quizConverter = quizConverter;
        this.context = context;
    }

    @GetMapping
    public QuizSettingsCommand getSettings(@PathVariable("id") Long id) {
        return quizService.findById(id, getUser())
                .map(quizConverter::convert)
                .orElseThrow(() -> new ResourceNotFound(String.format("Quiz id=[%d] not found", id)));
    }

    @PutMapping
    public QuizSettingsCommand updateSettings(
            @PathVariable("id") Long id,
            @RequestBody QuizSettingsCommand settings
    ) {
        Quiz toSave = quizService.findById(id, getUser())
                .orElseThrow(() -> new ResourceNotFound(String.format("Quiz id=[%d] not found", id)));
        updateSettings(toSave, settings);
        return quizService.save(toSave, getUser())
                .map(quizConverter::convert)
                .orElse(new QuizSettingsCommand());
    }

    private void updateSettings(Quiz quiz, QuizSettingsCommand settings) {
        quiz.setMaxScore(settings.getMaxScore());
        quiz.setMatchingRegime(MatchingRegime.fromCode(settings.getMatchingRegime()));
        quiz.setQuizRegime(QuizRegime.fromCode(settings.getQuizRegime()));
    }

    private User getUser() {
        return context.getUser();
    }
}
