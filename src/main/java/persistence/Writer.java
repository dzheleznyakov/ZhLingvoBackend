package persistence;

import zh.lingvo.domain.Dictionary;

public interface Writer {
    void saveDictionary(Dictionary dictionary, String fileName) throws Exception;
}
