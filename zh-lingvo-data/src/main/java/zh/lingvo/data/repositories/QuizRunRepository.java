package zh.lingvo.data.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import zh.lingvo.data.model.QuizRun;
import zh.lingvo.data.model.User;

import java.util.List;
import java.util.Optional;

public interface QuizRunRepository extends CrudRepository<QuizRun, Long> {
    @Query("SELECT qr FROM quiz_run qr JOIN qr.quiz q WHERE qr.id = (:id) AND q.user = (:user)")
    Optional<QuizRun> findByIdAndUser(Long id, User user);

    @Query("SELECT qr FROM quiz_run qr JOIN qr.quiz q WHERE q.id = (:quizId) AND q.user = (:user)")
    List<QuizRun> findAllByQuizAndUser(Long quizId, User user);

    @Query("SELECT qr FROM quiz_run qr JOIN qr.quiz q WHERE q.user = (:user)")
    List<QuizRun> findAllByUser(User user);
}
