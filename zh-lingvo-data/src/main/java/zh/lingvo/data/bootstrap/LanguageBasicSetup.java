package zh.lingvo.data.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import zh.lingvo.data.domain.Dictionary;
import zh.lingvo.data.domain.Language;
import zh.lingvo.data.domain.PartOfSpeech;
import zh.lingvo.data.domain.User;
import zh.lingvo.data.domain.Word;
import zh.lingvo.data.repositories.DictionaryRepository;
import zh.lingvo.data.repositories.LanguageRepository;
import zh.lingvo.data.repositories.PartOfSpeechRepository;
import zh.lingvo.data.repositories.UserRepository;
import zh.lingvo.data.repositories.WordRepository;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class LanguageBasicSetup implements CommandLineRunner {
    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;
    private final PartOfSpeechRepository posRepository;
    private final DictionaryRepository dictionaryRepository;
    private final WordRepository wordRepository;

    public LanguageBasicSetup(
            UserRepository userRepository,
            LanguageRepository languageRepository,
            PartOfSpeechRepository posRepository,
            DictionaryRepository dictionaryRepository,
            WordRepository wordRepository
    ) {
        this.userRepository = userRepository;
        this.languageRepository = languageRepository;
        this.posRepository = posRepository;
        this.dictionaryRepository = dictionaryRepository;
        this.wordRepository = wordRepository;
    }

    @Override
    public void run(String... args) {
        loadAdminUser();
        loadLanguages();
        loadPartsOfSpeech();
        loadTestDictionary();
    }

    private void loadAdminUser() {
        Optional<User> adminOptional = userRepository.findByName("admin");
        if (adminOptional.isEmpty()) {
            User user = new User();
            user.setName("admin");
            userRepository.save(user);
        }
        log.info("Admin user loaded");
    }


    private void loadLanguages() {
        loadLanguage("English", "En");
        loadLanguage("Spanish", "Es");
        loadLanguage("Russian", "Ru");
        log.info("Languages loaded");
    }

    private void loadLanguage(String name, String code) {
        Optional<Language> languageOptional = languageRepository.findByTwoLetterCode(code);
        if (languageOptional.isEmpty()) {
            Language language = new Language();
            language.setName(name);
            language.setTwoLetterCode(code);
            log.info("Loading [{}] language", name);
            languageRepository.save(language);
        }
    }

    private void loadPartsOfSpeech() {
        loadPartOfSpeech("NOUN");
        loadPartOfSpeech("VERB");
        loadPartOfSpeech("ADJECTIVE");
        log.info("Parts of speech loaded");
    }

    private void loadPartOfSpeech(String name) {
        Optional<PartOfSpeech> posOptional = posRepository.findByName(name);
        if (posOptional.isEmpty()) {
            PartOfSpeech pos = new PartOfSpeech();
            pos.setName(name);
            log.info("Loading [{}] part of speech", name);
            posRepository.save(pos);
        }
    }

    private void loadTestDictionary() {
        Optional<Dictionary> dictionaryOptional = dictionaryRepository.findById(1L);
        Dictionary dictionary;
        if (dictionaryOptional.isEmpty()) {
            dictionary = new Dictionary();
            User admin = userRepository.findByName("admin").get();
            Language english = languageRepository.findByTwoLetterCode("En").get();
            dictionary.setLanguage(english);
            dictionary.setUser(admin);
            dictionary.setName("Test dictionary");
            dictionary = dictionaryRepository.save(dictionary);
        } else
            dictionary = dictionaryOptional.get();
        loadWords(dictionary);
        log.info("Test dictionary loaded");
    }

    private void loadWords(Dictionary dictionary) {
        loadWord("word", "wəːd", dictionary);
        loadWord("box", "bɒks", dictionary);
        log.info("Test dictionary words loaded");
    }

    private void loadWord(String mainForm, String transcription, Dictionary dictionary) {
        List<Word> words = wordRepository.findAllByMainForm(mainForm);
        if (words.isEmpty()) {
            Word word = new Word();
            word.setDictionary(dictionary);
            word.setMainForm(mainForm);
            word.setTranscription(transcription);
            wordRepository.save(word);
            log.info("Word [{}] loaded", mainForm);
        }
    }
}
