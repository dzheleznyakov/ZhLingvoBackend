package zh.lingvo.data.integrationtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import zh.lingvo.data.fixtures.WordHelper;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.Example;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.Meaning;
import zh.lingvo.data.model.PartOfSpeech;
import zh.lingvo.data.model.SemanticBlock;
import zh.lingvo.data.model.Translation;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.Word;

import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static zh.hamcrest.ZhMatchers.empty;
import static zh.hamcrest.ZhMatchers.hasPropertySatisfying;

@TestPropertySource(locations = "classpath:/test.properties")
@Sql("/data-integration-tests.sql")
@SpringBootTest
public class WordServiceIT extends BaseDataIntegrationTest {
    private User user;
    private Dictionary dictionary;
    private PartOfSpeech pos;
    private Word word;
    private SemanticBlock sBlock;
    private Meaning meaning;
    private Translation translation1;
    private Translation translation2;
    private Example example;

    @BeforeEach
    void setUp() {
        user = userRepository.findById(1L).get();
        Language language = languageRepository.findById(1).get();
        pos = posRepository.findById(1).get();
        dictionary = getPersistedDictionary("WordServiceIT test", user, language);

        word = Word.builder()
                .mainForm("word")
                .dictionary(dictionary)
                .build();
        sBlock = SemanticBlock.builder()
                .pos(pos)
                .build();
        meaning = Meaning.builder()
                .remark("mRemark")
                .build();
        translation1 = Translation.builder()
                .value("translation1")
                .elaboration("elaboration1")
                .build();
        translation2 = Translation.builder()
                .value("translation2")
                .elaboration("elaboration2")
                .build();
        example = Example.builder()
                .remark("eRemark")
                .expression("expression1")
                .explanation("explanation1")
                .build();
    }

    @Test
    @DisplayName("Should save a new word along with all sub-word parts")
    void saveNewWord() {
        WordHelper.add(meaning, translation1, translation2);
        WordHelper.add(meaning, example);
        WordHelper.add(sBlock, meaning);
        WordHelper.add(word, sBlock);

        Optional<Word> optionalWord = wordService.create(word, dictionary.getId(), user);
        Word persistedWord = optionalWord.orElse(null);

        assertThat(persistedWord, is(equalTo(word)));
        assertThat(word.getId(), is(notNullValue()));

        assertThat(sBlock.getId(), is(notNullValue()));
        assertThat(sBlock.getWord(), is(equalTo(word)));

        assertThat(meaning.getId(), is(notNullValue()));
        assertThat(meaning.getSemBlock(), is(equalTo(sBlock)));

        assertThat(translation1.getId(), is(notNullValue()));
        assertThat(translation1.getMeaning(), is(equalTo(meaning)));

        assertThat(translation2.getId(), is(notNullValue()));
        assertThat(translation2.getMeaning(), is(equalTo(meaning)));

        assertThat(example.getId(), is(notNullValue()));
        assertThat(example.getMeaning(), is(equalTo(meaning)));
    }

    @Test
    @DisplayName("Should add a new sub-word part to the word")
    void addSubWordPart() {
        WordHelper.add(meaning, translation1);
        WordHelper.add(meaning, example);
        WordHelper.add(sBlock, meaning);
        WordHelper.add(word, sBlock);
        wordService.create(word, dictionary.getId(), user);

        meaning.getTranslations().add(translation2);

        wordService.update(word, user);

        assertThat(translation2.getId(), is(notNullValue()));
        assertThat(translation2.getMeaning(), is(equalTo(meaning)));
    }

    @Test
    @DisplayName("Should get the word with sub-word parts")
    void getWordWithSubWordParts() {
        WordHelper.add(word, sBlock);
        wordService.create(word, dictionary.getId(), user);

        Optional<Word> foundWord = wordService.findWithSubWordPartsById(word.getId(), user);

        assertThat(foundWord, is(not(empty())));
        assertThat(foundWord, hasPropertySatisfying(Word::getSemanticBlocks, Objects::nonNull));
        assertThat(foundWord, hasPropertySatisfying(Word::getSemanticBlocks, hasSize(1)));
    }

    @Test
    @DisplayName("Should delete the word with sub-word-parts")
    void deleteWord() {
        WordHelper.add(meaning, translation1);
        WordHelper.add(meaning, example);
        WordHelper.add(sBlock, meaning);
        WordHelper.add(word, sBlock);
        wordService.create(word, dictionary.getId(), user);

        wordService.delete(word, user);

        Word foundWord = findEntity(Word.class, word.getId());
        SemanticBlock foundSemB = findEntity(SemanticBlock.class, sBlock.getId());
        Meaning foundMeaning = findEntity(Meaning.class, meaning.getId());
        Translation foundTranslation = findEntity(Translation.class, translation1.getId());
        Example foundExample = findEntity(Example.class, example.getId());

        assertThat(foundWord, is(nullValue()));
        assertThat(foundSemB, is(nullValue()));
        assertThat(foundMeaning, is(nullValue()));
        assertThat(foundTranslation, is(nullValue()));
        assertThat(foundExample, is(nullValue()));
    }
}
