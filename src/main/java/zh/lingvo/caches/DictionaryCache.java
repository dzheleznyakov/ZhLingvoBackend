package zh.lingvo.caches;

import zh.lingvo.util.ConfigReader;
import zh.lingvo.domain.Dictionary;
import persistence.PersistenceException;
import persistence.xml.XmlReader;

import javax.xml.bind.JAXBException;
import java.io.File;

public class DictionaryCache {
    private static final ConfigReader config = ConfigReader.get();

    private static Dictionary dictionary;

    public static Dictionary get(String languageCode) {
        if (dictionary == null || !dictionary.getLanguage().getCode().equals(languageCode))
            loadDictionary(languageCode);
        return dictionary;
    }

    private static void loadDictionary(String languageCode) {
        String dictionaryFolderName = config.getStringOrDefault("dictionaryFolder", "");
        File dictionaryFolder = new File(new File(dictionaryFolderName).getAbsolutePath());
        String dicFileName = languageCode.toLowerCase() + "_dictionary.xml";
        File[] files = dictionaryFolder.listFiles(((dir, name) -> name.equals(dicFileName)));
        if (dictionaryFileIsFound(files)) {
            File dictionaryFile = files[0];
            try {
                dictionary = new XmlReader().loadDictionary(dictionaryFile);
            } catch (JAXBException e) {
                throw new PersistenceException("Failed to fetch the dictionary for language [" + languageCode + "]", e);
            }
        } else if (LanguagesCache.isRegistered(languageCode)) {
            dictionary = new Dictionary(LanguagesCache.get(languageCode));
        } else {
            throw new PersistenceException("Language [" + languageCode + "] is not supported");
        }
    }

    private static boolean dictionaryFileIsFound(File[] files) {
        return files != null && files.length > 0;
    }
}
