package zh.lingvo.data.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.User;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends CrudRepository<Quiz, Long> {
    List<Quiz> findAllByUser(User user);

    Optional<Quiz> findByIdAndUser(Long id, User user);

    @Query("SELECT q FROM quiz q JOIN FETCH q.quizRecords WHERE q.id = (:id)")
    Optional<Quiz> findByIdWithRecords(@Param("id") Long id);

    boolean existsByIdAndUser(Long id, User user);

}