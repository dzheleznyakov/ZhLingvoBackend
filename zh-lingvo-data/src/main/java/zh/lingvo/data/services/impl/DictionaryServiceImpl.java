package zh.lingvo.data.services.impl;

import org.springframework.stereotype.Service;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.User;
import zh.lingvo.data.repositories.DictionaryRepository;
import zh.lingvo.data.services.DictionaryService;

import java.util.List;
import java.util.Optional;

@Service
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
}
