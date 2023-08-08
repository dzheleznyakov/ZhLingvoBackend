package zh.lingvo.data.services;

import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.QuizRun;
import zh.lingvo.data.model.User;
import zh.lingvo.util.Either;

import java.util.List;
import java.util.Optional;

public interface QuizRunService {
    Optional<QuizRun> findById(Long id, User user);

    List<QuizRun> findAllByQuiz(Quiz quiz, User user);

    List<QuizRun> findAllByUser(User user);

    Optional<QuizRun> update(QuizRun quizRun, User user);

    Either<ServiceError, Boolean> complete(QuizRun quizRun, Long quizId, User user);

    Optional<QuizRun> create(QuizRun quizRun, Long quizId, User user);

    boolean deleteById(Long id, User user);

    enum ServiceError {
        RECORDS_PRESENT,
        MISSING_QUIZ_RUN
    }
}
