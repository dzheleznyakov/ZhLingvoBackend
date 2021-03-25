package zh.lingvo.data.services;

import zh.lingvo.data.model.User;
import zh.lingvo.data.model.Word;

import java.util.Optional;

public interface WordService {
    Optional<Word> findById(Long wordId, User user);

    Optional<Word> create(Word word, Long dictionary, User user);

    Word update(Word word, User user);
}
