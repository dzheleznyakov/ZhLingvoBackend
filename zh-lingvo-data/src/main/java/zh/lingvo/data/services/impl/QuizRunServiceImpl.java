package zh.lingvo.data.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.QuizRun;
import zh.lingvo.data.model.User;
import zh.lingvo.data.repositories.QuizRunRepository;
import zh.lingvo.data.services.QuizRunService;
import zh.lingvo.data.services.QuizService;
import zh.lingvo.util.BasicClock;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class QuizRunServiceImpl implements QuizRunService {
    private final QuizRunRepository quizRunRepository;
    private final QuizService quizService;

    public QuizRunServiceImpl(
            QuizRunRepository quizRunRepository,
            QuizService quizService
    ) {
        this.quizRunRepository = quizRunRepository;
        this.quizService = quizService;
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
        System.out.println("---------");
        System.out.println(quizRun);
        System.out.println("---------");
        return Optional.of(quizRunRepository.save(quizRun));
    }

    @Override
    public Optional<QuizRun> create(QuizRun quizRun, Long quizId, User user) {
        if (quizRun.getId() != null)
            return Optional.empty();
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
