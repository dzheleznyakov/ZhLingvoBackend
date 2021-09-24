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
import zh.lingvo.core.LanguageDescriptor;
import zh.lingvo.core.LanguageDescriptorManager;
import zh.lingvo.core.domain.PartOfSpeech;
import zh.lingvo.data.services.PosService;
import zh.lingvo.rest.converters.PosToPosCommand;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test PosController")
class PosControllerTest {
    private static final String LANG_CODE = "Lc";

    @Mock
    private PosService posService;

    @Mock
    private LanguageDescriptorManager languageDescriptorManager;

    @Mock
    private LanguageDescriptor languageDescriptor;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        PosToPosCommand posConverter = new PosToPosCommand(languageDescriptorManager);
        PosController posController = new PosController(posService, posConverter);
        mockMvc = MockMvcBuilders.standaloneSetup(posController)
                .setControllerAdvice(new ExceptionsAdvice())
                .build();

    }

    @Nested
    @DisplayName("Test GET /api/pos/{langCode}")
    class GetAllPos {
        private static final String URL_TEMPLATE = "/api/pos/{langCode}";

        @Test
        @DisplayName("Should return an empty list if not PoS found for a language")
        void noPosFoundForLanguage() throws Exception {
            when(posService.findAll(LANG_CODE)).thenReturn(ImmutableList.of());

            mockMvc.perform(get(URL_TEMPLATE, LANG_CODE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$", is(empty())));

            verify(posService, only()).findAll(LANG_CODE);
        }

        @Test
        @DisplayName("Should return a list of PoS commands for a langauage")
        void posFoundForLanguage() throws Exception {
            when(posService.findAll(LANG_CODE)).thenReturn(ImmutableList.of(
                    PartOfSpeech.NOUN,
                    PartOfSpeech.PRONOUN,
                    PartOfSpeech.ADJECTIVE));
            when(languageDescriptorManager.get(LANG_CODE)).thenReturn(languageDescriptor);
            when(languageDescriptor.getPartOfSpeechNativeName(PartOfSpeech.NOUN)).thenReturn("LcNoun");
            when(languageDescriptor.getPartOfSpeechNativeShortName(PartOfSpeech.NOUN)).thenReturn("lsN");
            when(languageDescriptor.getPartOfSpeechNativeName(PartOfSpeech.PRONOUN)).thenReturn("LcPronoun");
            when(languageDescriptor.getPartOfSpeechNativeShortName(PartOfSpeech.PRONOUN)).thenReturn("lsP");
            when(languageDescriptor.getPartOfSpeechNativeName(PartOfSpeech.ADJECTIVE)).thenReturn("LcAdjective");
            when(languageDescriptor.getPartOfSpeechNativeShortName(PartOfSpeech.ADJECTIVE)).thenReturn("lsA");

            mockMvc.perform(get(URL_TEMPLATE, LANG_CODE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$", is(hasSize(3))))
                    .andExpect(jsonPath("$[0].name", is("NOUN")))
                    .andExpect(jsonPath("$[0].defaultShortName", is("n")))
                    .andExpect(jsonPath("$[0].nativeName", is("LcNoun")))
                    .andExpect(jsonPath("$[0].nativeShortName", is("lsN")))
                    .andExpect(jsonPath("$[1].name", is("PRONOUN")))
                    .andExpect(jsonPath("$[1].defaultShortName", is("pron")))
                    .andExpect(jsonPath("$[1].nativeName", is("LcPronoun")))
                    .andExpect(jsonPath("$[1].nativeShortName", is("lsP")))
                    .andExpect(jsonPath("$[2].name", is("ADJECTIVE")))
                    .andExpect(jsonPath("$[2].defaultShortName", is("adj")))
                    .andExpect(jsonPath("$[2].nativeName", is("LcAdjective")))
                    .andExpect(jsonPath("$[2].nativeShortName", is("lsA")));
        }
    }
}