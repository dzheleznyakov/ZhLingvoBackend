package zh.lingvo.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.User;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends CrudRepository<Quiz, Long> {
    List<Quiz> findAllByUser(User user);

    Optional<Quiz> findByIdAndUser(Long id, User user);

    boolean existsByIdAndUser(Long id, User user);
}