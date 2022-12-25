package zh.lingvo.data.services;

import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.User;

import java.util.List;
import java.util.Optional;

public interface QuizService {
    Optional<Quiz> findById(Long id, User user);

    boolean existsById(Long id, User user);

    List<Quiz> findAll(User user);

    List<Quiz> findAll(User user, Language language);

    Optional<Quiz> save(Quiz quiz, User user);

    boolean deleteById(Long id, User user);
}
