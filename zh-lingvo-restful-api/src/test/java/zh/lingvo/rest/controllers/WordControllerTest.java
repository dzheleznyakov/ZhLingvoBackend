package zh.lingvo.rest.controllers;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.Word;
import zh.lingvo.rest.util.RequestContext;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test WordController")
class WordControllerTest {
    private static final String URL = "api/words/";

    private final static User USER = User.builder().id(1L).name("test").build();
    private static final Language LANG_1 = Language.builder().id(1).name("Language 1").twoLetterCode("L1").build();
    private static final Dictionary DICTIONARY_1 = Dictionary.builder().id(1L).name("D1").language(LANG_1).build();
    private static final Dictionary DICTIONARY_2 = Dictionary.builder().id(2L).name("D2").language(LANG_1).build();
    private static final Word WORD_1 = Word.builder().id(1L).dictionary(DICTIONARY_1).mainForm("word1").build();
    private static final Word WORD_2 = Word.builder().id(2L).dictionary(DICTIONARY_1).mainForm("word1").build();
    static {
        DICTIONARY_1.setWords(ImmutableList.of(WORD_1, WORD_2));
    }

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RequestContext context = new RequestContext();
        context.setUser(USER);

        WordController wordController = new WordController(context);

        mockMvc = MockMvcBuilders.standaloneSetup(wordController)
                .setControllerAdvice(new ExceptionsAdvice())
                .build();
    }

    @Nested
    @DisplayName("Test GET /api/words/dictionary/{dictionaryId}")
    class GetAllWords {

    }
}