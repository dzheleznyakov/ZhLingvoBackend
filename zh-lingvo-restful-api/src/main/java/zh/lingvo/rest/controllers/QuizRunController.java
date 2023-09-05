package zh.lingvo.rest.controllers;

import com.google.common.collect.ImmutableList;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.QuizRun;
import zh.lingvo.data.model.User;
import zh.lingvo.data.services.QuizRunService;
import zh.lingvo.rest.annotations.ApiController;
import zh.lingvo.rest.commands.QuizRunCommand;
import zh.lingvo.rest.converters.QuizRunCommandToQuizRun;
import zh.lingvo.rest.converters.QuizRunToQuizRunCommand;
import zh.lingvo.rest.exceptions.RequestMalformed;
import zh.lingvo.rest.exceptions.ResourceNotFound;
import zh.lingvo.rest.util.RequestContext;
import zh.lingvo.util.Either;

import java.util.List;

import static zh.lingvo.util.Preconditions.checkCondition;

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

    @GetMapping
    public List<QuizRunCommand> getAllQuizRuns(@PathVariable("id") Long quizId) {
        Quiz quiz = Quiz.builder().id(quizId).build();
        return quizRunService
                .findAllByQuiz(quiz, getUser())
                .stream()
                .map(quizRunConverter::convert)
                .collect(ImmutableList.toImmutableList());
    }

    @GetMapping("/{runId}")
    public QuizRunCommand getQuizRun(@PathVariable("runId") Long quizRunId) {
        return quizRunService.findById(quizRunId, getUser())
                .map(quizRunConverter::convert)
                .orElse(null);
    }

    @PostMapping
    public QuizRunCommand newQuizRun(
            @PathVariable("id") Long quizId,
            @RequestBody QuizRunCommand command
    ) {
        checkCondition(
                command != null && command.getId() == null,
                () -> new RequestMalformed("New quiz run should not have a set id"));
        return quizRunService.create(quizRunCommandConverter.convert(command), quizId, getUser())
                .map(quizRunConverter::convert)
                .orElseThrow(() -> new ResourceNotFound(String.format(
                        "Quiz run not found when trying to create one for quiz [%d]", quizId)));
    }

    @PutMapping
    public QuizRunCommand updateQuizRun(
            @RequestBody QuizRunCommand command
    ) {
        checkCondition(
                command != null && command.getId() != null,
                () -> new RequestMalformed("Quiz run to be updated should have an id"));
        return quizRunService.update(quizRunCommandConverter.convert(command), getUser())
                .map(quizRunConverter::convert)
                .orElseThrow(() -> new ResourceNotFound(String.format(
                        "Quiz run not found when trying to update quiz run [%d]", command.getId())));
    }

    @PutMapping("/{runId}/complete")
    public boolean completeQuizRun(
            @PathVariable("runId") Long runId,
            @PathVariable("id") Long quizId,
            @RequestBody QuizRunCommand command
    ) {
        QuizRun quizRun = quizRunCommandConverter.convert(command);
        Either<QuizRunService.ServiceError, Boolean> result = quizRunService.complete(quizRun, quizId, getUser());
        if (result.isLeft())
            handleOnCompleteErrors(runId, result);
        return true;
    }

    @DeleteMapping("/{runId}")
    public boolean deleteQuizRun(@PathVariable("runId") Long runId) {
        return quizRunService.deleteById(runId, getUser());
    }

    private static void handleOnCompleteErrors(Long runId, Either<QuizRunService.ServiceError, Boolean> result) {
        switch (result.getLeft()) {
            case RECORDS_PRESENT:
                throw new RequestMalformed(String.format(
                        "Exception when completing quiz run [%d]: it should not have remaining records", runId));
            case MISSING_QUIZ_RUN:
                throw new ResourceNotFound(String.format("Quiz run [%d] not found", runId));
        }
    }

    private User getUser() {
        return requestContext.getUser();
    }
}
