package zh.lingvo.caches;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.persistence.PersistenceException;
import zh.lingvo.persistence.Reader;
import zh.lingvo.persistence.xml.PersistenceManager;

import java.io.File;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@Service
@Scope(SCOPE_SINGLETON)
public class DictionaryCache {
    @Value("${app.dictionaries.location}")
    private String dictionaryFolderName;

    private LanguagesCache languagesCache;

    private Reader reader;

    private Dictionary dictionary;

    public DictionaryCache(LanguagesCache languagesCache, PersistenceManager xmlReader) {
        this.languagesCache = languagesCache;
        this.reader = xmlReader;
    }

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
        File dictionaryFolder = new File(new File(dictionaryFolderName).getAbsolutePath());
        String dicFileName = languageCode.toLowerCase() + "_dictionary.xml";
        File[] files = dictionaryFolder.listFiles(((dir, name) -> name.equals(dicFileName)));
        if (dictionaryFileIsFound(files)) {
            File dictionaryFile = files[0];
            dictionary = reader.loadDictionary(dictionaryFile);
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
