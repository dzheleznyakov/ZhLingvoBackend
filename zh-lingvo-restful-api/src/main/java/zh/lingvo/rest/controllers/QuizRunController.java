package zh.lingvo.rest.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import zh.lingvo.data.model.User;
import zh.lingvo.data.services.QuizRunService;
import zh.lingvo.rest.annotations.ApiController;
import zh.lingvo.rest.commands.QuizRunCommand;
import zh.lingvo.rest.converters.QuizRunCommandToQuizRun;
import zh.lingvo.rest.converters.QuizRunToQuizRunCommand;
import zh.lingvo.rest.exceptions.ResourceNotFound;
import zh.lingvo.rest.util.RequestContext;

@ApiController
@RequestMapping("/api/quizzes/{id}/runs")
public class QuizRunController {
    private final QuizRunCommandToQuizRun quizRunCommandConverter;
    private final QuizRunToQuizRunCommand quizRunConverter;
    private final QuizRunService quizRunService;
    private final RequestContext requestContext;

    public QuizRunController(
            QuizRunCommandToQuizRun quizRunCommandConverter,
            QuizRunToQuizRunCommand quizRunConverter,
            QuizRunService quizRunService,
            RequestContext requestContext
    ) {
        this.quizRunCommandConverter = quizRunCommandConverter;
        this.quizRunConverter = quizRunConverter;
        this.quizRunService = quizRunService;
        this.requestContext = requestContext;
    }

    @PostMapping
    public QuizRunCommand newQuizRun(
            @PathVariable("id") Long quizId,
            @RequestBody QuizRunCommand command
    ) {
        return quizRunService.create(quizRunCommandConverter.convert(command), quizId, getUser())
                .map(quizRunConverter::convert)
                .orElseThrow(() -> new ResourceNotFound(String.format(
                        "Quiz run not found when trying to create one for quiz [%d]", quizId)));
    }

    @PutMapping
    public QuizRunCommand updateQuizRun(
            @RequestBody QuizRunCommand command
    ) {
        return quizRunService.update(quizRunCommandConverter.convert(command), getUser())
                .map(quizRunConverter::convert)
                .orElseThrow(() -> new ResourceNotFound(String.format(
                        "Quiz run not found when trying to update quiz run [%d]", command.getId())));
    }

    private User getUser() {
        return requestContext.getUser();
    }
}
