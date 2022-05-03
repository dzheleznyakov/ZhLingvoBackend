package zh.lingvo.data.bootstrap;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.Example;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.Meaning;
import zh.lingvo.data.model.SemanticBlock;
import zh.lingvo.data.model.Translation;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.Word;
import zh.lingvo.data.repositories.DictionaryRepository;
import zh.lingvo.data.repositories.ExampleRepository;
import zh.lingvo.data.repositories.LanguageRepository;
import zh.lingvo.data.repositories.MeaningRepository;
import zh.lingvo.data.repositories.SemanticBlockRepository;
import zh.lingvo.data.repositories.TranslationRepository;
import zh.lingvo.data.repositories.UserRepository;
import zh.lingvo.data.repositories.WordRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static zh.lingvo.core.domain.PartOfSpeech.NOUN;
import static zh.lingvo.core.domain.PartOfSpeech.VERB;
import static zh.lingvo.data.constants.Profiles.DEV;

@Component
@Profile(DEV)
@Slf4j
@Order(1)
public class DictionaryBootstrap implements CommandLineRunner {
    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;
    private final DictionaryRepository dictionaryRepository;
    private final WordRepository wordRepository;
    private final SemanticBlockRepository semanticBlockRepository;
    private final MeaningRepository meaningRepository;
    private final TranslationRepository translationRepository;
    private final ExampleRepository exampleRepository;

    public DictionaryBootstrap(
            UserRepository userRepository,
            LanguageRepository languageRepository,
            DictionaryRepository dictionaryRepository,
            WordRepository wordRepository,
            SemanticBlockRepository semanticBlockRepository,
            MeaningRepository meaningRepository,
            TranslationRepository translationRepository,
            ExampleRepository exampleRepository
    ) {
        this.userRepository = userRepository;
        this.languageRepository = languageRepository;
        this.dictionaryRepository = dictionaryRepository;
        this.wordRepository = wordRepository;
        this.semanticBlockRepository = semanticBlockRepository;
        this.meaningRepository = meaningRepository;
        this.translationRepository = translationRepository;
        this.exampleRepository = exampleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        loadTestDictionary();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private void loadTestDictionary() {
        Optional<Dictionary> dictionaryOptional = dictionaryRepository.findById(1L);
        Dictionary dictionary = dictionaryOptional
                .orElseGet(() -> {
                    User admin = userRepository.findByName("admin").get();
                    Language english = languageRepository.findByTwoLetterCode("En").get();
                    Dictionary d = Dictionary.builder()
                            .language(english)
                            .user(admin)
                            .name("Test dictionary")
                            .build();
                    return dictionaryRepository.save(d);
                });
        loadWords(dictionary);
        log.info("Test dictionary loaded");
    }

    private void loadWords(Dictionary dictionary) {
        loadWordWord(dictionary);
        loadWordBox(dictionary);
        log.info("Test dictionary words loaded");
    }

    private void loadWordWord(Dictionary dictionary) {
        String mainForm = "word";
        List<Word> words = wordRepository.findAllByMainForm(mainForm);
        if (words.isEmpty()) {
            Word word = new Word();
            word.setDictionary(dictionary);
            word.setMainForm(mainForm);
            word.setTranscription("wəːd");
            wordRepository.save(word);

            SemanticBlock block = new SemanticBlock();
            block.setWord(word);
            block.setPos(NOUN);
            semanticBlockRepository.save(block);

            Meaning meaning = new Meaning();
            meaning.setSemBlock(block);
            meaningRepository.save(meaning);

            Translation translation = getTranslation(meaning, "слово");
            translationRepository.save(translation);
        }
    }

    private void loadWordBox(Dictionary dictionary) {
        String mainForm = "box";
        String transcription = "bɒks";
        List<Word> words = wordRepository.findAllByMainForm(mainForm);
        if (words.isEmpty()) {
            Word word1 = new Word();
            word1.setDictionary(dictionary);
            word1.setMainForm(mainForm);
            word1.setTranscription(transcription);
            wordRepository.save(word1);

            SemanticBlock blockNoun = new SemanticBlock();
            blockNoun.setWord(word1);
            blockNoun.setPos(NOUN);
            semanticBlockRepository.save(blockNoun);

            Meaning meaning1 = new Meaning();
            meaning1.setSemBlock(blockNoun);
            meaningRepository.save(meaning1);
            Translation translation1_1 = getTranslation(meaning1, "коробка");
            Translation translation1_2 = getTranslation(meaning1, "ящик");
            Translation translation1_3 = getTranslation(meaning1, "сундук");
            Translation translation1_4 = getTranslation(meaning1, "сумка, вместилище");
            translationRepository.saveAll(ImmutableList.of(
                    translation1_1, translation1_2, translation1_3, translation1_4));

            Meaning meaning2 = new Meaning();
            meaning2.setSemBlock(blockNoun);
            meaningRepository.save(meaning2);
            Translation translation2_1 = getTranslation(meaning2, "ящичек", "стола");
            Translation translation2_2 = getTranslation(meaning2, "коробочка", "для всяких мелочей");
            Translation translation2_3 = getTranslation(meaning2, "шкатулка");
            translationRepository.saveAll(ImmutableList.of(
                    translation2_1, translation2_2, translation2_3));
            Example example2_1 = getExample(meaning2, "witness box", "место в суде, где сидят свидетели");
            Example example2_2 = getExample(meaning2, "music box", "музыкальная шкатулка", "амер.");
            exampleRepository.saveAll(ImmutableList.of(
                    example2_1, example2_2));

            Meaning meaning3 = new Meaning();
            meaning3.setSemBlock(blockNoun);
            meaning3.setRemark("рел.");
            meaningRepository.save(meaning3);
            Translation translation3_1 = getTranslation(meaning3, "дарохранительница");
            Translation translation3_2 = getTranslation(meaning3, "дароносица");
            translationRepository.saveAll(ImmutableList.of(
                    translation3_1, translation3_2));

            Meaning meaning4 = new Meaning();
            meaning4.setSemBlock(blockNoun);
            meaningRepository.save(meaning4);
            Translation translation4_1 = getTranslation(meaning4, "сейф");
            translationRepository.save(translation4_1);

            SemanticBlock blockVerb = new SemanticBlock();
            blockVerb.setWord(word1);
            blockVerb.setPos(VERB);
            semanticBlockRepository.save(blockVerb);

            Meaning meaning5 = new Meaning();
            meaning5.setSemBlock(blockVerb);
            meaningRepository.save(meaning5);
            Translation translation5_1 = getTranslation(meaning5, "класть в ящик или коробку");
            Translation translation5_2 = getTranslation(meaning5, "упаковывать");
            Translation translation5_3 = getTranslation(meaning5, "запирать в сундук");
            translationRepository.saveAll(ImmutableList.of(
                    translation5_1, translation5_2, translation5_3));

            Meaning meaning6 = new Meaning();
            meaning6.setSemBlock(blockVerb);
            meaningRepository.save(meaning6);
            Translation translation6_1 = getTranslation(meaning6, "обрамлять, печатать в рамке");
            translationRepository.save(translation6_1);

            Word word2 = new Word();
            word2.setDictionary(dictionary);
            word2.setMainForm(mainForm);
            word2.setTranscription(transcription);
            wordRepository.save(word2);

            SemanticBlock blockNoun2 = new SemanticBlock();
            blockNoun2.setPos(NOUN);
            blockNoun2.setWord(word2);
            semanticBlockRepository.save(blockNoun2);

            Meaning meaning7 = new Meaning();
            meaning7.setSemBlock(blockNoun2);
            meaningRepository.save(meaning7);
            translationRepository.save(getTranslation(meaning7, "пощёчина"));
        }
    }

    private Translation getTranslation(Meaning meaning, String value) {
        return getTranslation(meaning, value, null);
    }

    private Translation getTranslation(Meaning meaning, String value, String elaboration) {
        Translation translation = new Translation();
        translation.setMeaning(meaning);
        translation.setValue(value);
        translation.setElaboration(elaboration);
        return translation;
    }

    private Example getExample(Meaning meaning, String expression, String explanation) {
        return getExample(meaning, expression, explanation, null);
    }

    private Example getExample(Meaning meaning, String expression, String explanation, String remark) {
        Example example = new Example();
        example.setMeaning(meaning);
        example.setRemark(remark);
        example.setExpression(expression);
        example.setExplanation(explanation);
        return example;
    }
}
