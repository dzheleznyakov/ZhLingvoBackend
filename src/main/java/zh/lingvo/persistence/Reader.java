package zh.lingvo.persistence;

import zh.lingvo.domain.Dictionary;

import java.io.File;

public interface Reader {
    Dictionary loadDictionary(String fileName);
    Dictionary loadDictionary(File dictionaryFile);
}
