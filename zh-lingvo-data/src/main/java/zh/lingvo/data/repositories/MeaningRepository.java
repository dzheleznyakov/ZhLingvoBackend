package zh.lingvo.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zh.lingvo.data.model.Meaning;

public interface MeaningRepository extends CrudRepository<Meaning, Long> {
}
