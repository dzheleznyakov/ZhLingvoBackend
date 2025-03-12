package zh.lingvo.data.services;

import zh.lingvo.data.fixtures.PageableList;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.Word;

import java.util.List;
import java.util.Optional;

public interface WordService {
    List<Word> findAll(Long dictionaryId, User user);
    PageableList<Word> findAll(Long dictionaryId, User user, int offset, int limit);

    List<Word> findAllByMainForm(String mainForm, Long dictionaryId, User user);

    Optional<Word> findById(Long wordId, User user);

    Optional<Word> findWithSubWordPartsById(Long wordId, User user);

    Optional<Word> create(Word word, Long dictionaryId, User user);

    Word update(Word word, Long dictionaryId, User user);

    void delete(Word word, User user);

    boolean userIsAuthorised(Word word, User user);
}
