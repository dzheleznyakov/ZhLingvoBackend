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
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.Language;
import zh.lingvo.data.model.User;
import zh.lingvo.data.services.DictionaryService;
import zh.lingvo.rest.commands.DictionaryCommand;
import zh.lingvo.rest.converters.DictionaryCommandToDictionary;
import zh.lingvo.rest.converters.DictionaryToDictionaryCommand;
import zh.lingvo.rest.converters.LanguageCommandToLanguage;
import zh.lingvo.rest.converters.LanguageToLanguageCommand;
import zh.lingvo.rest.util.RequestContext;

import java.util.Optional;

import static org.hamcrest.Matchers.empty;
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
@DisplayName("Test DictionaryController")
class DictionaryControllerTest {
    private static final Gson GSON = new Gson();

    private static final String URL = "/api/dictionaries";
    private static final String URL_DICTIONARY_TEMPLATE = URL + "/{id}";

    private static final User USER = User.builder().id(1L).name("Test").build();
    private static final Language LANG_1 = Language.builder().id(1).name("Language 1").twoLetterCode("L1").build();
    private static final Language LANG_2 = Language.builder().id(2).name("Language 2").twoLetterCode("L2").build();
    private static final Dictionary DICTIONARY_1 = Dictionary.builder().id(1L).name("D1").language(LANG_1).build();
    private static final Dictionary DICTIONARY_2 = Dictionary.builder().id(2L).name("D2").language(LANG_2).build();

    @Mock
    private DictionaryService dictionaryService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RequestContext context = new RequestContext();
        context.setUser(USER);

        LanguageToLanguageCommand languageConverter = new LanguageToLanguageCommand();
        LanguageCommandToLanguage languageCommandConverter = new LanguageCommandToLanguage();
        DictionaryToDictionaryCommand dictionaryConverter = new DictionaryToDictionaryCommand(languageConverter);
        DictionaryCommandToDictionary dictionaryCommandConverter = new DictionaryCommandToDictionary(languageCommandConverter);

        DictionaryController controller = new DictionaryController(
                dictionaryService,
                dictionaryConverter,
                dictionaryCommandConverter,
                context);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionsAdvice())
                .build();
    }

    private String toPayload(Object object) {
        return GSON.toJson(object);
    }

    @Nested
    @DisplayName("Test GET /api/dictionaries")
    class GetAllDictionaries {
        @Test
        @DisplayName("Should return empty list if no dictionaries are found for the user")
        void dictionariesNotFound_ReturnEmptyList() throws Exception {
            when(dictionaryService.findAll(USER)).thenReturn(ImmutableList.of());

            mockMvc.perform(get(URL))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$", is(empty())));

            verify(dictionaryService, only()).findAll(USER);
        }

        @Test
        @DisplayName("Should return list of dictionaries if they are found for the user")
        void returnFoundDictionaries() throws Exception {
            when(dictionaryService.findAll(USER)).thenReturn(ImmutableList.of(DICTIONARY_1, DICTIONARY_2));

            mockMvc.perform(get(URL))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].name", is("D1")))
                    .andExpect(jsonPath("$[0].language.id", is(1)))
                    .andExpect(jsonPath("$[0].language.name", is("Language 1")))
                    .andExpect(jsonPath("$[0].language.code", is("L1")))
                    .andExpect(jsonPath("$[1].id", is(2)))
                    .andExpect(jsonPath("$[1].name", is("D2")))
                    .andExpect(jsonPath("$[1].language.id", is(2)))
                    .andExpect(jsonPath("$[1].language.name", is("Language 2")))
                    .andExpect(jsonPath("$[1].language.code", is("L2")));

            verify(dictionaryService, only()).findAll(USER);
        }
    }

    @Nested
    @DisplayName("Test GET /api/dictionaries/{id}")
    class GetDictionary {
        @Test
        @DisplayName("Should return NOT FOUND 404 if the dictionary does not exist")
        void getDictionary_NotFound() throws Exception {
            long dicId = 1L;
            when(dictionaryService.findById(dicId, USER)).thenReturn(Optional.empty());

            mockMvc.perform(get(URL_DICTIONARY_TEMPLATE, dicId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*not found.*")));

            verify(dictionaryService, only()).findById(dicId, USER);
        }

        @Test
        @DisplayName("Should return OK 200 when the dictionary exists")
        void getDictionary_Found() throws Exception {
            long dicId = 1L;
            Dictionary foundDictionary = Dictionary.builder().id(dicId).user(USER).build();
            when(dictionaryService.findById(dicId, USER)).thenReturn(Optional.of(foundDictionary));

            mockMvc.perform(get(URL_DICTIONARY_TEMPLATE, dicId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.id", is((int) dicId)));

            verify(dictionaryService, only()).findById(dicId, USER);
        }
    }

    @Nested
    @DisplayName("Test POST /api/dictionaries")
    class CreateDictionary {
        @Test
        @DisplayName("Should return BAD REQUEST 400 if the dictionary payload contains an id")
        void createNewDictionary_DictionaryExists() throws Exception {
            long id = 1L;
            DictionaryCommand command = DictionaryCommand.builder().id(id).build();

            mockMvc.perform(post(URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toPayload(command))
            )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*should not contain id.*")));

            verifyNoInteractions(dictionaryService);
        }

        @Test
        @DisplayName("Should return OK 200 if the dictionary is saved")
        void createNewDictionary_Success() throws Exception {
            long newId = 1L;
            Dictionary saved = Dictionary.builder().id(newId).build();
            when(dictionaryService.save(any(Dictionary.class), eq(USER))).thenReturn(Optional.of(saved));

            DictionaryCommand command = DictionaryCommand.builder().build();
            mockMvc.perform(post(URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toPayload(command))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.id", is((int) newId)));

            verify(dictionaryService, times(1)).save(any(Dictionary.class), eq(USER));
            verifyNoMoreInteractions(dictionaryService);
        }
    }

    @Nested
    @DisplayName("Test PUT /api/dictionaries")
    class UpdateDictionary {
        @Test
        @DisplayName("Should return NOT FOUND 404 if the dictionary does not exist")
        void dictionaryDoesNotExist_Failure() throws Exception {
            long dicId = 1L;
            when(dictionaryService.findById(dicId, USER)).thenReturn(Optional.empty());

            DictionaryCommand command = DictionaryCommand.builder()
                    .id(dicId)
                    .build();
            mockMvc.perform(put(URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toPayload(command))
            )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*not found.*")));

            verify(dictionaryService, only()).findById(dicId, USER);
        }

        @Test
        @DisplayName("Should return OK 200 if the dictionary exists")
        void dictionaryExists_Success() throws Exception {
            long dicId = 1L;
            Dictionary foundDictionary = Dictionary.builder().id(dicId).user(USER).build();
            when(dictionaryService.findById(dicId, USER)).thenReturn(Optional.of(foundDictionary));
            Dictionary savedDictionary = Dictionary.builder().id(dicId).build();
            when(dictionaryService.save(any(Dictionary.class), eq(USER))).thenReturn(Optional.of(savedDictionary));

            DictionaryCommand command = DictionaryCommand.builder()
                    .id(dicId)
                    .build();
            mockMvc.perform(put(URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toPayload(command))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.id", is((int) dicId)));

            verify(dictionaryService, times(1)).findById(dicId, USER);
            verify(dictionaryService, times(1)).save(any(Dictionary.class), eq(USER));
            verifyNoMoreInteractions(dictionaryService);
        }
    }

    @Nested
    @DisplayName("Test DELETE /api/dictionaries/{id}")
    class DeleteDictionary {
        @Test
        @DisplayName("Should return SERVICE UNAVAILABLE 503 if deletion fails")
        void deletionFails() throws Exception {
            long dicId = DICTIONARY_1.getId();
            DICTIONARY_1.setUser(USER);
            when(dictionaryService.deleteById(dicId, USER)).thenReturn(false);

            mockMvc.perform(delete(URL_DICTIONARY_TEMPLATE, dicId))
                    .andExpect(status().isServiceUnavailable())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", matchesRegex(".*unavailable.*")));

            verify(dictionaryService, only()).deleteById(dicId, USER);
        }

        @Test
        @DisplayName("Should return OK 200 and delete the dictionary if it exists and belongs to the right user")
        void deleteDictionary_Success() throws Exception {
            long id = DICTIONARY_1.getId();
            DICTIONARY_1.setUser(USER);

            when(dictionaryService.deleteById(id, USER)).thenReturn(true);

            mockMvc.perform(delete(URL_DICTIONARY_TEMPLATE, id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is((int) id)));

            verify(dictionaryService, only()).deleteById(id, USER);
        }
    }
}