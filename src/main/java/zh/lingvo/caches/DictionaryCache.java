package zh.lingvo.caches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import zh.lingvo.util.ConfigReader;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.persistence.PersistenceException;
import zh.lingvo.persistence.xml.XmlReader;

import javax.xml.bind.JAXBException;
import java.io.File;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@Service
@Scope(SCOPE_SINGLETON)
public class DictionaryCache {
    private static final ConfigReader config = ConfigReader.get();

    @Autowired
    private LanguagesCache languagesCache;

    @Autowired
    private XmlReader xmlReader;

    private Dictionary dictionary;

    public Dictionary get(String languageCode) {
        if (!dictionaryIsLoaded() || !dictionaryLanguageIsCorrect(languageCode))
            loadDictionary(languageCode);
        return dictionary;
    }

    private boolean dictionaryLanguageIsCorrect(String languageCode) {
        return dictionary.getLanguage().getCode().equals(languageCode);
    }

    private boolean dictionaryIsLoaded() {
        return dictionary != null;
    }

    private void loadDictionary(String languageCode) {
        String dictionaryFolderName = config.getStringOrDefault("dictionaryFolder", "");
        File dictionaryFolder = new File(new File(dictionaryFolderName).getAbsolutePath());
        String dicFileName = languageCode.toLowerCase() + "_dictionary.xml";
        File[] files = dictionaryFolder.listFiles(((dir, name) -> name.equals(dicFileName)));
        if (dictionaryFileIsFound(files)) {
            File dictionaryFile = files[0];
            try {
                dictionary = xmlReader.loadDictionary(dictionaryFile);
            } catch (JAXBException e) {
                throw new PersistenceException("Failed to fetch the dictionary for language [" + languageCode + "]", e);
            }
        } else if (languagesCache.isRegistered(languageCode)) {
            dictionary = new Dictionary(languagesCache.get(languageCode));
        } else {
            throw new PersistenceException("Language [" + languageCode + "] is not supported");
        }
    }

    private static boolean dictionaryFileIsFound(File[] files) {
        return files != null && files.length > 0;
    }
}
