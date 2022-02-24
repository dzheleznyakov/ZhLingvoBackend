package zh.lingvo.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.QuizRecord;

import java.util.List;

public interface QuizRecordRepository extends CrudRepository<QuizRecord, Long> {
    List<QuizRecord> findAllByQuiz(Quiz quiz);
}
