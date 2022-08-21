package zh.lingvo.rest.controllers;

import com.google.common.collect.ImmutableList;
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
import zh.lingvo.data.model.User;
import zh.lingvo.data.services.QuizService;
import zh.lingvo.rest.commands.LanguageCommand;
import zh.lingvo.rest.commands.QuizCommand;
import zh.lingvo.rest.converters.LanguageCommandToLanguage;
import zh.lingvo.rest.converters.LanguageToLanguageCommand;
import zh.lingvo.rest.converters.QuizToQuizCommand;
import zh.lingvo.rest.util.RequestContext;

import java.util.Optional;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesRegex;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test QuizController")
class QuizControllerTest {
    private static final Gson GSON = new Gson();
    private static final String URL = "/api/quizzes";
    private static final User USER = User.builder().id(1L).name("Test").build();
    private static final Language LANGUAGE_1 = Language.builder().id(1).name("Language 1").twoLetterCode("L1").build();
    private static final Language LANGUAGE_2 = Language.builder().id(2).name("Language 2").twoLetterCode("L2").build();
    private static final Quiz QUIZ_1 = Quiz.builder().id(1L).name("Q1").user(USER).language(LANGUAGE_1).build();
    private static final Quiz QUIZ_2 = Quiz.builder().id(2L).name("Q2").user(USER).language(LANGUAGE_2).build();

    @Mock
    private QuizService quizService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RequestContext context = new RequestContext();
        context.setUser(USER);

        QuizToQuizCommand quizConverter = new QuizToQuizCommand(new LanguageToLanguageCommand());
        LanguageCommandToLanguage languageCommandConverter = new LanguageCommandToLanguage();
        var quizController = new QuizController(
                quizService,
                quizConverter,
                languageCommandConverter,
                context);

        mockMvc = MockMvcBuilders.standaloneSetup(quizController)
                .setControllerAdvice(new ExceptionsAdvice())
                .build();
    }

    private String toPayload(Object object) {
        return GSON.toJson(object);
    }

    @Nested
    @DisplayName("Test GET /api/quizzes")
    class GetAllQuizzes {
        @Test
        @DisplayName("")
        void quizzesNotFound_ReturnEmptyList() throws Exception {
            when(quizService.findAll(USER)).thenReturn(ImmutableList.of());

            mockMvc.perform(get(URL))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$", is(empty())));

            verify(quizService, only()).findAll(USER);
        }

        @Test
        @DisplayName("Should return list of quizzes found for the user")
        void returnFoundQuizzes() throws Exception {
            when(quizService.findAll(USER)).thenReturn(ImmutableList.of(QUIZ_1, QUIZ_2));

            mockMvc.perform(get(URL))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].name", is("Q1")))
                    .andExpect(jsonPath("$[0].targetLanguage.id", is(1)))
                    .andExpect(jsonPath("$[1].id", is(2)))
                    .andExpect(jsonPath("$[1].name", is("Q2")))
                    .andExpect(jsonPath("$[1].targetLanguage.id", is(2)));

            verify(quizService, only()).findAll(USER);
        }
    }

    @Nested
    @DisplayName("Test GET /api/quizzes/{id}")
    class GetQuiz {
        private final String GET_QUIZ_URL_TEMPLATE = URL +"/{id}";

        @Test
        @DisplayName("Should return NOT FOUND 404 if the quiz does not exist for the user")
        void getQuiz_NotFound() throws Exception {
            long quizId = 1L;
            when(quizService.findById(quizId, USER)).thenReturn(Optional.empty());

            mockMvc.perform(get(GET_QUIZ_URL_TEMPLATE, quizId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*not found.*")));

            verify(quizService, only()).findById(quizId, USER);
        }

        @Test
        @DisplayName("Should return OK 200 when the quiz exists")
        void getQuiz_Found() throws Exception {
            long quizId = QUIZ_1.getId();
            when(quizService.findById(quizId, USER)).thenReturn(Optional.of(QUIZ_1));

            mockMvc.perform(get(GET_QUIZ_URL_TEMPLATE, quizId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.id", is((int) quizId)));

            verify(quizService, only()).findById(quizId, USER);
        }
    }

    @Nested
    @DisplayName("Test POST /api/quizzes/")
    class CreateQuiz {
        @Test
        @DisplayName("Should return BAD REQUEST 400 if the quiz payload contains an id")
        void createNewQuiz_QuizExists() throws Exception {
            long id = 1L;
            QuizCommand command = QuizCommand.builder().id(id).build();

            mockMvc.perform(post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*should not contain id.*")));

            verifyNoInteractions(quizService);
        }

        @Test
        @DisplayName("Should return OK 200, it the quiz is saved")
        void createNewQuiz_Success() throws Exception {
            long newId = 1L;
            when(quizService.save(any(Quiz.class), eq(USER)))
                    .then(invocation -> {
                        Quiz passedToSaved = invocation.getArgument(0, Quiz.class);
                        passedToSaved.setId(newId);
                        return Optional.of(passedToSaved);
                    });

            QuizCommand command = QuizCommand.builder().build();
            mockMvc.perform(post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.id", is((int) newId)));
        }
    }

    @Nested
    @DisplayName("Test PUT /api/quizzes/")
    class UpdateQuiz {
        @Test
        @DisplayName("Shoud return NOT FOUND 404 if the quiz does not exist")
        void quizDoesNotExist_Failure() throws Exception {
            long quizId = 1L;
            when(quizService.findById(quizId, USER)).thenReturn(Optional.empty());

            QuizCommand command = QuizCommand.builder()
                    .id(quizId)
                    .build();
            mockMvc.perform(put(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*not found.*")));

            verify(quizService, only()).findById(quizId, USER);
        }

        @Test
        @DisplayName("Should save the quiz and return OK 200 if the quiz exists")
        void quizExists_Success() throws Exception {
            long quizId = 1L;
            Quiz oldQuiz = Quiz.builder().id(quizId).user(USER).build();
            String newQuizName = "Test quiz name";
            when(quizService.findById(quizId, USER)).thenReturn(Optional.of(oldQuiz));
            when(quizService.save(any(Quiz.class), eq(USER)))
                    .then(invocation -> Optional.of(invocation.<Quiz>getArgument(0)));

            LanguageCommand langCommand = LanguageCommand.builder()
                    .id(LANGUAGE_1.getId())
                    .name(LANGUAGE_1.getName())
                    .code(LANGUAGE_1.getTwoLetterCode())
                    .build();
            QuizCommand command = QuizCommand.builder()
                    .id(quizId)
                    .name(newQuizName)
                    .targetLanguage(langCommand)
                    .build();
            mockMvc.perform(put(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.id", is((int) quizId)))
                    .andExpect(jsonPath("$.name", is(newQuizName)))
                    .andExpect(jsonPath("$.targetLanguage", is(notNullValue())))
                    .andExpect(jsonPath("$.targetLanguage.id", is(equalTo(LANGUAGE_1.getId()))))
                    .andExpect(jsonPath("$.targetLanguage.name", is(equalTo(LANGUAGE_1.getName()))))
                    .andExpect(jsonPath("$.targetLanguage.code", is(equalTo(LANGUAGE_1.getTwoLetterCode()))));

            verify(quizService, times(1)).findById(quizId, USER);
            verify(quizService, times(1)).save(any(Quiz.class), eq(USER));
            verifyNoMoreInteractions(quizService);
        }
    }

    @Nested
    @DisplayName("Test DELETE /api/quizzes/{id}")
    class DeleteQuiz {
        private final String DELETE_QUIZ_URL_TEMPLATE = URL +"/{id}";

        @Test
        @DisplayName("Should return SERVICE UNAVAILABLE 503 if deletion fails")
        void deletionFails() throws Exception {
            long quizId = QUIZ_1.getId();
            when(quizService.deleteById(quizId, USER)).thenReturn(false);

            mockMvc.perform(delete(DELETE_QUIZ_URL_TEMPLATE, quizId))
                    .andExpect(status().isServiceUnavailable())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*unavailable.*")));

            verify(quizService, only()).deleteById(quizId, USER);
        }

        @Test
        @DisplayName("Should return OK 200 and delete the quiz if it exists and belongs to the right user")
        void deleteDictionary_Success() throws Exception {
            long quizId = QUIZ_1.getId();
            when(quizService.deleteById(quizId, USER)).thenReturn(true);

            mockMvc.perform(delete(DELETE_QUIZ_URL_TEMPLATE, quizId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is((int) quizId)));
        }
    }
}