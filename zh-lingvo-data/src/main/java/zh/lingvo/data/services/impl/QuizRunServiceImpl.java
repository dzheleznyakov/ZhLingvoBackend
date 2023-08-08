package zh.lingvo.data.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zh.lingvo.data.fixtures.QuizRunEvaluator;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.QuizRecord;
import zh.lingvo.data.model.QuizRun;
import zh.lingvo.data.model.User;
import zh.lingvo.data.repositories.QuizRecordRepository;
import zh.lingvo.data.repositories.QuizRunRepository;
import zh.lingvo.data.services.QuizRunService;
import zh.lingvo.data.services.QuizService;
import zh.lingvo.util.BasicClock;
import zh.lingvo.util.Either;

import java.util.List;
import java.util.Optional;

import static zh.lingvo.util.CollectionsHelper.getListSafely;

@Service
@Slf4j
public class QuizRunServiceImpl implements QuizRunService {
    private final QuizRunRepository quizRunRepository;
    private final QuizService quizService;
    private final QuizRecordRepository quizRecordRepository;

    public QuizRunServiceImpl(
            QuizRunRepository quizRunRepository,
            QuizService quizService,
            QuizRecordRepository quizRecordRepository) {
        this.quizRunRepository = quizRunRepository;
        this.quizService = quizService;
        this.quizRecordRepository = quizRecordRepository;
    }

    @Override
    public Optional<QuizRun> findById(Long id, User user) {
        Optional<QuizRun> optionalQuizRun = quizRunRepository.findByIdAndUser(id, user);
        optionalQuizRun.ifPresent(qr -> {
            qr.setAccessedTimestamp(BasicClock.get().now());
            quizRunRepository.save(qr);
        });
        return optionalQuizRun;
    }

    @Override
    public List<QuizRun> findAllByQuiz(Quiz quiz, User user) {
        return quizRunRepository.findAllByQuizAndUser(quiz.getId(), user);
    }

    @Override
    public List<QuizRun> findAllByUser(User user) {
        return quizRunRepository.findAllByUser(user);
    }

    @Override
    public Optional<QuizRun> update(QuizRun quizRun, User user) {
        Optional<QuizRun> optionalFoundQuizRun = quizRunRepository
                .findByIdAndUser(quizRun.getId(), user);
        if (optionalFoundQuizRun.isEmpty())
            return Optional.empty();

        QuizRun foundQuizRun = optionalFoundQuizRun.get();
        quizRun.setQuiz(foundQuizRun.getQuiz());
        quizRun.setAccessedTimestamp(BasicClock.get().now());
        return Optional.of(quizRunRepository.save(quizRun));
    }

    @Override
    public Either<ServiceError, Boolean> complete(QuizRun quizRun, Long quizId, User user) {
        if (!getListSafely(quizRun::getRecords).isEmpty())
            return Either.left(ServiceError.RECORDS_PRESENT);
        if (quizRun.getDoneRecords().isEmpty())
            return Either.right(true);

        Optional<QuizRun> optionalFoundQuizRun = quizRunRepository.findByIdAndUser(quizRun.getId(), user);
        if (optionalFoundQuizRun.isEmpty())
            return Either.left(ServiceError.MISSING_QUIZ_RUN);

        Optional<Quiz> optionalFoundQuiz = quizService.findById(quizId, user);
        if (optionalFoundQuiz.isEmpty())
            return Either.right(true);

        List<QuizRecord> quizRecords = getListSafely(optionalFoundQuiz.get()::getQuizRecords);
        QuizRunEvaluator quizRunEvaluator = new QuizRunEvaluator(optionalFoundQuiz.get(), quizRun);
        quizRunEvaluator.evaluate();
        quizRecordRepository.saveAll(quizRecords);

        return Either.right(true);
    }


    @Override
    public Optional<QuizRun> create(QuizRun quizRun, Long quizId, User user) {
        Optional<Quiz> optionalQuiz = quizService.findById(quizId, user);
        if (optionalQuiz.isEmpty())
            return Optional.empty();
        quizRun.setQuiz(optionalQuiz.get());
        return Optional.of(quizRunRepository.save(quizRun));
    }

    @Override
    public boolean deleteById(Long id, User user) {
        Optional<QuizRun> optionalQuizRun = quizRunRepository.findByIdAndUser(id, user);
        if (optionalQuizRun.isEmpty())
            return false;
        try {
            quizRunRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error("Error while deleting quiz run [{}]", id);
            return false;
        }
    }
}
