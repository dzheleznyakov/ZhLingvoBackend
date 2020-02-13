package zh.lingvo.persistence;

import zh.lingvo.domain.Dictionary;

public interface Writer extends AutoCloseable {
    void saveDictionary(Dictionary dictionary, String fileName) throws PersistenceException;
}
