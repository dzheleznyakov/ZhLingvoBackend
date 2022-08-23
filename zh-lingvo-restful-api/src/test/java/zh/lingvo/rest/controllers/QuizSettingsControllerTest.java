package zh.lingvo.rest.controllers;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.enums.MatchingRegime;
import zh.lingvo.data.model.enums.QuizRegime;
import zh.lingvo.data.services.QuizService;
import zh.lingvo.rest.commands.QuizSettingsCommand;
import zh.lingvo.rest.converters.QuizToQuizSettingsCommand;
import zh.lingvo.rest.util.RequestContext;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesRegex;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test QuizSettingsController")
class QuizSettingsControllerTest {
    private static final Gson GSON = new Gson();
    private static final String URL_PATTERN = "/api/quizzes/{id}/settings";

    private static final User USER = User.builder().id(1L).name("Test").build();
    private static final Quiz QUIZ = Quiz.builder()
            .id(1L)
            .name("Q")
            .user(USER)
            .maxScore(1)
            .matchingRegime(MatchingRegime.LOOSENED)
            .quizRegime(QuizRegime.FORWARD)
            .build();

    @Mock
    private QuizService quizService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RequestContext context = new RequestContext();
        context.setUser(USER);

        QuizSettingsController controller = new QuizSettingsController(
                quizService,
                new QuizToQuizSettingsCommand(),
                context);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionsAdvice())
                .build();
    }

    private String toPayload(Object object) {
        return GSON.toJson(object);
    }

    @Test
    @DisplayName("Should return NOT FOUND 404 if the quiz does not exist for the user")
    void getSettings_NotFound() throws Exception {
        Long quizId = QUIZ.getId();
        when(quizService.findById(quizId, USER)).thenReturn(Optional.empty());

        mockMvc.perform(get(URL_PATTERN, quizId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is(notNullValue())))
                .andExpect(jsonPath("$.message", matchesRegex(".*not found.*")));

        verify(quizService, only()).findById(quizId, USER);
    }

    @Test
    @DisplayName("Should return OK 200 along with quiz setting when the quiz exists for the user")
    void getSettings_Found() throws Exception {
        Long quizId = QUIZ.getId();
        when(quizService.findById(quizId, USER)).thenReturn(Optional.of(QUIZ));

        mockMvc.perform(get(URL_PATTERN, quizId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(notNullValue())))
                .andExpect(jsonPath("$.quizId", is(quizId.intValue())))
                .andExpect(jsonPath("$.maxScore", is(QUIZ.getMaxScore())))
                .andExpect(jsonPath("$.quizRegime", is(QUIZ.getQuizRegime().getCode())))
                .andExpect(jsonPath("$.matchingRegime", is(QUIZ.getMatchingRegime().getCode())));

        verify(quizService, only()).findById(quizId, USER);
    }

    @Test
    @DisplayName("Should return NOT FOUND 404 if the quiz does not exist")
    void updateSettings_NotFound() throws Exception {
        Long quizId = QUIZ.getId();
        when(quizService.findById(quizId, USER)).thenReturn(Optional.empty());

        QuizSettingsCommand command = QuizSettingsCommand.builder()
                .quizId(quizId)
                .build();
        mockMvc.perform(put(URL_PATTERN, quizId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toPayload(command))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is(notNullValue())))
                .andExpect(jsonPath("$.message", matchesRegex(".*not found.*")));

        verify(quizService, only()).findById(quizId, USER);
    }

    @Test
    @DisplayName("Should update settings and return OK 200 if the quiz exists for the user")
    void updateSettings_Found() throws Exception {
        Long quizId = QUIZ.getId();
        Quiz oldQuiz = Quiz.builder()
                .id(quizId)
                .maxScore(QUIZ.getMaxScore())
                .quizRegime(QUIZ.getQuizRegime())
                .matchingRegime(QUIZ.getMatchingRegime())
                .build();
        Integer newMaxScore = 42;
        String newQuizRegime = QuizRegime.ALTERNATING.getCode();
        String newMatchingRegime = MatchingRegime.STRICT.getCode();

        when(quizService.findById(quizId, USER)).thenReturn(Optional.of(oldQuiz));
        when(quizService.save(any(Quiz.class), eq(USER)))
                .then(invocation -> Optional.of(invocation.<Quiz>getArgument(0)));

        QuizSettingsCommand settings = QuizSettingsCommand.builder()
                .maxScore(newMaxScore)
                .quizRegime(newQuizRegime)
                .matchingRegime(newMatchingRegime)
                .build();
        mockMvc.perform(put(URL_PATTERN, quizId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toPayload(settings))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(notNullValue())))
                .andExpect(jsonPath("$.quizId", is(quizId.intValue())))
                .andExpect(jsonPath("$.maxScore", is(newMaxScore)))
                .andExpect(jsonPath("$.quizRegime", is(newQuizRegime)))
                .andExpect(jsonPath("$.matchingRegime", is(newMatchingRegime)));

        verify(quizService, times(1)).findById(quizId, USER);
        verify(quizService, times(1)).save(any(Quiz.class), eq(USER));
        verifyNoMoreInteractions(quizService);
    }
}