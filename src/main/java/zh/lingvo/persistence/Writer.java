package zh.lingvo.persistence;

import zh.lingvo.domain.Dictionary;

import java.io.Closeable;

public interface Writer extends Closeable {
    void saveDictionary(Dictionary dictionary, String fileName) throws PersistenceException;
}
