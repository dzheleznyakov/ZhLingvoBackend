package zh.lingvo.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zh.lingvo.data.model.QuizTranslation;

public interface QuizTranslationRepository extends CrudRepository<QuizTranslation, Long> {
}
