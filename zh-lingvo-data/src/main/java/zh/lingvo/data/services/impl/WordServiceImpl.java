package zh.lingvo.data.services.impl;

import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Service;
import zh.lingvo.data.exceptions.FailedToPersist;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.SemanticBlock;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.Word;
import zh.lingvo.data.repositories.WordRepository;
import zh.lingvo.data.services.DictionaryService;
import zh.lingvo.data.services.SubWordService;
import zh.lingvo.data.services.WordService;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.MoreObjects.firstNonNull;

@Service
public class WordServiceImpl implements WordService {
    private final WordRepository wordRepository;
    private final DictionaryService dictionaryService;
    private final SubWordService subWordService;

    public WordServiceImpl(WordRepository wordRepository, DictionaryService dictionaryService, SubWordService subWordService) {
        this.wordRepository = wordRepository;
        this.dictionaryService = dictionaryService;
        this.subWordService = subWordService;
    }

    @Override
    public Optional<Word> findById(@Nonnull Long id, @Nonnull User user) {
        return wordRepository.findById(id)
                .filter(word -> Objects.equals(user, word.getDictionary().getUser()));
    }

    @Override
    public Optional<Word> create(Word word, Long dictionaryId, User user) {
        return word.getId() == null
                ? dictionaryService.findById(dictionaryId, user)
                        .flatMap(dictionary -> save(word, dictionary))
                : Optional.empty();
    }

    @Override
    public Word update(Word word, User user) {
        if (shouldNotUpdate(word, user))
            throw new FailedToPersist("Word [%s] does not exist", word);
        return save(word, word.getDictionary())
                .orElse(word);
    }

    private boolean shouldNotUpdate(Word word, User user) {
        return word.getId() == null
                || word.getDictionary() == null
                || !Objects.equals(word.getDictionary().getUser(), user);
    }

    private Optional<Word> save(Word word, Dictionary dictionary) {
        word.setDictionary(dictionary);
        Word savedWord = wordRepository.save(word);
        firstNonNull(word.getSemanticBlocks(), ImmutableList.<SemanticBlock>of())
                .stream()
                .peek(sb -> sb.setWord(word))
                .forEach(subWordService::save);
        return Optional.of(savedWord);
    }
}
