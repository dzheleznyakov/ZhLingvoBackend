package zh.lingvo.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zh.lingvo.data.model.Dictionary;

public interface DictionaryRepository extends CrudRepository<Dictionary, Long> {
}
