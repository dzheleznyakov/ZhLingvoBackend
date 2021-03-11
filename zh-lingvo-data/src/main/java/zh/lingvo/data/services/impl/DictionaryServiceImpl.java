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
    public Optional<Dictionary> findById(Long id) {
        return dictionaryRepository.findById(id);
    }

    @Override
    public List<Dictionary> findAllByUser(User user) {
        return dictionaryRepository.findAllByUser(user);
    }

    @Override
    public Dictionary save(Dictionary dictionary) {
        return dictionaryRepository.save(dictionary);
    }

    @Override
    public boolean existsById(Long id) {
        return id != null && dictionaryRepository.existsById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            dictionaryRepository.deleteById(id);
            return true;
        } catch (Throwable t) {
            log.error("Error while deleting dictionary [{}]", id, t);
            return false;
        }
    }
}
