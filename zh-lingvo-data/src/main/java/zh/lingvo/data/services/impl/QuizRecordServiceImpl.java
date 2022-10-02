package zh.lingvo.data.services.impl;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.QuizRecord;
import zh.lingvo.data.model.User;
import zh.lingvo.data.repositories.QuizRecordRepository;
import zh.lingvo.data.services.QuizRecordService;
import zh.lingvo.data.services.QuizService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class QuizRecordServiceImpl implements QuizRecordService {
    private final QuizService quizService;
    private final QuizRecordRepository quizRecordRepository;

    public QuizRecordServiceImpl(
            QuizService quizService,
            QuizRecordRepository quizRecordRepository
    ) {
        this.quizService = quizService;
        this.quizRecordRepository = quizRecordRepository;
    }

    @Override
    public List<QuizRecord> findAll(Long quizId, User user) {
        return quizService.findById(quizId, user)
                .map(quizRecordRepository::findAllByQuiz)
                .orElse(ImmutableList.of());
    }

    @Override
    public Optional<QuizRecord> findById(Long quizRecordId, User user) {
        return quizRecordRepository.findByIdWithQuiz(quizRecordId)
                .filter(qr -> Objects.equals(qr.getQuiz().getUser().getId(), user.getId()));
    }

    @Override
    public Optional<QuizRecord> create(QuizRecord record, Long quizId, User user) {
        return record.getId() == null ?
                quizService.findById(quizId, user)
                        .flatMap(quiz -> save(record, quiz, user))
                : Optional.empty();
    }

    private Optional<QuizRecord> save(QuizRecord record, Quiz quiz, User user) {
        record.setQuiz(quiz);
        quizService.save(quiz, user);
        return Optional.of(quizRecordRepository.save(record));
    }

    @Override
    @Nullable
    public Optional<QuizRecord> update(@Nonnull QuizRecord record, Long quizId, User user) {
        if (record.getQuiz() == null || !Objects.equals(record.getQuiz().getId(), quizId))
            return Optional.empty();
        return quizService.existsById(quizId, user)
                ? Optional.of(quizRecordRepository.save(record))
                : Optional.empty();
    }

    @Override
    public boolean deleteById(Long quizRecordId, Long quizId, User user) {
        if (quizRecordId == null)
            return false;
        try {
            quizRecordRepository.findByIdWithQuiz(quizRecordId)
                    .filter(qr -> Objects.equals(qr.getQuiz().getId(), quizId))
                    .filter(qr -> Objects.equals(qr.getQuiz().getUser(), user))
                    .ifPresent(quizRecordRepository::delete);
            return true;
        } catch (Throwable t) {
            log.error("Error while deleting quiz record [{}]", quizRecordId, t);
            return false;
        }
    }
}
