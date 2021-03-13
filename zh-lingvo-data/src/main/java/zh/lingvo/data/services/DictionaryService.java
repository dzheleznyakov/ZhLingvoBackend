package zh.lingvo.data.services;

import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.User;

import java.util.List;
import java.util.Optional;

public interface DictionaryService {
    Optional<Dictionary> findById(Long id, User user);

    List<Dictionary> findAll(User user);

    Optional<Dictionary> save(Dictionary dictionary, User user);

    boolean existsById(Long id);

    boolean deleteById(Long id);
}
