package zh.lingvo.rest.controllers;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.SemanticBlock;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.Word;
import zh.lingvo.data.services.WordService;
import zh.lingvo.rest.commands.SemanticBlockCommand;
import zh.lingvo.rest.commands.WordCommand;
import zh.lingvo.rest.commands.WordOverviewCommand;
import zh.lingvo.rest.converters.WordToWordCommand;
import zh.lingvo.rest.converters.WordToWordOverviewCommand;
import zh.lingvo.rest.util.RequestContext;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesRegex;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test WordController")
class WordControllerTest {
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
    private WordToWordOverviewCommand wordOverviewConverter;

    @Mock
    WordToWordCommand wordConverter;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RequestContext context = new RequestContext();
        context.setUser(USER);

        WordController wordController = new WordController(
                wordService, wordOverviewConverter, wordConverter, context);

        mockMvc = MockMvcBuilders.standaloneSetup(wordController)
                .setControllerAdvice(new ExceptionsAdvice())
                .build();

        lenient().when(wordOverviewConverter.convert(any(Word.class))).thenAnswer(invocation -> {
            Word word = invocation.getArgument(0, Word.class);
            return WordOverviewCommand.builder()
                    .id(word.getId())
                    .mainForm(word.getMainForm())
                    .build();
        });
        lenient().when(wordConverter.convert(any(Word.class))).thenAnswer(invocation -> {
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
        private final String URL_TEMPLATE = "/api/words/dictionary/{dictionaryId}";

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
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].mainForm", is("word1")))
                    .andExpect(jsonPath("$[1].id", is(2)))
                    .andExpect(jsonPath("$[1].mainForm", is("word2")));

            verify(wordService, only()).findAll(DICTIONARY_ID, USER);
        }
    }

    @Nested
    @DisplayName("Test GET /api/words/{wordId}")
    class GetWord {
        private final String URL_PATTERN = "/api/words/{wordId}";

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
}