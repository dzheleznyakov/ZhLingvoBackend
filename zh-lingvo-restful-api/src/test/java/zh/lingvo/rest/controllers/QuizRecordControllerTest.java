package zh.lingvo.rest.controllers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
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
import zh.lingvo.data.model.Example;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.Meaning;
import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.QuizRecord;
import zh.lingvo.data.model.SemanticBlock;
import zh.lingvo.data.model.Translation;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.Word;
import zh.lingvo.data.services.MeaningService;
import zh.lingvo.data.services.QuizRecordService;
import zh.lingvo.data.services.QuizService;
import zh.lingvo.rest.commands.QuizRecordCommand;
import zh.lingvo.rest.converters.ExampleCommandToQuizExample;
import zh.lingvo.rest.converters.QuizExampleToExampleCommand;
import zh.lingvo.rest.converters.QuizRecordCommandToQuizRecord;
import zh.lingvo.rest.converters.QuizRecordConverter;
import zh.lingvo.rest.converters.QuizRecordToQuizRecordCommand;
import zh.lingvo.rest.converters.QuizRecordToQuizRecordOverviewCommand;
import zh.lingvo.rest.converters.QuizTranslationToTranslationCommand;
import zh.lingvo.rest.converters.TranslationCommandToQuizTranslation;
import zh.lingvo.rest.util.RequestContext;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesRegex;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.only;
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
@DisplayName("Test QuizRecordController")
class QuizRecordControllerTest {
    private static final Gson GSON = new Gson();
    private static final String URL_TEMPLATE = "/api/quizzes/{id}/records";
    private static final Long QUIZ_ID = 1L;
    private static final Long QUIZ_RECORD_ID = 42L;

    private final User user = User.builder().id(1L).name("Test").build();
    private final Language language = Language.builder().id(1).name("Language").twoLetterCode("L1").build();
    private final Quiz quiz = Quiz.builder().id(QUIZ_ID).name("q1").user(user).language(language).build();

    @Mock
    private QuizRecordService quizRecordService;

    @Mock
    private QuizService quizService;

    @Mock
    private MeaningService meaningService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RequestContext context = new RequestContext();
        context.setUser(user);

        QuizRecordToQuizRecordCommand quizToCommandConverter = new QuizRecordToQuizRecordCommand(
                new QuizTranslationToTranslationCommand(),
                new QuizExampleToExampleCommand());
        QuizRecordToQuizRecordOverviewCommand quizToOverviewConverter = new QuizRecordToQuizRecordOverviewCommand(
                new QuizTranslationToTranslationCommand());
        QuizRecordCommandToQuizRecord commandConverter = new QuizRecordCommandToQuizRecord(
                new TranslationCommandToQuizTranslation(),
                new ExampleCommandToQuizExample());
        QuizRecordConverter quizRecordConverter = new QuizRecordConverter(
                quizToOverviewConverter,
                quizToCommandConverter,
                commandConverter);

        QuizRecordController controller = new QuizRecordController(
                quizRecordService,
                quizService,
                meaningService,
                quizRecordConverter,
                context);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionsAdvice())
                .build();
    }

    private static String toPayload(Object object) {
        return GSON.toJson(object);
    }

    @Nested
    @DisplayName("Test GET /api/quizzes/{id}/records/overviews")
    class GetAllRecordOverviews {
        private final String GET_OVERVIEWS_URL_TEMPLATE = URL_TEMPLATE + "/overviews";

        @Test
        @DisplayName("Should return an empty list if no records are found for the quiz and user")
        void recordsNotFound() throws Exception {
            when(quizRecordService.findAll(QUIZ_ID, user)).thenReturn(ImmutableList.of());

            mockMvc.perform(get(GET_OVERVIEWS_URL_TEMPLATE, QUIZ_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$", is(empty())));

            verify(quizRecordService, only()).findAll(QUIZ_ID, user);
        }

        @Test
        @DisplayName("Should return the overviews of all found quiz records")
        void quizDoesNotExistForUser() throws Exception {
            Long recordId1 = 101L;
            Long recordId2 = 102L;
            QuizRecord record1 = QuizRecord.builder().id(recordId1).build();
            QuizRecord record2 = QuizRecord.builder().id(recordId2).build();
            when(quizRecordService.findAll(QUIZ_ID, user))
                    .thenReturn(ImmutableList.of(record1, record2));

            mockMvc.perform(get(GET_OVERVIEWS_URL_TEMPLATE, QUIZ_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(recordId1.intValue())))
                    .andExpect(jsonPath("$[1].id", is(recordId2.intValue())));

            verify(quizRecordService, only()).findAll(QUIZ_ID, user);
        }
    }

    @Nested
    @DisplayName("Test GET /api/quizzes/{id}/records/{rid}")
    class GetQuizRecord {
        private final String GET_URL_TEMPLATE = URL_TEMPLATE + "/{rid}";

        @Test
        @DisplayName("Should return NOT FOUND 404 if the quiz record not found for the user")
        void getQuizRecord_QuizNotFound() throws Exception {
            when(quizRecordService.findById(QUIZ_RECORD_ID, user)).thenReturn(Optional.empty());

            mockMvc.perform(get(GET_URL_TEMPLATE, QUIZ_ID, QUIZ_RECORD_ID))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*not found.*")));

            verify(quizRecordService, only()).findById(QUIZ_RECORD_ID, user);
        }

        @Test
        @DisplayName("Should return OK 200 and the quiz record command if the record is found for the user")
        void getQuizRecord_RecordFound() throws Exception {
            String transcription = "fɔːm";
            String wordMainForm = "form";
            Integer numberOfRuns = 10;
            Integer numberOfSuccesses = 5;
            QuizRecord quizRecord = QuizRecord.builder()
                    .id(QUIZ_RECORD_ID)
                    .wordMainForm(wordMainForm)
                    .quiz(quiz)
                    .pos(PartOfSpeech.NOUN)
                    .transcription(transcription)
                    .numberOfRuns(numberOfRuns)
                    .numberOfSuccesses(numberOfSuccesses)
                    .build();
            when(quizRecordService.findById(QUIZ_RECORD_ID, user)).thenReturn(Optional.of(quizRecord));

            mockMvc.perform(get(GET_URL_TEMPLATE, QUIZ_ID, QUIZ_RECORD_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.id", is(QUIZ_RECORD_ID.intValue())))
                    .andExpect(jsonPath("$.wordMainForm", is(wordMainForm)))
                    .andExpect(jsonPath("$.pos", is(PartOfSpeech.NOUN.getShortName())))
                    .andExpect(jsonPath("$.transcription", is(transcription)))
                    .andExpect(jsonPath("$.numberOfRuns", is(numberOfRuns)))
                    .andExpect(jsonPath("$.numberOfSuccesses", is(numberOfSuccesses)));

            verify(quizRecordService, only()).findById(QUIZ_RECORD_ID, user);
        }
    }

    @Nested
    @DisplayName("Test POST /api/quizzes/{id}/records")
    class CreateQuizRecord {
        private final String POST_URL_TEMPLATE = URL_TEMPLATE;

        @Test
        @DisplayName("Should return BAD REQUEST 400 if the quiz record contains an id")
        void createNewQuizRecord_RecordExists() throws Exception {
            QuizRecordCommand command = QuizRecordCommand.builder().id(QUIZ_RECORD_ID).build();

            mockMvc.perform(post(POST_URL_TEMPLATE, QUIZ_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*should not contain id.*")));

            verifyNoInteractions(quizRecordService);
        }

        @Test
        @DisplayName("Should return OK 200 if the quiz record has no id")
        void createNewQuizRecord_HappyPath() throws Exception {
            String wordMainForm = "main";
            String transcription = "meɪn";
            Integer numberOfRuns = 10;
            Integer numberOfSuccesses = 5;
            String strPos = "adj";
            QuizRecordCommand command = QuizRecordCommand.builder()
                    .wordMainForm(wordMainForm)
                    .pos(strPos)
                    .transcription(transcription)
                    .numberOfRuns(numberOfRuns)
                    .numberOfSuccesses(numberOfSuccesses)
                    .build();
            QuizRecord savedRecord = QuizRecord.builder()
                    .id(QUIZ_RECORD_ID)
                    .wordMainForm(wordMainForm)
                    .pos(PartOfSpeech.ADJECTIVE)
                    .transcription(transcription)
                    .numberOfRuns(numberOfRuns)
                    .numberOfSuccesses(numberOfSuccesses)
                    .build();

            when(quizRecordService.create(any(QuizRecord.class), eq(QUIZ_ID), eq(user)))
                    .thenReturn(Optional.of(savedRecord));

            mockMvc.perform(post(POST_URL_TEMPLATE, QUIZ_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.id", is(QUIZ_RECORD_ID.intValue())))
                    .andExpect(jsonPath("$.wordMainForm", is(wordMainForm)))
                    .andExpect(jsonPath("$.pos", is(strPos)))
                    .andExpect(jsonPath("$.transcription", is(transcription)))
                    .andExpect(jsonPath("$.numberOfRuns", is(numberOfRuns)))
                    .andExpect(jsonPath("$.numberOfSuccesses", is(numberOfSuccesses)));

            verify(quizRecordService, only())
                    .create(any(QuizRecord.class), eq(QUIZ_ID), eq(user));
            verifyNoMoreInteractions(quizRecordService);
        }

        @Test
        @DisplayName("Should return NOT FOUND 404 if the quiz is not found for the user")
        void createNewQuizRecord_NoQuiz() throws Exception {
            QuizRecordCommand command = QuizRecordCommand.builder().build();

            when(quizRecordService.create(any(QuizRecord.class), eq(QUIZ_ID), eq(user)))
                    .thenReturn(Optional.empty());

            mockMvc.perform(post(POST_URL_TEMPLATE, QUIZ_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*not found.*")));

            verify(quizRecordService, only())
                    .create(any(QuizRecord.class), eq(QUIZ_ID), eq(user));
            verifyNoMoreInteractions(quizRecordService);
        }
    }

    @Nested
    @DisplayName("Test POST /api/quizzes/{id}/records/meaning/{mid}")
    class CreateQuizRecordFromMeaning {
        private final String POST_URL_TEMPLATE = URL_TEMPLATE + "/meaning/{mid}";
        private final Long MEANING_ID = 100L;

        @Test
        @DisplayName("Should return 404 NOT FOUND if the meaning does not exist for the user")
        void meaningNotFound_Throw() throws Exception {
            when(meaningService.findById(MEANING_ID, user)).thenReturn(Optional.empty());

            mockMvc.perform(post(POST_URL_TEMPLATE, QUIZ_ID, MEANING_ID))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*not found.*")));

            verify(meaningService, only()).findById(MEANING_ID, user);
            verifyNoInteractions(quizRecordService, quizService);
        }

        @Test
        @DisplayName("Should return 404 NOT FOUND if the quiz does not exist for the user")
        void quizNotFound_Throw() throws Exception {
            when(meaningService.findById(MEANING_ID, user)).thenReturn(Optional.of(new Meaning()));
            when(quizService.findById(QUIZ_ID, user)).thenReturn(Optional.empty());

            mockMvc.perform(post(POST_URL_TEMPLATE, QUIZ_ID, MEANING_ID))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*not found.*")));

            verify(meaningService, only()).findById(MEANING_ID, user);
            verify(quizService, only()).findById(QUIZ_ID, user);
            verifyNoInteractions(quizRecordService);
        }

        @Test
        @DisplayName("Should return OK 200 and new quiz record when the payload is full")
        void createNewQuizRecordFromMeaning_HappyPath() throws Exception {
            String wordMainForm = "form";
            String transcription = "fɔːm";
            PartOfSpeech pos = PartOfSpeech.NOUN;
            Word word = Word.builder()
                    .mainForm(wordMainForm)
                    .transcription(transcription)
                    .build();
            SemanticBlock sb = SemanticBlock.builder()
                    .word(word)
                    .pos(pos)
                    .build();
            Translation translation = Translation.builder()
                    .value("tr_value")
                    .elaboration("tr_elab")
                    .build();
            Example example = Example.builder()
                    .remark("ex_rem")
                    .expression("ex_expr")
                    .explanation("ex_expl")
                    .build();
            Meaning meaning = Meaning.builder()
                    .id(MEANING_ID)
                    .semBlock(sb)
                    .translations(ImmutableSet.of(translation))
                    .examples(ImmutableSet.of(example))
                    .build();

            when(meaningService.findById(MEANING_ID, user)).thenReturn(Optional.of(meaning));
            when(quizService.findById(QUIZ_ID, user)).thenReturn(Optional.of(quiz));
            when(quizRecordService.create(any(QuizRecord.class), eq(QUIZ_ID), eq(user)))
                    .thenAnswer(invocation -> {
                        QuizRecord record = invocation.getArgument(0);
                        assertThat(record.getQuiz(), is(notNullValue()));
                        return Optional.of(record);
                    });

            mockMvc.perform(post(POST_URL_TEMPLATE, QUIZ_ID, MEANING_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.wordMainForm", is(wordMainForm)))
                    .andExpect(jsonPath("$.transcription", is(transcription)))
                    .andExpect(jsonPath("$.pos", is(pos.getShortName())))
                    .andExpect(jsonPath("$.currentScore", is(0.0)))
                    .andExpect(jsonPath("$.numberOfRuns", is(0)))
                    .andExpect(jsonPath("$.numberOfSuccesses", is(0)))
                    .andExpect(jsonPath("$.translations", is(notNullValue())))
                    .andExpect(jsonPath("$.translations", hasSize(1)))
                    .andExpect(jsonPath("$.translations[0].value", is("tr_value")))
                    .andExpect(jsonPath("$.translations[0].elaboration", is("tr_elab")))
                    .andExpect(jsonPath("$.examples", is(notNullValue())))
                    .andExpect(jsonPath("$.examples", hasSize(1)))
                    .andExpect(jsonPath("$.examples[0].remark", is("ex_rem")))
                    .andExpect(jsonPath("$.examples[0].expression", is("ex_expr")))
                    .andExpect(jsonPath("$.examples[0].explanation", is("ex_expl")));

            verify(meaningService, only()).findById(MEANING_ID, user);
            verify(quizService, only()).findById(QUIZ_ID, user);
            verify(quizRecordService, only()).create(any(QuizRecord.class), eq(QUIZ_ID), eq(user));
            verifyNoMoreInteractions(meaningService, quizService, quizRecordService);
        }
    }

    @Nested
    @DisplayName("Test PUT /api/quizzes/{id}/records")
    class UpdateQuizRecord {
        private final String PUT_URL_TEMPLATE = URL_TEMPLATE;

        @Test
        @DisplayName("Should return BAD REQUEST 400 if the quiz record does not contain id")
        void updateQuizRecord_NoId() throws Exception {
            QuizRecordCommand command = QuizRecordCommand.builder().wordMainForm("form").build();

            mockMvc.perform(put(PUT_URL_TEMPLATE, QUIZ_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*no id found.*")));

            verifyNoInteractions(quizRecordService);
        }

        @Test
        @DisplayName("Should return BAD REQUEST 400 if the part of speech in the record is null")
        void updateQuizRecord_NoPos() throws Exception {
            QuizRecordCommand command = QuizRecordCommand.builder()
                    .wordMainForm("form")
                    .id(QUIZ_RECORD_ID)
                    .build();

            mockMvc.perform(put(PUT_URL_TEMPLATE, QUIZ_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*no part of speech found.*")));

            verifyNoInteractions(quizRecordService);
        }

        @Test
        @DisplayName("Should return NOT FOUND 404 if the record was not saved because the record, quiz and user do not match")
        void updateQuizRecord_NoSave() throws Exception {
            QuizRecordCommand command = QuizRecordCommand.builder()
                    .id(QUIZ_RECORD_ID)
                    .wordMainForm("form")
                    .pos("n")
                    .build();

            when(quizRecordService.update(any(QuizRecord.class), eq(QUIZ_ID), eq(user)))
                    .thenReturn(Optional.empty());

            mockMvc.perform(put(PUT_URL_TEMPLATE, QUIZ_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*not found.*")));

            verify(quizRecordService, only()).update(any(QuizRecord.class), eq(QUIZ_ID), eq(user));
        }

        @Test
        @DisplayName("Should save the record and return OK 200 if the record exists, matching the quiz and user")
        void updateQuizRecord_Success() throws Exception {
            String wordMainForm = "form";
            String pos = "n";
            QuizRecordCommand command = QuizRecordCommand.builder()
                    .id(QUIZ_RECORD_ID)
                    .wordMainForm(wordMainForm)
                    .pos(pos)
                    .build();

            when(quizRecordService.update(any(QuizRecord.class), eq(QUIZ_ID), eq(user)))
                    .thenAnswer(invocation -> {
                        QuizRecord recordToSave = invocation.getArgument(0);
                        return Optional.of(recordToSave);
                    });

            mockMvc.perform(put(PUT_URL_TEMPLATE, QUIZ_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toPayload(command))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.id", is((QUIZ_RECORD_ID.intValue()))))
                    .andExpect(jsonPath("$.wordMainForm", is(wordMainForm)))
                    .andExpect(jsonPath("$.pos", is(pos)));

            verify(quizRecordService, only())
                    .update(any(QuizRecord.class), eq(QUIZ_ID), eq(user));
        }
    }

    @Nested
    @DisplayName("Test DELETE /api/quizzes/{id}/records/{rid}")
    class DeleteQuizRecord {
        private final String DELETE_URL_TEMPLATE = URL_TEMPLATE + "/{rid}";

        @Test
        @DisplayName("Should return SERVICE UNAVAILABLE 503 if deletion fails")
        void deletionFails() throws Exception {
            when(quizRecordService.deleteById(QUIZ_RECORD_ID, QUIZ_ID, user))
                    .thenReturn(false);

            mockMvc.perform(delete(DELETE_URL_TEMPLATE, QUIZ_ID, QUIZ_RECORD_ID))
                    .andExpect(status().isServiceUnavailable())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*unavailable.*")));

            verify(quizRecordService, only()).deleteById(QUIZ_RECORD_ID, QUIZ_ID, user);
        }

        @Test
        @DisplayName("Should return OK 200 if the deletion is successful")
        void deletionSucceeds() throws Exception {
            when(quizRecordService.deleteById(QUIZ_RECORD_ID, QUIZ_ID, user))
                    .thenReturn(true);

            mockMvc.perform(delete(DELETE_URL_TEMPLATE, QUIZ_ID, QUIZ_RECORD_ID))
                    .andExpect(status().isOk());

            verify(quizRecordService, only()).deleteById(QUIZ_RECORD_ID, QUIZ_ID, user);
        }
    }
}