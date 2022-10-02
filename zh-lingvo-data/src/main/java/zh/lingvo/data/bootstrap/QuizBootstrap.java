package zh.lingvo.data.bootstrap;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import zh.lingvo.core.domain.PartOfSpeech;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.QuizExample;
import zh.lingvo.data.model.QuizRecord;
import zh.lingvo.data.model.QuizTranslation;
import zh.lingvo.data.model.User;
import zh.lingvo.data.repositories.LanguageRepository;
import zh.lingvo.data.repositories.QuizExampleRepository;
import zh.lingvo.data.repositories.QuizRecordRepository;
import zh.lingvo.data.repositories.QuizRepository;
import zh.lingvo.data.repositories.QuizTranslationRepository;
import zh.lingvo.data.repositories.UserRepository;

import java.util.List;

import static zh.lingvo.data.constants.Profiles.DEV;

@Component
@Profile(DEV)
@Slf4j
@Order(2)
public class QuizBootstrap implements CommandLineRunner {
    private final QuizRepository quizRepository;
    private final QuizRecordRepository quizRecordRepository;
    private final QuizTranslationRepository quizTranslationRepository;
    private final QuizExampleRepository quizExampleRepository;
    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;

    public QuizBootstrap(
            QuizRepository quizRepository,
            QuizRecordRepository quizRecordRepository,
            QuizTranslationRepository quizTranslationRepository,
            QuizExampleRepository quizExampleRepository,
            UserRepository userRepository,
            LanguageRepository languageRepository
    ) {
        this.quizRepository = quizRepository;
        this.quizRecordRepository = quizRecordRepository;
        this.quizTranslationRepository = quizTranslationRepository;
        this.quizExampleRepository = quizExampleRepository;
        this.userRepository = userRepository;
        this.languageRepository = languageRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadTestQuiz();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private void loadTestQuiz() {
        Quiz quiz = quizRepository
                .findById(1L)
                .orElseGet(() -> {
                    User admin = userRepository.findByName("admin").get();
                    Language english = languageRepository.findByTwoLetterCode("En").get();
                    Quiz q = new Quiz();
                    q.setLanguage(english);
                    q.setUser(admin);
                    q.setName("Test quiz");
                    return quizRepository.save(q);
                });
        loadRecords(quiz);
        log.info("Test quiz loaded");
    }

    private void loadRecords(Quiz quiz) {
        List<QuizRecord> records = quizRecordRepository.findAllByQuiz(quiz);
        if (records.isEmpty()) {
            loadRecordWord(quiz);
            loadRecordBox(quiz);
        }
        log.info("Test quiz records loaded");
    }

    private void loadRecordWord(Quiz quiz) {
        List<QuizRecord> records = quizRecordRepository.findAllByQuiz(quiz);
        QuizRecord word = QuizRecord.builder()
                .quiz(quiz)
                .wordMainForm("word")
                .pos(PartOfSpeech.NOUN)
                .transcription("wəːd")
                .currentScore(0f)
                .numberOfRuns(0)
                .numberOfSuccesses(0)
                .build();
        word = quizRecordRepository.save(word);

        QuizTranslation translation = getQuizTranslation(word, "слово");
        quizTranslationRepository.save(translation);
    }

    private void loadRecordBox(Quiz quiz) {
        List<QuizRecord> records = quizRecordRepository.findAllByQuiz(quiz);
        QuizRecord box = QuizRecord.builder()
                .quiz(quiz)
                .wordMainForm("box")
                .pos(PartOfSpeech.NOUN)
                .transcription("bɒks")
                .currentScore(0f)
                .numberOfRuns(0)
                .numberOfSuccesses(0)
                .build();
        box = quizRecordRepository.save(box);

        QuizTranslation translation1 = getQuizTranslation(box, "ящичек", "стола");
        QuizTranslation translation2 = getQuizTranslation(box, "коробочка", "для всяких мелочей");
        QuizTranslation translation3 = getQuizTranslation(box, "шкатулка");
        quizTranslationRepository.saveAll(ImmutableList.of(
                translation1, translation2, translation3
        ));

        QuizExample example1 = getQuizExample(box, "witness box", "место в суде, где сидят свидетели");
        QuizExample example2 = getQuizExample(box, "musib box", "музыкальная шкатулка", "амер.");
        quizExampleRepository.saveAll(ImmutableList.of(
                example1, example2
        ));
    }

    private QuizTranslation getQuizTranslation(QuizRecord record, String value) {
        return getQuizTranslation(record, value, null);
    }

    private QuizTranslation getQuizTranslation(QuizRecord record, String value, String elaboration) {
        return QuizTranslation.builder()
                .record(record)
                .value(value)
                .elaboration(elaboration)
                .build();
    }

    private QuizExample getQuizExample(QuizRecord record, String expression, String explanation) {
        return getQuizExample(record, expression, explanation, null);
    }

    private QuizExample getQuizExample(QuizRecord record, String expression, String explanation, String remark) {
        return QuizExample.builder()
                .record(record)
                .expression(expression)
                .explanation(explanation)
                .remark(remark)
                .build();
    }
}
