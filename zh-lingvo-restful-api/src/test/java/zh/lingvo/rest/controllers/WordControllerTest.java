package zh.lingvo.rest.controllers;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zh.lingvo.data.exceptions.FailedToPersist;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.SemanticBlock;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.Word;
import zh.lingvo.data.services.WordService;
import zh.lingvo.rest.commands.SemanticBlockCommand;
import zh.lingvo.rest.commands.WordCommand;
import zh.lingvo.rest.commands.WordOverviewCommand;
import zh.lingvo.rest.converters.WordConverter;
import zh.lingvo.rest.util.RequestContext;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesRegex;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test WordController")
class WordControllerTest {
    private static final Gson GSON = new Gson();
    private static final long DICTIONARY_ID = 101L;

    private final static User USER = User.builder().id(1L).name("test").build();
    private static final Language LANG_1 = Language.builder().id(1).name("Language 1").twoLetterCode("L1").build();
    private static final Dictionary DICTIONARY_1 = Dictionary.builder().id(DICTIONARY_ID).name("D1").language(LANG_1).build();
    private static final Word WORD_1 = Word.builder().id(1L).dictionary(DICTIONARY_1).mainForm("word1").build();
    private static final Word WORD_2 = Word.builder().id(2L).dictionary(DICTIONARY_1).mainForm("word2").build();

    static {
        DICTIONARY_1.setWords(ImmutableList.of(WORD_1, WORD_2));
    }

    @Mock
    private WordService wordService;

    @Mock
    private WordConverter wordConverter;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RequestContext context = new RequestContext();
        context.setUser(USER);

        WordController wordController = new WordController(
                wordService, wordConverter, context);

        mockMvc = MockMvcBuilders.standaloneSetup(wordController)
                .setControllerAdvice(new ExceptionsAdvice())
                .build();

        lenient().when(wordConverter.toWordOverviewCommand(any(Word.class))).thenAnswer(invocation -> {
            Word word = invocation.getArgument(0, Word.class);
            return WordOverviewCommand.builder()
                    .id(word.getId())
                    .mainForm(word.getMainForm())
                    .build();
        });
        lenient().when(wordConverter.toWordCommand(any(Word.class))).thenAnswer(invocation -> {
            Word word = invocation.getArgument(0, Word.class);
            List<SemanticBlockCommand> sbCommands = null;
            if (word.getSemanticBlocks() != null)
                sbCommands = word.getSemanticBlocks().stream()
                        .map(sb -> SemanticBlockCommand.builder().id(sb.getId()).build())
                        .collect(ImmutableList.toImmutableList());
            return WordCommand.builder()
                    .id(word.getId())
                    .transcription(word.getTranscription())
                    .mainForm(word.getMainForm())
                    .typeOfIrregularity(word.getTypeOfIrregularity())
                    .semBlocks(sbCommands)
                    .build();
        });
    }

    @Nested
    @DisplayName("Test GET /api/words/dictionary/{dictionaryId}")
    class GetAllWords {
        private static final String URL_TEMPLATE = "/api/words/dictionary/{dictionaryId}";

        @Test
        @DisplayName("Should return an empty list if no words found for the dictionary")
        void noWordsFound() throws Exception {
            when(wordService.findAll(DICTIONARY_ID, USER)).thenReturn(ImmutableList.of());

            mockMvc.perform(get(URL_TEMPLATE, DICTIONARY_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(wordService, only()).findAll(DICTIONARY_ID, USER);
        }

        @Test
        @DisplayName("Should return the overview of the words from the dictionary")
        void wordsFound() throws Exception {
            when(wordService.findAll(DICTIONARY_ID, USER)).thenReturn(ImmutableList.of(WORD_1, WORD_2));

            mockMvc.perform(get(URL_TEMPLATE, DICTIONARY_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0]", is("word1")))
                    .andExpect(jsonPath("$[1]", is("word2")));

            verify(wordService, only()).findAll(DICTIONARY_ID, USER);
        }
    }

    @Nested
    @DisplayName("Test GET /api/words/{wordId}")
    class GetWord {
        private static final String URL_PATTERN = "/api/words/{wordId}";

        @Test
        @DisplayName("Should return 404 NOT FOUND if the word is not found")
        void wordNotFound() throws Exception {
            long wordId = 42L;
            when(wordService.findWithSubWordPartsById(wordId, USER)).thenReturn(Optional.empty());

            mockMvc.perform(get(URL_PATTERN, wordId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*not found.*")));

            verify(wordService, only()).findWithSubWordPartsById(wordId, USER);
        }

        @Test
        @DisplayName("Should return 200 OK with word data if the word is found")
        void wordFound() throws Exception {
            SemanticBlock sb1 = SemanticBlock.builder().id(201L).build();
            SemanticBlock sb2 = SemanticBlock.builder().id(202L).build();
            long wordId = 42L;
            Word word = Word.builder()
                    .id(wordId)
                    .mainForm("test")
                    .transcription("tæst")
                    .typeOfIrregularity("irreg")
                    .semanticBlocks(ImmutableList.of(sb1, sb2))
                    .build();

            when(wordService.findWithSubWordPartsById(wordId, USER)).thenReturn(Optional.of(word));

            mockMvc.perform(get(URL_PATTERN, wordId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.id", is((int) wordId)))
                    .andExpect(jsonPath("$.mainForm", is(word.getMainForm())))
                    .andExpect(jsonPath("$.transcription", is(word.getTranscription())))
                    .andExpect(jsonPath("$.typeOfIrregularity", is(word.getTypeOfIrregularity())))
                    .andExpect(jsonPath("$.semBlocks", is(notNullValue())))
                    .andExpect(jsonPath("$.semBlocks", hasSize(2)))
                    .andExpect(jsonPath("$.semBlocks[0].id", is(sb1.getId().intValue())))
                    .andExpect(jsonPath("$.semBlocks[1].id", is(sb2.getId().intValue())));

            verify(wordService, only()).findWithSubWordPartsById(wordId, USER);
        }
    }

    @Nested
    @DisplayName("Test POST /api/words/dictionary/{dictionaryId}")
    class CreateWord {
        private static final String URL_PATTERN = "/api/words/dictionary/{dictionaryId}";

        @Test
        @DisplayName("Should return 409 CONFLICT if the word already exists")
        void wordHasId() throws Exception {
            WordCommand command = WordCommand.builder().id(42L).build();

            mockMvc.perform(post(URL_PATTERN, DICTIONARY_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toPayload(command))
            )
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*already exists.*")));
        }

        @Test
        @DisplayName("Should return created word")
        void createWord() throws Exception {
            Word convertedWord = new Word();
            long wordId = 42L;
            Word savedWord = Word.builder().id(wordId).build();
            when(wordConverter.toWord(any(WordCommand.class)))
                    .thenReturn(convertedWord);
            when(wordService.create(convertedWord, DICTIONARY_ID, USER))
                    .thenReturn(Optional.of(savedWord));

            WordCommand command = new WordCommand();

            mockMvc.perform(post(URL_PATTERN, DICTIONARY_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toPayload(command))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.id", is((int) wordId)));

            verify(wordService, only()).create(convertedWord, DICTIONARY_ID, USER);
        }

        @Test
        @DisplayName("Should return an empty word if creating a new word fails")
        void creationFails() throws Exception {
            Word convertedWord = new Word();
            when(wordConverter.toWord(any(WordCommand.class)))
                    .thenReturn(convertedWord);
            when(wordService.create(convertedWord, DICTIONARY_ID, USER))
                    .thenReturn(Optional.empty());

            WordCommand command = WordCommand.builder().mainForm("word").build();

            mockMvc.perform(post(URL_PATTERN, DICTIONARY_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toPayload(command))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.id", is((nullValue()))));

            verify(wordService, only()).create(convertedWord, DICTIONARY_ID, USER);
        }
    }

    @Nested
    @DisplayName("Test PUT /api/words/{wordId}")
    class UpdateWord {
        private final static String URL_PATTERN = "/api/words/{wordId}";

        @Test
        @DisplayName("Should return 404 NOT FOUND if the word does not exist")
        void wordDoesNotExist() throws Exception {
            long wordId = 42L;

            when(wordConverter.toWord(any(WordCommand.class)))
                    .thenReturn(Word.builder().id(wordId).build());
            when(wordService.update(any(Word.class), eq(USER)))
                    .thenThrow(new FailedToPersist("Something went terrible wrong"));

            WordCommand command = WordCommand.builder().id(wordId).build();

            mockMvc.perform(put(URL_PATTERN, wordId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toPayload(command))
            )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*not found.*")));
        }

        @Test
        @DisplayName("Should return 200 OK and the updated word if persisting is successful")
        void happyPath() throws Exception {
            long wordId = 42L;
            String mainForm = "updated";

            when(wordConverter.toWord(any(WordCommand.class)))
                    .thenReturn(Word.builder().id(wordId).mainForm(mainForm).build());
            when(wordService.update(any(Word.class), eq(USER)))
                    .thenReturn(Word.builder().id(wordId).mainForm(mainForm).build());

            WordCommand command = WordCommand.builder().id(wordId).mainForm(mainForm).build();

            mockMvc.perform(put(URL_PATTERN, wordId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toPayload(command))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.id", is((int) wordId)))
                    .andExpect(jsonPath("$.mainForm", is(mainForm)));
        }

        @Test
        @DisplayName("Should updated the right word even if the payload has the wrong id")
        void fixingId() throws Exception {
            long wordId = 42L;
            WordCommand command = WordCommand.builder().id(wordId + 10).build();

            mockMvc.perform(put(URL_PATTERN, wordId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toPayload(command)));

            ArgumentCaptor<WordCommand> commandCaptor = ArgumentCaptor.forClass(WordCommand.class);
            verify(wordConverter, times(1)).toWord(commandCaptor.capture());
            WordCommand capturedCommand = commandCaptor.getValue();
            assertThat(capturedCommand.getId(), is(wordId));
        }
    }

    @Nested
    @DisplayName("Test DELETE /api/words/{wordId}")
    class DeleteWord {
        private static final String URL_PATTERN = "/api/words/{wordId}";

        @Test
        @DisplayName("Should return 200 OK when the word is deleted")
        void deleteWord() throws Exception {
            long wordId = 42L;

            mockMvc.perform(delete(URL_PATTERN, wordId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is((int) wordId)));

            ArgumentCaptor<Word> wordCaptor = ArgumentCaptor.forClass(Word.class);
            verify(wordService, only()).delete(wordCaptor.capture(), eq(USER));
            Word capturedWord = wordCaptor.getValue();
            assertThat(capturedWord.getId(), is(wordId));
        }
    }

    private String toPayload(WordCommand command) {
        return GSON.toJson(command);
    }
}