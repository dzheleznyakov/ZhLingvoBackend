package zh.lingvo.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.User;

import java.util.List;
import java.util.Optional;

public interface DictionaryRepository extends CrudRepository<Dictionary, Long> {
    List<Dictionary> findAllByUser(User user);

    boolean existsByIdAndUser(Long id, User user);

    Optional<Dictionary> findByIdAndUser(Long id, User user);
}
