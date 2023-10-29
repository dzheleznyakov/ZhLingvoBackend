package zh.lingvo.rest.controllers;

import com.google.common.collect.ImmutableList;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import zh.lingvo.data.model.Meaning;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.QuizRecord;
import zh.lingvo.data.model.User;
import zh.lingvo.data.services.MeaningService;
import zh.lingvo.data.services.QuizRecordService;
import zh.lingvo.data.services.QuizService;
import zh.lingvo.rest.annotations.ApiController;
import zh.lingvo.rest.commands.QuizRecordCommand;
import zh.lingvo.rest.commands.QuizRecordOverviewCommand;
import zh.lingvo.rest.converters.QuizRecordConverter;
import zh.lingvo.rest.exceptions.InternalError;
import zh.lingvo.rest.exceptions.RequestMalformed;
import zh.lingvo.rest.exceptions.ResourceNotFound;
import zh.lingvo.rest.util.RequestContext;

import java.util.List;
import java.util.Optional;

import static zh.lingvo.util.Preconditions.checkCondition;
import static zh.lingvo.util.Preconditions.checkNotBlank;
import static zh.lingvo.util.Preconditions.checkNotNull;
import static zh.lingvo.util.Preconditions.checkNull;

@ApiController
@RequestMapping("/api/quizzes/{id}/records")
public class QuizRecordController {
    private final QuizRecordService quizRecordService;
    private final QuizService quizService;
    private final MeaningService meaningService;
    private final QuizRecordConverter quizRecordConverter;
    private final RequestContext context;

    public QuizRecordController(
            QuizRecordService quizRecordService,
            QuizService quizService,
            MeaningService meaningService,
            QuizRecordConverter quizRecordConverter,
            RequestContext context
    ) {
        this.quizRecordService = quizRecordService;
        this.quizService = quizService;
        this.meaningService = meaningService;
        this.quizRecordConverter = quizRecordConverter;
        this.context = context;
    }

    @GetMapping("/overviews")
    public List<QuizRecordOverviewCommand> getAllRecordsOverviews(@PathVariable("id") Long quizId) {
        return quizRecordService.findAll(quizId, getUser())
                .stream()
                .map(quizRecordConverter::toOverviewCommand)
                .collect(ImmutableList.toImmutableList());
    }

    @GetMapping
    public List<QuizRecordCommand> getAllRecords(@PathVariable("id") Long quizId) {
        return quizRecordService.findAll(quizId, getUser())
                .stream()
                .map(quizRecordConverter::toCommand)
                .collect(ImmutableList.toImmutableList());
    }

    @GetMapping("/{rid}")
    public QuizRecordCommand getQuizRecord(
            @PathVariable("rid") Long recordId)
    {
        Optional<QuizRecordCommand> optionalRecord = quizRecordService
                .findById(recordId, getUser())
                .map(quizRecordConverter::toCommand);
        if (optionalRecord.isEmpty()) {
            throw new ResourceNotFound(String.format("Quiz record id=[%d] not found", recordId));
        }
        return optionalRecord.get();
    }

    @PostMapping
    public QuizRecordCommand createQuizRecord(
            @PathVariable("id") Long quizId,
            @RequestBody QuizRecordCommand command
    ) {
        checkNull(command::getId,
                () -> new RequestMalformed(String.format(
                        "Quiz record create request should not contain id; found [%d]",
                        command.getId())));
        return quizRecordService
                .create(quizRecordConverter.createQuizRecord(command), quizId, getUser())
                .map(quizRecordConverter::toCommand)
                .orElseThrow(() -> new ResourceNotFound(String.format("Quiz id=[%d] not found", quizId)));
    }

    @PostMapping("/meaning/{mid}")
    public QuizRecordCommand createQuizRecord(
            @PathVariable("id") Long quizId,
            @PathVariable("mid") Long meaningId
    ) {
        User user = getUser();
        Meaning meaning = meaningService.findById(meaningId, user)
                .orElseThrow(() -> new ResourceNotFound(String.format(
                        "Meaning [%d] not found for user [%d]", meaningId, user.getId())));
        Quiz quiz = quizService.findById(quizId, user)
                .orElseThrow(() -> new ResourceNotFound(String.format(
                        "Quiz [%d] not found for user [%d]", quizId, user.getId())));
        QuizRecord quizRecord = quizRecordConverter.createQuizRecord(meaning);
        quizRecord.setQuiz(quiz);
        return quizRecordService
                .create(quizRecord, quizId, user)
                .map(quizRecordConverter::toCommand)
                .orElseThrow(() -> new InternalError(String.format(
                        "Failed to create new quiz record from meaning [%d] for quiz [%d]", meaningId, quizId)));
    }

    @PutMapping
    public QuizRecordCommand updateQuizRecord(
            @PathVariable("id") Long quizId,
            @RequestBody QuizRecordCommand command
    ) {
        checkNotNull(command::getId,
                () -> new RequestMalformed("Cannot update quiz record: no id found in the request"));
        checkNotBlank(command::getPos,
                () -> new RequestMalformed("Cannot update quiz record: no part of speech found in the request"));
        return quizRecordService
                .update(quizRecordConverter.toQuizRecord(command), quizId, getUser())
                .map(quizRecordConverter::toCommand)
                .orElseThrow(() -> new ResourceNotFound(
                        String.format("Quiz record id=[%d] for quiz [%d] not found", command.getId(), quizId)));
    }

    @DeleteMapping("/{rid}")
    public Long deleteQuizRecord(
            @PathVariable("id") Long quizId,
            @PathVariable("rid") Long quizRecordId
    ) {
        boolean successful = quizRecordService.deleteById(quizRecordId, quizId, getUser());
        checkCondition(successful,
                () -> new InternalError(String.format("Failed to delete quiz record [%d]", quizRecordId)));
        return quizRecordId;
    }

    private User getUser() {
        return context.getUser();
    }
}
