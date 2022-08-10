package zh.lingvo.data.services.impl;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zh.lingvo.core.domain.PartOfSpeech;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.QuizRecord;
import zh.lingvo.data.model.User;
import zh.lingvo.data.repositories.QuizRecordRepository;
import zh.lingvo.data.services.QuizRecordService;
import zh.lingvo.data.services.QuizService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test QuizRecordServiceImpl")
class QuizRecordServiceImplTest {
    private static final Long QUIZ_ID = 100L;

    @Mock
    private QuizService quizService;

    @Mock
    private QuizRecordRepository quizRecordRepository;

    private QuizRecordService service;

    private User user;
    private User anotherUser;
    private Quiz quiz;
    private QuizRecord record;

    @BeforeEach
    void setUpService() {
        service = new QuizRecordServiceImpl(
                quizService, quizRecordRepository);
    }

    @BeforeEach
    void setUpEntities() {
        user = User.builder().id(1L).build();
        anotherUser = User.builder().id(2L).build();
        quiz = Quiz.builder().id(QUIZ_ID).user(user).build();
        record = QuizRecord.builder()
                .quiz(quiz)
                .wordMainForm("test")
                .numberOfSuccesses(0)
                .numberOfRuns(0)
                .currentScore(0.0f)
                .pos(PartOfSpeech.NOUN)
                .build();
    }

    @Nested
    @DisplayName("Test QuizRecordServiceImpl.findAll(id, user)")
    class FindAll {
        @Test
        @DisplayName("Should return empty list if no quiz is found for the user")
        void noQuizFoundForUser() {
            when(quizService.findById(QUIZ_ID, user)).thenReturn(Optional.empty());

            List<QuizRecord> records = service.findAll(QUIZ_ID, user);

            assertThat(records, is(empty()));
            verify(quizService, only()).findById(QUIZ_ID, user);
            verifyNoMoreInteractions(quizService);
        }

        @Test
        @DisplayName("Should return the list of quiz records if the quiz is found for the user")
        void quizIsFoundForUser() {
            when(quizService.findById(QUIZ_ID, user)).thenReturn(Optional.of(quiz));
            when(quizRecordRepository.findAllByQuiz(quiz)).thenReturn(ImmutableList.of(record));

            List<QuizRecord> records = service.findAll(QUIZ_ID, user);

            assertThat(records, is(equalTo(ImmutableList.of(record))));

            verify(quizService, only()).findById(QUIZ_ID, user);
            verify(quizRecordRepository, only()).findAllByQuiz(quiz);
            verifyNoMoreInteractions(quizService, quizRecordRepository);
        }
    }
}