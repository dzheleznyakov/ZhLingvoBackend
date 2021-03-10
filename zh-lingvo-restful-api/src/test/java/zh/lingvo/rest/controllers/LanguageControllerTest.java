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
import zh.lingvo.data.model.Language;
import zh.lingvo.data.services.LanguageService;
import zh.lingvo.rest.converters.LanguageToLanguageCommand;

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
@DisplayName("Test LanguageController")
class LanguageControllerTest {
    private static final String URL = "/api/languages";

    @Mock
    private LanguageService languageService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LanguageController controller = new LanguageController(languageService, new LanguageToLanguageCommand());

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionsAdvice())
                .build();
    }

    @Nested
    @DisplayName("Test GET /api/languages")
    class GetAllLanguages {
        @Test
        @DisplayName("Should return OK 200 and empty list if no languages are found")
        void nothingFound_ReturnEmptyList() throws Exception {
            when(languageService.findAll()).thenReturn(ImmutableList.of());

            mockMvc.perform(get(URL))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(languageService, only()).findAll();
        }

        @Test
        @DisplayName("Should return OK 200 and the list of languages if the languages are found")
        void languagesFound_ReturnAll() throws Exception {
            Language lang1 = Language.builder().id(1).name("Lang1").twoLetterCode("L1").build();
            Language lang2 = Language.builder().id(2).name("Lang2").twoLetterCode("L2").build();
            when(languageService.findAll()).thenReturn(ImmutableList.of(lang1, lang2));

            mockMvc.perform(get(URL))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].name", is("Lang1")))
                    .andExpect(jsonPath("$[0].code", is("L1")))
                    .andExpect(jsonPath("$[1].id", is(2)))
                    .andExpect(jsonPath("$[1].name", is("Lang2")))
                    .andExpect(jsonPath("$[1].code", is("L2")));

            verify(languageService, only()).findAll();
        }
    }
}