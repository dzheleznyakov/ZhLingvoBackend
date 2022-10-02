package zh.lingvo.data.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.QuizRecord;

import java.util.List;
import java.util.Optional;

public interface QuizRecordRepository extends CrudRepository<QuizRecord, Long> {
    List<QuizRecord> findAllByQuiz(Quiz quiz);

    @Query("SELECT qr FROM quiz_record qr JOIN FETCH qr.quiz WHERE qr.id = (:id)")
    Optional<QuizRecord> findByIdWithQuiz(@Param("id") Long id);
}
