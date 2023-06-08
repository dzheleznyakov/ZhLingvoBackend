package zh.lingvo.rest.controllers;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.QuizRun;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.enums.MatchingRegime;
import zh.lingvo.data.model.enums.QuizRegime;
import zh.lingvo.data.repositories.QuizRepository;
import zh.lingvo.data.repositories.QuizRunRepository;
import zh.lingvo.data.services.QuizRunService;
import zh.lingvo.data.services.QuizService;
import zh.lingvo.data.services.impl.QuizRunServiceImpl;
import zh.lingvo.data.services.impl.QuizServiceImpl;
import zh.lingvo.rest.commands.QuizRunCommand;
import zh.lingvo.rest.converters.QuizRunCommandToQuizRun;
import zh.lingvo.rest.converters.QuizRunToQuizRunCommand;
import zh.lingvo.rest.util.RequestContext;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesRegex;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test QuizRunController")
public class QuizRunControllerTest {
    private static final Gson GSON = new Gson();
    private static final String BASE_URL_PATTERN = "/api/quizzes/{id}/runs";
    private static final User USER_1 = User.builder().id(1L).name("Test user").build();
    private static final User USER_2 = User.builder().id(2L).name("Wrong test user").build();
    private static final Language LANGUAGE = Language.builder().id(1).name("Language 1").twoLetterCode("L1").build();
    private static final Quiz QUIZ_1 = Quiz.builder().id(1L).name("Quiz").user(USER_1).language(LANGUAGE).quizRegime(QuizRegime.FORWARD).matchingRegime(MatchingRegime.LOOSENED).build();
    private static final Quiz QUIZ_2 = Quiz.builder().id(2L).name("Wrong user quiz").user(USER_2).language(LANGUAGE).name("Quiz").build();

    @Mock
    private QuizRepository quizRepository;
    @Mock
    QuizRunRepository quizRunRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RequestContext context = new RequestContext();
        context.setUser(USER_1);

        QuizService quizService = new QuizServiceImpl(quizRepository);
        QuizRunService quizRunService = new QuizRunServiceImpl(quizRunRepository, quizService);
        QuizRunController controller = new QuizRunController(
                new QuizRunCommandToQuizRun(),
                new QuizRunToQuizRunCommand(),
                quizRunService,
                context);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionsAdvice())
                .build();
    }

    private static String toPayload(Object object) {
        return GSON.toJson(object);
    }

    @Nested
    @DisplayName("Test POST /api/quizzes/{id}/runs")
    class CreateQuizRun {
        @Test
        @DisplayName("Should return NOT FOUND 404 if the quiz belongs to a wrong user")
        void wrongUser_ReturnNotFound() throws Exception {
            Long quizId = QUIZ_2.getId();
            when(quizRepository.findById(quizId)).thenReturn(Optional.empty());

            QuizRunCommand command = QuizRunCommand.builder()
                    .build();

            mockMvc.perform(post(BASE_URL_PATTERN, quizId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*not found.*")));

            verify(quizRepository, times(1)).findById(quizId);
            verifyNoMoreInteractions(quizRepository);
            verifyNoInteractions(quizRunRepository);
        }

        @Test
        @DisplayName("Should return BAD REQUEST 400 if quiz run has a non-null id")
        void idExists_ReturnBadRequest() throws Exception {
            Long quizId = QUIZ_1.getId();

            QuizRunCommand command = QuizRunCommand.builder()
                    .id(quizId)
                    .build();

            mockMvc.perform(post(BASE_URL_PATTERN, quizId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*should not.*")));

            verifyNoInteractions(quizRunRepository, quizRepository);
        }

        @Test
        @DisplayName("Should return OK 200, if the quiz run is saved")
        void createNewQuizRun_Success() throws Exception {
            Long quizId = QUIZ_1.getId();
            Long newId = 42L;
            when(quizRepository.findById(quizId)).thenReturn(Optional.of(QUIZ_1));
            when(quizRunRepository.save(any(QuizRun.class)))
                    .thenAnswer(invocation -> {
                        QuizRun quizRun = invocation.getArgument(0);
                        quizRun.setId(newId);
                        return quizRun;
                    });

            QuizRunCommand command = QuizRunCommand.builder()
                    .quizRegime("b")
                    .matchingRegime("r")
                    .build();
            mockMvc.perform(post(BASE_URL_PATTERN, quizId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.id", is(newId.intValue())))
                    .andExpect(jsonPath("$.quizId", is(quizId.intValue())));

            verify(quizRepository, times(1)).findById(quizId);
            verify(quizRunRepository, times(1)).save(any(QuizRun.class));
            verifyNoMoreInteractions(quizRepository, quizRunRepository);
        }
    }

    @Nested
    @DisplayName("Test PUT /api/quizzes/{id}/runs")
    class UpdateQuizRun {
        private final Long QUIZ_ID = QUIZ_1.getId();

        @Test
        @DisplayName("Should return BAD REQUEST 400 if the quiz run does not have id")
        void quizRunDoesNotHaveId_ReturnBadRequest() throws Exception {
            QuizRunCommand command = QuizRunCommand.builder().build();

            mockMvc.perform(put(BASE_URL_PATTERN, QUIZ_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*should have.*")));
        }

        @Test
        @DisplayName("Should return NOT FOUND 404 if the quiz run does not belong to the user")
        void quizRunNotForUser_ReturnNotFound() throws Exception {
            when(quizRunRepository.findByIdAndUser(any(Long.class), eq(USER_1)))
                    .thenReturn(Optional.empty());

            Long id = 42L;
            QuizRunCommand command = QuizRunCommand.builder()
                    .id(id)
                    .quizId(QUIZ_ID)
                    .build();

            mockMvc.perform(put(BASE_URL_PATTERN, QUIZ_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*not found.*")));

            verify(quizRunRepository, times(1)).findByIdAndUser(any(Long.class), eq(USER_1));
            verifyNoMoreInteractions(quizRunRepository);
            verifyNoInteractions(quizRepository);
        }

        @Test
        @DisplayName("Should return OK 200 if the quiz run is successfully saved")
        void updateQuizRun_Success() throws Exception {
            Long id = 42L;
            QuizRun persistedQuizRun = QuizRun.builder()
                    .id(id)
                    .quiz(QUIZ_1)
                    .quizRegime(QuizRegime.FORWARD)
                    .matchingRegime(MatchingRegime.LOOSENED)
                    .build();
            when(quizRunRepository.findByIdAndUser(id, USER_1)).thenReturn(Optional.of(persistedQuizRun));
            when(quizRunRepository.save(any(QuizRun.class))).thenAnswer(invocation -> invocation.getArgument(0));

            QuizRunCommand command = QuizRunCommand.builder()
                    .id(id)
                    .quizRegime("f")
                    .matchingRegime("l")
                    .build();

            mockMvc.perform(put(BASE_URL_PATTERN, QUIZ_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.id", is(id.intValue())));

            verify(quizRunRepository, times(1)).findByIdAndUser(id, USER_1);
            verify(quizRunRepository, times(1)).save(any(QuizRun.class));
            verifyNoMoreInteractions(quizRunRepository);
            verifyNoInteractions(quizRepository);
        }
    }
}
