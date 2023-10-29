package zh.lingvo.rest.controllers;

import com.google.common.collect.ImmutableList;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.User;
import zh.lingvo.data.services.LanguageService;
import zh.lingvo.data.services.QuizService;
import zh.lingvo.rest.annotations.ApiController;
import zh.lingvo.rest.commands.QuizCommand;
import zh.lingvo.rest.converters.LanguageCommandToLanguage;
import zh.lingvo.rest.converters.QuizToQuizCommand;
import zh.lingvo.rest.exceptions.InternalError;
import zh.lingvo.rest.exceptions.RequestMalformed;
import zh.lingvo.rest.exceptions.ResourceNotFound;
import zh.lingvo.rest.util.RequestContext;

import java.util.List;

import static zh.lingvo.util.Preconditions.checkCondition;
import static zh.lingvo.util.Preconditions.checkNull;

@ApiController
@RequestMapping("/api/quizzes")
public class QuizController {
    private final QuizService quizService;
    private final LanguageService languageService;
    private final QuizToQuizCommand quizConverter;
    private final LanguageCommandToLanguage languageCommandConverter;
    private final RequestContext requestContext;

    public QuizController(
            QuizService quizService,
            LanguageService languageService,
            QuizToQuizCommand quizConverter,
            LanguageCommandToLanguage languageCommandConverter, RequestContext requestContext
    ) {
        this.quizService = quizService;
        this.languageService = languageService;
        this.quizConverter = quizConverter;
        this.languageCommandConverter = languageCommandConverter;
        this.requestContext = requestContext;

    }

    @GetMapping
    public List<QuizCommand> getAllQuizzes(
            @RequestParam(name = "lang", required = false) String langCode
    ) {
        return getQuizzes(langCode)
                .stream()
                .map(quizConverter::convert)
                .collect(ImmutableList.toImmutableList());
    }

    private List<Quiz> getQuizzes(String langCode) {
        if (langCode != null)
            return languageService.findByTwoLetterCode(langCode)
                    .map(language -> quizService.findAll(getUser(), language))
                    .orElse(ImmutableList.of());

        return quizService.findAll(getUser());
    }

    @GetMapping("/{id}")
    public QuizCommand getQuiz(@PathVariable("id") Long id) {
        return quizService.findById(id, getUser())
                .map(quizConverter::convert)
                .orElseThrow(() -> new ResourceNotFound(String.format("Quiz id=[%d] not found", id)));
    }

    @PostMapping
    public QuizCommand createNewQuiz(@RequestBody QuizCommand command) {
        checkNull(command::getId,
                () -> new RequestMalformed(String.format("Quiz create request should not contain id; found [%d]", command.getId())));
        return quizService.save(newQuiz(command), getUser())
                .map(quizConverter::convert)
                .orElse(new QuizCommand());
    }

    @PutMapping
    public QuizCommand updateQuiz(@RequestBody QuizCommand command) {
        Quiz toSave = quizService.findById(command.getId(), getUser())
                .orElseThrow(() -> new ResourceNotFound(String.format("Quiz id=[%d] not found", command.getId())));
        Quiz.merge(toSave, newQuiz(command));
        return quizService.save(toSave, getUser())
                .map(quizConverter::convert)
                .orElse(new QuizCommand());
    }

    @DeleteMapping("/{id}")
    public Long deleteQuiz(@PathVariable("id") Long id) {
        boolean successful = quizService.deleteById(id, getUser());
        checkCondition(successful,
                () -> new InternalError(String.format("Failed to delete quiz [%d]", id)));
        return id;
    }

    private Quiz newQuiz(QuizCommand command) {
        Quiz quiz = new Quiz();
        quiz.setName(command.getName());
        quiz.setLanguage(languageCommandConverter.convert(command.getTargetLanguage()));
        return quiz;
    }

    private User getUser() {
        return requestContext.getUser();
    }
}
