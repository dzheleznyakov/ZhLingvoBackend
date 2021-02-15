package zh.lingvo.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zh.lingvo.data.model.Translation;

public interface TranslationRepository extends CrudRepository<Translation, Long> {
}
