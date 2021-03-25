package zh.lingvo.data.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.User;
import zh.lingvo.data.repositories.DictionaryRepository;
import zh.lingvo.data.services.DictionaryService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DictionaryServiceImpl implements DictionaryService {
    private final DictionaryRepository dictionaryRepository;

    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    @Override
    public Optional<Dictionary> findById(Long id, User user) {
        return dictionaryRepository.findByIdAndUser(id, user);
    }

    @Override
    public List<Dictionary> findAll(User user) {
        return dictionaryRepository.findAllByUser(user);
    }

    @Override
    public Optional<Dictionary> save(Dictionary dictionary, User user) {
        return isNew(dictionary) ? saveNew(dictionary, user) : saveExisting(dictionary, user);
    }

    private boolean isNew(Dictionary dictionary) {
        return dictionary.getId() == null;
    }

    private Optional<Dictionary> saveNew(Dictionary dictionary, User user) {
        dictionary.setUser(user);
        return Optional.of(dictionaryRepository.save(dictionary));
    }

    private Optional<Dictionary> saveExisting(Dictionary dictionary, User user) {
        return dictionaryRepository.existsByIdAndUser(dictionary.getId(), user)
                ? Optional.of(dictionaryRepository.save(dictionary))
                : Optional.empty();
    }

    @Override
    public boolean deleteById(Long id, User user) {
        try {
            dictionaryRepository
                    .findByIdAndUser(id, user)
                    .ifPresent(dictionaryRepository::delete);
            return true;
        } catch (Throwable t) {
            log.error("Error while deleting dictionary [{}]", id, t);
            return false;
        }
    }
}
