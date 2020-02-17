package zh.lingvo.persistence;

import zh.lingvo.domain.Dictionary;

public interface Writer {
    void saveDictionary(Dictionary dictionary, String fileName) throws PersistenceException;
}
