package zh.lingvo.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zh.lingvo.data.domain.Meaning;

public interface MeaningRepository extends CrudRepository<Meaning, Long> {
}
