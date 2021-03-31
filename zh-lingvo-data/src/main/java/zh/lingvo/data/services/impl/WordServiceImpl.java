package zh.lingvo.data.services.impl;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.LongFunction;

import static com.google.common.base.MoreObjects.firstNonNull;

@Slf4j
@Service
public class WordServiceImpl implements WordService {
    private final WordRepository wordRepository;
    private final DictionaryService dictionaryService;
    private final SubWordService subWordService;

    private final LoadingCache<Long, Long> wordIdsToUserIds = CacheBuilder.newBuilder()
            .maximumSize(100L)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build(new WordToUserIdCacheLoader());

    public WordServiceImpl(WordRepository wordRepository, DictionaryService dictionaryService, SubWordService subWordService) {
        this.wordRepository = wordRepository;
        this.dictionaryService = dictionaryService;
        this.subWordService = subWordService;
    }

    @Override
    public List<Word> findAll(Long dictionaryId, User user) {
        return dictionaryService.findById(dictionaryId, user)
                .map(wordRepository::findAllByDictionary)
                .orElseGet(ImmutableList::of);
    }

    @Override
    public Optional<Word> findById(@Nonnull Long id, @Nonnull User user) {
        return findWord(id, user, wordRepository::findById);
    }

    @Override
    public Optional<Word> findWithSubWordPartsById(@Nonnull Long wordId, @Nonnull User user) {
        return findWord(wordId, user, wordRepository::findByIdWithSubWordParts);
    }

    private Optional<Word> findWord(Long wordId, User user, LongFunction<Optional<Word>> wordGetter) {
        return wordGetter.apply(wordId)
                .filter(word -> userIsAuthorised(word, user));
    }

    private boolean userIsAuthorised(Word word, User user) {
        try {
            Long userIdForWord = wordIdsToUserIds.get(word.getId());
            return Objects.equals(userIdForWord, user.getId());
        } catch (Exception   e) {
            log.error("Error when verifying user [{}] for word [{}]", user, word, e);
        }
        return false;
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
                || !userIsAuthorised(word, user);
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

    @Override
    public void delete(Word word, User user) {
        if (userIsAuthorised(word, user))
            delete(word);
    }

    private void delete(Word word) {
        wordRepository.delete(word);
        wordIdsToUserIds.invalidate(word.getId());
    }

    private class WordToUserIdCacheLoader extends CacheLoader<Long, Long> {
        @Override
        public Long load(@Nonnull Long wordId) {
            return wordRepository.findByIdWithDictionary(wordId)
                    .map(Word::getDictionary)
                    .map(Dictionary::getUser)
                    .map(User::getId)
                    .orElse(-1L);
        }
    }

    @VisibleForTesting
    LoadingCache<Long, Long> getWordIdsToUserIds() {
        return wordIdsToUserIds;
    }
}
