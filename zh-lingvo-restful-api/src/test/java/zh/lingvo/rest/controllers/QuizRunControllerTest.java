package zh.lingvo.rest.controllers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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
import zh.lingvo.core.domain.PartOfSpeech;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.QuizRecord;
import zh.lingvo.data.model.QuizRun;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.enums.MatchingRegime;
import zh.lingvo.data.model.enums.QuizRegime;
import zh.lingvo.data.repositories.QuizRecordRepository;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesRegex;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private final Quiz QUIZ_1 = Quiz.builder().id(1L).name("Quiz").user(USER_1).language(LANGUAGE).quizRegime(QuizRegime.FORWARD).matchingRegime(MatchingRegime.LOOSENED).build();
    private final Quiz QUIZ_2 = Quiz.builder().id(2L).name("Wrong user quiz").user(USER_2).language(LANGUAGE).name("Quiz").build();

    @Mock
    private QuizRepository quizRepository;
    @Mock
    private QuizRecordRepository quizRecordRepository;
    @Mock
    QuizRunRepository quizRunRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RequestContext context = new RequestContext();
        context.setUser(USER_1);

        QuizService quizService = new QuizServiceImpl(quizRepository);
        QuizRunService quizRunService = new QuizRunServiceImpl(quizRunRepository, quizService, quizRecordRepository);
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

    @Nested
    @DisplayName("Test PUT /api/quizzes/{id}/runs/{runId}/complete")
    class CompleteQuizRun {
        private final String URL = BASE_URL_PATTERN + "/{runId}/complete";
        private final Long QUIZ_ID = QUIZ_1.getId();
        private final Long QUIZ_RUN_ID = 42L;

        private final QuizRecord QUIZ_RECORD_1 = buildQuizRecord(101L, "alpha", 0.1f, 10, 1);
        private final QuizRecord QUIZ_RECORD_2 = buildQuizRecord(102L, "beta", 0.97f, 15, 8);
        private final QuizRecord QUIZ_RECORD_3 = buildQuizRecord(103L, "gamma", 0f, 10, 2);
        private final QuizRecord QUIZ_RECORD_4 = buildQuizRecord(104L, "delta", 1f, 10, 1);

        private QuizRecord buildQuizRecord(
                Long id,
                String wordMainForm,
                Float currentScore,
                Integer numberOfRuns,
                Integer numberOfSuccesses
        ) {
            return QuizRecord.builder()
                    .id(id)
                    .quiz(QUIZ_1)
                    .wordMainForm(wordMainForm)
                    .pos(PartOfSpeech.NOUN)
                    .currentScore(currentScore)
                    .numberOfRuns(numberOfRuns)
                    .numberOfSuccesses(numberOfSuccesses)
                    .build();
        }

        @Test
        @DisplayName("Should return BAD REQUEST 400 if the submitted quiz run still has remaining records")
        void quizRunHasRemainingRecords_BadRequest() throws Exception {
            QuizRunCommand command = getQuizRunCommand(ImmutableList.of(1L));

            mockMvc.perform(put(URL, QUIZ_ID, QUIZ_RUN_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*should not.*")));

            verifyNoInteractions(quizRepository, quizRunRepository);
        }

        @Test
        @DisplayName("Should return OK 200 if the quiz run has empty doneRecords")
        void doneRecordsIsEmpty_Complete() throws Exception {
            QuizRunCommand command = getQuizRunCommand(ImmutableMap.of());

            mockMvc.perform(put(URL, QUIZ_ID, QUIZ_RUN_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toPayload(command))
            ).andExpect(status().isOk());

            verifyNoInteractions(quizRepository, quizRunRepository);
        }

        @Test
        @DisplayName("Should return NOT FOUND 404 if the quiz run not found for the user")
        void quizRunNotFound_ReturnNotFound() throws Exception {
            QuizRunCommand command = getQuizRunCommand();

            when(quizRunRepository.findByIdAndUser(QUIZ_RUN_ID, USER_1)).thenReturn(Optional.empty());

            mockMvc.perform(put(URL, QUIZ_ID, QUIZ_RUN_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*not found.*")));

            verify(quizRunRepository, times(1)).findByIdAndUser(QUIZ_RUN_ID, USER_1);
            verifyNoMoreInteractions(quizRepository, quizRepository);
        }

        @Test
        @DisplayName("Should return OK 200 if the quiz in not found for the user")
        void quizNotFound_ReturnOK() throws Exception {
            QuizRunCommand command = getQuizRunCommand();

            when(quizRunRepository.findByIdAndUser(QUIZ_RUN_ID, USER_1)).thenReturn(Optional.of(new QuizRun()));
            when(quizRepository.findById(QUIZ_ID)).thenReturn(Optional.empty());

            mockMvc.perform(put(URL, QUIZ_ID, QUIZ_RUN_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toPayload(command))
            )
                    .andExpect(status().isOk());

            verify(quizRunRepository, times(1)).findByIdAndUser(QUIZ_RUN_ID, USER_1);
            verify(quizRepository, times(1)).findById(QUIZ_ID);
            verifyNoMoreInteractions(quizRepository, quizRunRepository);
        }

        @Test
        @DisplayName("Should increase the score for all correctly answered records and return OK 200")
        void updateStatistics_allCorrect() throws Exception {
            QuizRunCommand command = getQuizRunCommand();
            QUIZ_1.setQuizRecords(ImmutableList.of(
                    QUIZ_RECORD_1,
                    QUIZ_RECORD_2,
                    QUIZ_RECORD_3,
                    QUIZ_RECORD_4
            ));

            when(quizRunRepository.findByIdAndUser(QUIZ_RUN_ID, USER_1)).thenReturn(Optional.of(new QuizRun()));
            when(quizRepository.findById(QUIZ_ID)).thenReturn(Optional.of(QUIZ_1));

            List<QuizRecord> persistedQuizRecords = new ArrayList<>();
            when(quizRecordRepository.saveAll(any(List.class))).then(invocation -> {
                Collection<QuizRecord> records = invocation.getArgument(0);
                persistedQuizRecords.addAll(records);
                return records;
            });

            mockMvc.perform(put(URL, QUIZ_ID, QUIZ_RUN_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toPayload(command))
            )
                    .andExpect(status().isOk());

            assertThat(persistedQuizRecords, hasSize(4));
            assertPersistedQuizRecords(persistedQuizRecords, ImmutableMap.of(
                    101L, new Object[]{11, 2, 0.13f},
                    102L, new Object[]{16, 9, 1.0f},
                    103L, new Object[]{11, 3, 0.033f},
                    104L, new Object[]{10, 1, 1.0f}
            ));

            verify(quizRunRepository, times(1)).findByIdAndUser(QUIZ_RUN_ID, USER_1);
            verify(quizRepository, times(1)).findById(QUIZ_ID);
            verify(quizRecordRepository, times(1)).saveAll(any(List.class));
            verifyNoMoreInteractions(quizRunRepository, quizRepository, quizRecordRepository);
        }

        @Test
        @DisplayName("Should decrease the score for all incorrectly answered records and return OK 200")
        void updateStatistics_allIncorrect() throws Exception {
            QuizRunCommand command = getQuizRunCommand(
                    ImmutableList.of(),
                    ImmutableMap.of(
                            QUIZ_RECORD_1.getId(), false,
                            QUIZ_RECORD_2.getId(), false,
                            QUIZ_RECORD_3.getId(), false
                    )
            );
            QUIZ_1.setQuizRecords(ImmutableList.of(
                    QUIZ_RECORD_1,
                    QUIZ_RECORD_2,
                    QUIZ_RECORD_3,
                    QUIZ_RECORD_4
            ));

            when(quizRunRepository.findByIdAndUser(QUIZ_RUN_ID, USER_1)).thenReturn(Optional.of(new QuizRun()));
            when(quizRepository.findById(QUIZ_ID)).thenReturn(Optional.of(QUIZ_1));

            List<QuizRecord> persistedQuizRecords = new ArrayList<>();
            when(quizRecordRepository.saveAll(any(List.class))).then(invocation -> {
                Collection<QuizRecord> records = invocation.getArgument(0);
                persistedQuizRecords.addAll(records);
                return records;
            });

            mockMvc.perform(put(URL, QUIZ_ID, QUIZ_RUN_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isOk());

            assertThat(persistedQuizRecords, hasSize(4));
            assertPersistedQuizRecords(persistedQuizRecords, ImmutableMap.of(
                    101L, new Object[]{11, 1, 0.067f},
                    102L, new Object[]{16, 8, 0.94f},
                    103L, new Object[]{11, 2, 0f},
                    104L, new Object[]{10, 1, 1.0f}
            ));

            verify(quizRunRepository, times(1)).findByIdAndUser(QUIZ_RUN_ID, USER_1);
            verify(quizRepository, times(1)).findById(QUIZ_ID);
            verify(quizRecordRepository, times(1)).saveAll(any(List.class));
            verifyNoMoreInteractions(quizRunRepository, quizRecordRepository, quizRepository);
        }

        private void assertPersistedQuizRecords(List<QuizRecord> persistedQuizRecords, ImmutableMap<Long, Object[]> expectedAll) {
            for (QuizRecord quizRecord : persistedQuizRecords)
                assertPersistedQuizRecord(expectedAll, quizRecord);
        }

        private void assertPersistedQuizRecord(ImmutableMap<Long, Object[]> expectedAll, QuizRecord quizRecord) {
            var expected = expectedAll.get(quizRecord.getId());
            assertThat(quizRecord.getNumberOfRuns(), is(expected[0]));
            assertThat(quizRecord.getNumberOfSuccesses(), is(expected[1]));
            assertEquals((float)expected[2], quizRecord.getCurrentScore(), 0.04);
            assertTrue((float)expected[2] <= 1.0f);
            assertTrue((float)expected[2] >= 0.0f);
        }

        private QuizRunCommand getQuizRunCommand() {
            return getQuizRunCommand(
                    ImmutableList.of(),
                    ImmutableMap.of(
                            QUIZ_RECORD_1.getId(), true,
                            QUIZ_RECORD_2.getId(), true,
                            QUIZ_RECORD_3.getId(), true
                    ));
        }

        private QuizRunCommand getQuizRunCommand(List<Long> records) {
            return getQuizRunCommand(records, ImmutableMap.of());
        }

        private QuizRunCommand getQuizRunCommand(Map<Long, Boolean> doneRecordsAsMap) {
            return getQuizRunCommand(ImmutableList.of(), doneRecordsAsMap);
        }

        private QuizRunCommand getQuizRunCommand(
                List<Long> records,
                Map<Long, Boolean> doneRecordsAsMap
        ) {
            var doneRecords = doneRecordsAsMap.entrySet()
                    .stream()
                    .map(entry -> new QuizRunCommand.DoneRecord(entry.getKey(), entry.getValue()))
                    .collect(ImmutableList.toImmutableList());
            return QuizRunCommand.builder()
                    .id(QUIZ_RUN_ID)
                    .matchingRegime("l")
                    .quizRegime("f")
                    .doneRecords(doneRecords)
                    .records(records)
                    .build();
        }
    }

    @Nested
    @DisplayName("Test GET /api/quizzes/{id}/runs")
    class GetQuizRuns {
        private final String URL = BASE_URL_PATTERN;
        private final Long QUIZ_ID = QUIZ_1.getId();

        @Test
        @DisplayName("Should return empty list if the quiz does not belong to the user")
        void quizRunsNotFoundForQuizAndUser_ReturnEmptyList() throws Exception {
            when(quizRunRepository.findAllByQuizAndUser(QUIZ_ID, USER_1))
                    .thenReturn(ImmutableList.of());

            mockMvc.perform(get(URL, QUIZ_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$", is(empty())));

            verify(quizRunRepository, only()).findAllByQuizAndUser(QUIZ_ID, USER_1);
        }

        @Test
        @DisplayName("Should return found quiz runs")
        void quizRunsFound() throws Exception {
            Long quizRunId1 = 42L;
            Long quizRunId2 = 43L;
            QuizRun quizRun1 = QuizRun.builder()
                    .id(quizRunId1)
                    .quiz(QUIZ_1)
                    .quizRegime(QuizRegime.FORWARD)
                    .matchingRegime(MatchingRegime.LOOSENED)
                    .build();
            QuizRun quizRun2 = QuizRun.builder()
                    .id(quizRunId2)
                    .quiz(QUIZ_1)
                    .quizRegime(QuizRegime.FORWARD)
                    .matchingRegime(MatchingRegime.LOOSENED)
                    .build();

            when(quizRunRepository.findAllByQuizAndUser(QUIZ_ID, USER_1))
                    .thenReturn(ImmutableList.of(quizRun1, quizRun2));

            mockMvc.perform(get(URL, QUIZ_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(quizRunId1.intValue())))
                    .andExpect(jsonPath("$[1].id", is(quizRunId2.intValue())));

            verify(quizRunRepository, only()).findAllByQuizAndUser(QUIZ_ID, USER_1);
        }
    }
}
