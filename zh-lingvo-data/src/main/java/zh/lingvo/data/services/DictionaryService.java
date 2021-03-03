package zh.lingvo.data.services;

import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.User;

import java.util.List;
import java.util.Optional;

public interface DictionaryService {
    Optional<Dictionary> findById(Long id);

    List<Dictionary> findAllByUser(User user);

    Dictionary save(Dictionary dictionary);

    boolean existsById(Long id);
}
