package zh.lingvo.data.services;

import zh.lingvo.data.model.QuizRecord;
import zh.lingvo.data.model.User;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface QuizRecordService {
    List<QuizRecord> findAll(Long quizId, User user);

    Optional<QuizRecord> findById(Long quizRecordId, User user);

    Optional<QuizRecord> create(QuizRecord record, Long quizId, User user);

    Optional<QuizRecord> update(@Nonnull QuizRecord record, Long quizId, User user);

    boolean deleteById(Long quizRecordId, Long quizId, User user);
}
