package zh.lingvo.data.services.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.QuizRun;
import zh.lingvo.data.model.User;
import zh.lingvo.data.repositories.QuizRecordRepository;
import zh.lingvo.data.repositories.QuizRunRepository;
import zh.lingvo.data.services.QuizRunService;
import zh.lingvo.data.services.QuizService;
import zh.lingvo.data.services.impl.utils.MockingClock;
import zh.lingvo.util.ClockTestHelper;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static zh.hamcrest.ZhMatchers.empty;
import static zh.hamcrest.ZhMatchers.hasPropertySatisfying;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test implemented QuizRun service")
class QuizRunServiceImplTest {
    private final Language LANGUAGE_1 = Language.builder().name("Language 1").twoLetterCode("L1").build();
    private final User USER_1 = User.builder().name("User 1").build();
    private final User USER_2 = User.builder().name("User 2").build();
    private final Quiz QUIZ_1 = Quiz.builder().name("Quiz 1").language(LANGUAGE_1).user(USER_1).build();
    private final Long QUIZ_RUN_ID = 42L;
    private final Long QUIZ_ID_1 = 100L;

    private QuizRunService service;

    private final MockingClock clock = new MockingClock();

    @Mock
    private QuizRunRepository quizRunRepository;
    @Mock
    private QuizService quizService;
    @Mock
    private QuizRecordRepository quizRecordRepository;

    @BeforeEach
    void setUp() {
        service = new QuizRunServiceImpl(quizRunRepository, quizService, quizRecordRepository);
        ClockTestHelper.setBasicClock(clock);
    }

    @Nested
    @DisplayName("Test QuizRunService.findById(id, user)")
    class FindById {
        @Test
        @DisplayName("Should automatically set up createdTimestamp")
        void testCreatedTimestamp() {
            long createdTimestamp = 1L;
            clock.setTime(createdTimestamp);

            QuizRun quizRun = newQuizRun();

            assertThat(quizRun.getCreatedTimestamp(), is(createdTimestamp));
        }

        @Test
        @DisplayName("Should automatically update accessed timestamp when the quiz run is got by id")
        void testAccessedTimestamp() {
            Long createdTimestamp = 1L;
            Long accessedTimestamp1 = 10L;
            clock.setTime(createdTimestamp, accessedTimestamp1);

            QuizRun quizRun = newQuizRun();
            when(quizRunRepository.findByIdAndUser(QUIZ_RUN_ID, USER_1)).thenReturn(Optional.of(quizRun));

            Optional<QuizRun> optionalQuizRun = service.findById(QUIZ_RUN_ID, USER_1);

            assertThat(optionalQuizRun, is(not(empty())));
            assertThat(optionalQuizRun, hasPropertySatisfying(QuizRun::getAccessedTimestamp, accessedTimestamp1::equals));

            verify(quizRunRepository, times(1)).findByIdAndUser(QUIZ_RUN_ID, USER_1);
            verify(quizRunRepository, times(1)).save(quizRun);
            verifyNoMoreInteractions(quizRunRepository);
        }
    }

    @Nested
    @DisplayName("Test QuizRunService.findAllByQuiz(quiz, user)")
    class findAllByQuiz {
        @Test
        @DisplayName("Should not update accessed timestamp in the retrieved quiz runs")
        void testNoChangeToAccessedTimestamp() {
            long createdTimeStamp = 1L;
            long accessedTimestamp = 10L;
            long wrongAccessedTimestamp = 100L;
            clock.setTime(createdTimeStamp, wrongAccessedTimestamp);

            QuizRun quizRun = newQuizRun();
            quizRun.setAccessedTimestamp(accessedTimestamp);
            when(quizRunRepository.findAllByQuizAndUser(QUIZ_1.getId(), USER_1)).thenReturn(ImmutableList.of(quizRun));

            List<QuizRun> quizRuns = service.findAllByQuiz(QUIZ_1, USER_1);

            assertThat(quizRuns, hasSize(1));
            QuizRun qr = quizRuns.get(0);
            assertThat(qr.getCreatedTimestamp(), is(createdTimeStamp));
            assertThat(qr.getAccessedTimestamp(), is(accessedTimestamp));

            verify(quizRunRepository, times(1)).findAllByQuizAndUser(QUIZ_1.getId(), USER_1);
            verifyNoMoreInteractions(quizRunRepository);
        }
    }

    @Nested
    @DisplayName("Test QuizRunService.findAllByUser(user)")
    class FindAllByUser {
        @Test
        @DisplayName("Should not update accessed timestamp in the retrieved quiz runs")
        void testNotChangeToAccessedTimestamp() {
            long createdTimeStamp = 1L;
            long accessedTimestamp = 10L;
            long wrongAccessedTimestamp = 100L;
            clock.setTime(createdTimeStamp, wrongAccessedTimestamp);

            QuizRun quizRun = newQuizRun();
            quizRun.setAccessedTimestamp(accessedTimestamp);
            when(quizRunRepository.findAllByUser(USER_1)).thenReturn(ImmutableList.of(quizRun));

            List<QuizRun> quizRuns = service.findAllByUser(USER_1);

            assertThat(quizRuns, hasSize(1));
            QuizRun qr = quizRuns.get(0);
            assertThat(qr.getCreatedTimestamp(), is(createdTimeStamp));
            assertThat(qr.getAccessedTimestamp(), is(accessedTimestamp));

            verify(quizRunRepository, times(1)).findAllByUser(USER_1);
            verifyNoMoreInteractions(quizRunRepository);
        }
    }

    @Nested
    @DisplayName("Test QuizRunService.update(quizRun, user)")
    class Update {
        @Test
        @DisplayName("Should automatically update timestamp when the quiz run is saved")
        void testAccessedTimestampIsUpdated() {
            long createdTimeStamp = 1L;
            long accessedTimestamp = 10L;
            clock.setTime(createdTimeStamp, accessedTimestamp);

            QuizRun quizRun = newQuizRun();
            when(quizRunRepository.findByIdAndUser(QUIZ_RUN_ID, USER_1)).thenReturn(Optional.of(quizRun));
            when(quizRunRepository.save(quizRun)).thenReturn(quizRun);

            Optional<QuizRun> savedQuizRun = service.update(quizRun, USER_1);

            assertThat(savedQuizRun, is(not(empty())));
            assertThat(savedQuizRun, hasPropertySatisfying(QuizRun::getAccessedTimestamp, equalTo(accessedTimestamp)));

            verify(quizRunRepository, times(1)).findByIdAndUser(QUIZ_RUN_ID, USER_1);
            verify(quizRunRepository, times(1)).save(quizRun);
            verifyNoMoreInteractions(quizRunRepository);
        }

        @Test
        @DisplayName("Should not update the user if the quiz run does not belong to them")
        void testNotUpdatedIfUserIsWrong() {
            QuizRun quizRun = newQuizRun();
            when(quizRunRepository.findByIdAndUser(QUIZ_RUN_ID, USER_2)).thenReturn(Optional.empty());

            Optional<QuizRun> savedQuizRun = service.update(quizRun, USER_2);

            assertThat(savedQuizRun, is(empty()));

            verify(quizRunRepository, times(1)).findByIdAndUser(QUIZ_RUN_ID, USER_2);
            verifyNoMoreInteractions(quizRunRepository);
        }
    }

    @Nested
    @DisplayName("Test QuizRunService.create(quizRun, quizId, user")
    class Create {
        @Test
        @DisplayName("Should not create a new test run if the quiz does not belong to the user")
        void testNotCreatedIfUserIsWrong() {
            QuizRun quizRun = newQuizRun();
            quizRun.setId(null);

            when(quizService.findById(QUIZ_ID_1, USER_1)).thenReturn(Optional.empty());

            Optional<QuizRun> optionalPersistedQuizRun = service.create(quizRun, QUIZ_ID_1, USER_1);

            assertThat(optionalPersistedQuizRun, is(empty()));

            verify(quizService, times(1)).findById(QUIZ_ID_1, USER_1);
            verifyNoMoreInteractions(quizService);
            verifyNoInteractions(quizRunRepository);
        }

        @Test
        @DisplayName("Should create a new test run if the quiz belongs to the user")
        void testHappyPath() {
            QuizRun quizRun = newQuizRun();
            quizRun.setId(null);

            when(quizService.findById(QUIZ_ID_1, USER_1)).thenReturn(Optional.of(QUIZ_1));
            when(quizRunRepository.save(quizRun)).thenReturn(quizRun);

            Optional<QuizRun> optionalPersistedQuizRun = service.create(quizRun, QUIZ_ID_1, USER_1);

            assertThat(optionalPersistedQuizRun, is(not(empty())));

            verify(quizService, times(1)).findById(QUIZ_ID_1, USER_1);
            verify(quizRunRepository, times(1)).save(quizRun);
            verifyNoMoreInteractions(quizService, quizRunRepository);
        }
    }

    @Nested
    @DisplayName("Test QuizRunService.deleteById(id, user)")
    class DeleteById {
        @Test
        @DisplayName("Should not delete the quiz run if it does not belong to this user")
        void testQuizRunDoesNotBelongToUser() {
            when(quizRunRepository.findByIdAndUser(QUIZ_RUN_ID, USER_1)).thenReturn(Optional.empty());

            boolean successful = service.deleteById(QUIZ_RUN_ID, USER_1);

            assertThat(successful, is(false));

            verify(quizRunRepository, times(1)).findByIdAndUser(QUIZ_RUN_ID, USER_1);
            verifyNoMoreInteractions(quizRunRepository);
            verifyNoInteractions(quizService);
        }

        @Test
        @DisplayName("Should delete the quiz run if it belongs to this user")
        void testHappyPath() {
            when(quizRunRepository.findByIdAndUser(QUIZ_RUN_ID, USER_1)).thenReturn(Optional.of(newQuizRun()));

            boolean successful = service.deleteById(QUIZ_RUN_ID, USER_1);

            assertThat(successful, is(true));

            verify(quizRunRepository, times(1)).findByIdAndUser(QUIZ_RUN_ID, USER_1);
            verify(quizRunRepository, times(1)).deleteById(QUIZ_RUN_ID);
            verifyNoMoreInteractions(quizRunRepository);
            verifyNoInteractions(quizService);
        }

        @Test
        @DisplayName("Should return false if there is an error on the persistence side")
        void testDeletionThrows() {
            when(quizRunRepository.findByIdAndUser(QUIZ_RUN_ID, USER_1)).thenReturn(Optional.of(newQuizRun()));
            doThrow(new RuntimeException("Something went terribly wron")).when(quizRunRepository).deleteById(QUIZ_RUN_ID);

            boolean successful = service.deleteById(QUIZ_RUN_ID, USER_1);

            assertThat(successful, is(false));

            verify(quizRunRepository, times(1)).findByIdAndUser(QUIZ_RUN_ID, USER_1);
            verify(quizRunRepository, times(1)).deleteById(QUIZ_RUN_ID);
            verifyNoMoreInteractions(quizRunRepository);
            verifyNoInteractions(quizService);
        }
    }

    private QuizRun newQuizRun() {
        return QuizRun.builder()
                .id(QUIZ_RUN_ID)
                .quiz(QUIZ_1)
                .quizRegime(QUIZ_1.getQuizRegime())
                .matchingRegime(QUIZ_1.getMatchingRegime())
                .records(ImmutableList.of(1L, 2L, 3L, 4L))
                .doneRecords(ImmutableMap.of(1L, true, 3L, false))
                .build();
    }
}