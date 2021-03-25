package zh.lingvo.data.integrationtests;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import zh.lingvo.data.fixtures.Persistable;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.Word;
import zh.lingvo.data.repositories.DictionaryRepository;
import zh.lingvo.data.repositories.LanguageRepository;
import zh.lingvo.data.repositories.PartOfSpeechRepository;
import zh.lingvo.data.repositories.UserRepository;
import zh.lingvo.data.repositories.WordRepository;
import zh.lingvo.data.services.DictionaryService;
import zh.lingvo.data.services.UserService;
import zh.lingvo.data.services.WordService;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static com.google.common.base.MoreObjects.firstNonNull;

@TestPropertySource(locations = "classpath:/test.properties")
@Sql("/data-integration-tests.sql")
@SpringBootTest
public abstract class BaseDataIntegrationTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    protected PartOfSpeechRepository posRepository;
    @Autowired
    protected LanguageRepository languageRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected DictionaryRepository dictionaryRepository;
    @Autowired
    protected WordRepository wordRepository;

    @Autowired
    protected UserService userService;
    @Autowired
    protected DictionaryService dictionaryService;
    @Autowired
    protected WordService wordService;

    protected <E extends Persistable> E findEntity(Class<E> entityClass, Object id) {
        return entityManager.find(entityClass, id);
    }

    protected Dictionary getPersistedDictionary(String name, User user, Language language) {
        Dictionary dictionary = Dictionary.builder()
                .name(name)
                .user(user)
                .language(language)
                .build();
        return dictionaryRepository.save(dictionary);
    }

    protected Word getPersistedWord(String mainForm, Dictionary dictionary) {
        Word word = Word.builder()
                .mainForm(mainForm)
                .dictionary(dictionary)
                .build();
        return wordRepository.save(word);
    }

    protected <E> List<E> getListSafely(Supplier<List<E>> supplier) {
        return firstNonNull(supplier.get(), ImmutableList.of());
    }

    protected <E> Set<E> getSetSafely(Supplier<Set<E>> supplier) {
        return firstNonNull(supplier.get(), ImmutableSet.of());
    }
}
