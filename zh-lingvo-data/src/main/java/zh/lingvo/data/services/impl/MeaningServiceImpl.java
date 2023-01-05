package zh.lingvo.data.services.impl;

import org.springframework.stereotype.Service;
import zh.lingvo.data.model.Meaning;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.Word;
import zh.lingvo.data.repositories.MeaningRepository;
import zh.lingvo.data.services.MeaningService;
import zh.lingvo.data.services.WordService;

import java.util.Optional;

@Service
public class MeaningServiceImpl implements MeaningService {
    private final MeaningRepository meaningRepository;
    private final WordService wordService;

    public MeaningServiceImpl(MeaningRepository meaningRepository, WordService wordService) {
        this.meaningRepository = meaningRepository;
        this.wordService = wordService;
    }

    @Override
    public Optional<Meaning> findById(Long id, User user) {
        Optional<Meaning> optionalMeaning = meaningRepository.findById(id);
        if (optionalMeaning.isEmpty())
            return Optional.empty();

        Meaning meaning = optionalMeaning.get();
        Word word = meaning.getSemBlock().getWord();
        return wordService.userIsAuthorised(word, user)
                ? Optional.of(meaning)
                : Optional.empty();
    }
}
