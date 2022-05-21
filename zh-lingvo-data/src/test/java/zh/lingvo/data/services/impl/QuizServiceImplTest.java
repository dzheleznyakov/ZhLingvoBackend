package zh.lingvo.data.services.impl;

import com.google.common.collect.ImmutableList;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.User;
import zh.lingvo.data.repositories.QuizRepository;
import zh.lingvo.data.services.QuizService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static zh.hamcrest.ZhMatchers.empty;
import static zh.hamcrest.ZhMatchers.hasPropertySatisfying;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test implemented Quiz service")
class QuizServiceImplTest {
    @Mock
    private QuizRepository quizRepository;

    private QuizService service;
    private static final Long ID = 42L;

    private final User user = User.builder().id(1L).build();
    private final User otherUser = User.builder().id(2L).build();

    @BeforeEach
    void setUp() {
        service = new QuizServiceImpl(quizRepository);
    }

    @Nested
    @DisplayName("Test QuizServiceImpl.findById(id, user)")
    class FindById {
        @Test
        @DisplayName("Should return nothing if no quiz is found by the id for the user")
        void foundNothing_returnNothing() {
            when(quizRepository.findById(ID)).thenReturn(Optional.empty());

            Optional<Quiz> quizOptional = service.findById(ID, user);

            assertThat(quizOptional, is(empty()));
            verify(quizRepository, only()).findById(ID);
        }

        @Test
        @DisplayName("Should return nothing if the quiz is found by id, but it does not belong to the user")
        void foundNotForUser_returnNothing() {
            Quiz quiz = Quiz.builder().user(otherUser).id(ID).build();
            when(quizRepository.findById(ID)).thenReturn(Optional.of(quiz));

            Optional<Quiz> quizOptional = service.findById(ID, user);

            assertThat(quizOptional, is(empty()));
            verify(quizRepository, only()).findById(ID);
        }

        @Test
        @DisplayName("Should return the quiz if it is found by id and it belongs to the user")
        void returnQuizIfFound() {
            Quiz quiz = Quiz.builder().user(user).id(ID).build();
            when(quizRepository.findById(ID)).thenReturn(Optional.of(quiz));

            Optional<Quiz> quizOptional = service.findById(ID, user);

            assertThat(quizOptional, is(not(empty())));
            assertThat(quizOptional, hasPropertySatisfying(Quiz::getId, ID::equals));
            verify(quizRepository, only()).findById(ID);
        }
    }

    @Nested
    @DisplayName("Test QuizServiceImpl.findAll(user)")
    class FindAll {
        @Test
        @DisplayName("Should return empty list if the user does not have quizes")
        void userHasNoQuizes_ReturnEmptyList() {
            when(quizRepository.findAllByUser(user)).thenReturn(ImmutableList.of());

            List<Quiz> quizzes = service.findAll(user);

            assertThat(quizzes, is(Matchers.empty()));
            verify(quizRepository, only()).findAllByUser(user);
        }

        @Test
        @DisplayName("Should return all found user's quizzes")
        void userHasQuizzes_ReturnThem() {
            Quiz quiz1 = Quiz.builder().id(1L).build();
            Quiz quiz2 = Quiz.builder().id(2L).build();
            when(quizRepository.findAllByUser(user))
                    .thenReturn(ImmutableList.of(quiz1, quiz2));

            List<Quiz> quizzes = service.findAll(user);

            assertThat(quizzes, is(equalTo(ImmutableList.of(quiz1, quiz2))));
            verify(quizRepository, only()).findAllByUser(user);
        }
    }

    @Nested
    @DisplayName("Test QuizServiceImpl.save(quiz, user)")
    class Save {
        @Test
        @DisplayName("Should save the existing quiz if it belongs to the user")
        void belongsToUser_Success() {
            String quizName = "Test quiz";
            Quiz quizToSave = Quiz.builder().id(ID).name(quizName).build();
            Quiz persistedQuiz = Quiz.builder().id(ID).name(quizName).build();

            when(quizRepository.existsByIdAndUser(ID, user)).thenReturn(true);
            when(quizRepository.save(quizToSave)).thenReturn(persistedQuiz);

            Optional<Quiz> savedQuizOptional = service.save(quizToSave, user);

            assertThat(savedQuizOptional, is(not(empty())));
            assertThat(savedQuizOptional, hasPropertySatisfying(Quiz::getId, ID::equals));
            assertThat(savedQuizOptional, hasPropertySatisfying(Quiz::getName, quizName::equals));
            verify(quizRepository, times(1)).existsByIdAndUser(ID, user);
            verify(quizRepository, times(1)).save(quizToSave);
            verifyNoMoreInteractions(quizRepository);
        }

        @Test
        @DisplayName("Should not save the existing quiz if it belongs to another user")
        void belongsToAnotherUser_DoNotSave() {
            String quizName = "Test quiz";
            Quiz quizToSave = Quiz.builder().id(ID).name(quizName).build();

            when(quizRepository.existsByIdAndUser(ID, user)).thenReturn(false);

            Optional<Quiz> savedQuizOptional = service.save(quizToSave, user);

            assertThat(savedQuizOptional, is(empty()));
            verify(quizRepository, only()).existsByIdAndUser(ID, user);
        }

        @Test
        @DisplayName("Should save the new quiz for the given user")
        void newDictionary_ShouldSave() {
            String quizName = "Test quiz";
            Quiz quizToSave = Quiz.builder().name(quizName).build();
            Quiz persistedDic = Quiz.builder().id(ID).name(quizName).user(user).build();

            when(quizRepository.save(quizToSave)).thenAnswer(invocation -> {
                Quiz passedQuiz = invocation.getArgument(0);
                assertThat(passedQuiz.getUser(), is(user));
                return persistedDic;
            });

            Optional<Quiz> savedQuizOptional = service.save(quizToSave, user);

            assertThat(savedQuizOptional, is(not(empty())));
            assertThat(savedQuizOptional, hasPropertySatisfying(Quiz::getId, ID::equals));
            assertThat(savedQuizOptional, hasPropertySatisfying(Quiz::getName, quizName::equals));
            assertThat(savedQuizOptional, hasPropertySatisfying(Quiz::getUser, u -> user == u));
            verify(quizRepository, only()).save(quizToSave);
        }
    }

    @Nested
    @DisplayName("Test QuizServiceImple.deleteById(id, user)")
    class DeleteById {
        private final Quiz quiz = Quiz.builder()
                .id(ID)
                .user(user)
                .name("Test: delete by ID")
                .build();

        @Test
        @DisplayName("Should return true if the deletion is successful")
        void deletionSuccessful() {
            when(quizRepository.findByIdAndUser(ID, user)).thenReturn(Optional.of(quiz));

            boolean deleted = service.deleteById(ID, user);

            assertThat(deleted, is(true));
            verify(quizRepository, times(1)).findByIdAndUser(ID, user);
            verify(quizRepository, times(1)).delete(quiz);
            verifyNoMoreInteractions(quizRepository);
        }

        @Test
        @DisplayName("Should return false if there is an error with the deletion")
        void deletionFails() {
            when(quizRepository.findByIdAndUser(ID, user)).thenReturn(Optional.of(quiz));
            doThrow(new RuntimeException("Something went terribly wrong"))
                    .when(quizRepository).delete(quiz);

            boolean deleted = service.deleteById(ID, user);

            assertThat(deleted, is(false));
            verify(quizRepository, times(1)).findByIdAndUser(ID, user);
            verify(quizRepository, times(1)).delete(quiz);
            verifyNoMoreInteractions(quizRepository);
        }

        @Test
        @DisplayName("Should return true if the quiz is not found for this user")
        void dictionaryNotFound() {
            when(quizRepository.findByIdAndUser(ID, user)).thenReturn(Optional.empty());

            boolean deleted = service.deleteById(ID, user);

            assertThat(deleted, is(true));
            verify(quizRepository, only()).findByIdAndUser(ID, user);
            verifyNoMoreInteractions(quizRepository);
        }
    }
}