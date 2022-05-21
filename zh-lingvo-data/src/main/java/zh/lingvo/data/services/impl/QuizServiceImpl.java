package zh.lingvo.data.services.impl;

import lombok.extern.slf4j.Slf4j;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.User;
import zh.lingvo.data.repositories.QuizRepository;
import zh.lingvo.data.services.QuizService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;

    public QuizServiceImpl(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Override
    public Optional<Quiz> findById(Long id, User user) {
        return quizRepository.findById(id)
                .filter(quiz -> Objects.equals(quiz.getUser().getId(), user.getId()));
    }

    @Override
    public List<Quiz> findAll(User user) {
        return quizRepository.findAllByUser(user);
    }

    @Override
    public Optional<Quiz> save(Quiz quiz, User user) {
        return isNew(quiz)
            ? saveNew(quiz, user)
            : saveExisting(quiz, user);
    }

    private boolean isNew(Quiz quiz) {
        return quiz.getId() == null;
    }

    private Optional<Quiz> saveNew(Quiz quiz, User user) {
        quiz.setUser(user);
        return Optional.of(quizRepository.save(quiz));
    }

    private Optional<Quiz> saveExisting(Quiz quiz, User user) {
        return quizRepository.existsByIdAndUser(quiz.getId(), user)
            ? Optional.of(quizRepository.save(quiz))
            : Optional.empty();
    }

    @Override
    public boolean deleteById(Long id, User user) {
        try {
            quizRepository
                    .findByIdAndUser(id, user)
                    .ifPresent(quizRepository::delete);
            return true;
        } catch (Throwable t) {
            log.error("Error while deleting quiz [{}]", id, t);
            return false;
        }
    }
}
