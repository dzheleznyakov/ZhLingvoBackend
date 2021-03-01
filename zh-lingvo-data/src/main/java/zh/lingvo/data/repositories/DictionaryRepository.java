package zh.lingvo.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.User;

import java.util.List;

public interface DictionaryRepository extends CrudRepository<Dictionary, Long> {
    List<Dictionary> findByUser(User user);
}
