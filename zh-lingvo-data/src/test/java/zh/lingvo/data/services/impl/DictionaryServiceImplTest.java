package zh.lingvo.data.services.impl;

import com.google.common.collect.ImmutableList;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.User;
import zh.lingvo.data.repositories.DictionaryRepository;
import zh.lingvo.data.services.DictionaryService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static zh.hamcrest.ZhMatchers.empty;
import static zh.hamcrest.ZhMatchers.hasPropertySatisfying;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test implemented Dictionary service")
class DictionaryServiceImplTest {
    @Mock
    private DictionaryRepository dictionaryRepository;

    private DictionaryService service;
    private static final Long ID = 42L;

    @BeforeEach
    void setUp() {
        service = new DictionaryServiceImpl(dictionaryRepository);
    }

    @Nested
    @DisplayName("Test DictionaryServiceImpl.findById(id)")
    class FindById {
        @Test
        @DisplayName("Should return nothing if no dictionary is found by the id")
        void foundNothing_ReturnNothing() {
            when(dictionaryRepository.findById(ID)).thenReturn(Optional.empty());

            Optional<Dictionary> dictionaryOptional = service.findById(ID);

            assertThat(dictionaryOptional, is(empty()));
            verify(dictionaryRepository, only()).findById(ID);
        }

        @Test
        @DisplayName("Should return a dictionary if it exists")
        void returnFoundDictionary() {
            Dictionary dictionary = Dictionary.builder().id(ID).build();
            when(dictionaryRepository.findById(ID)).thenReturn(Optional.of(dictionary));

            Optional<Dictionary> dictionaryOptional = service.findById(ID);

            assertThat(dictionaryOptional, is(not(empty())));
            assertThat(dictionaryOptional, hasPropertySatisfying(Dictionary::getId, ID::equals));
            verify(dictionaryRepository, only()).findById(ID);
        }
    }

    @Nested
    @DisplayName("Test DictionaryServiceImpl.findAllByUser(user)")
    class FindAllByUser {
        private final User user = User.builder().id(1L).build();

        @Test
        @DisplayName("Should return empty list if the user does not have dictionaries")
        void userHasNoDictionaries_ReturnEmptyList() {
            when(dictionaryRepository.findAllByUser(user)).thenReturn(ImmutableList.of());

            List<Dictionary> dictionaries = service.findAllByUser(user);

            assertThat(dictionaries, is(Matchers.empty()));
            verify(dictionaryRepository, only()).findAllByUser(user);
        }

        @Test
        @DisplayName("Should return all user's dictionaries")
        void userHasDictionaries_ReturnThem() {
            Dictionary dictionary1 = Dictionary.builder().id(1L).build();
            Dictionary dictionary2 = Dictionary.builder().id(2L).build();
            when(dictionaryRepository.findAllByUser(user)).thenReturn(ImmutableList.of(dictionary1, dictionary2));

            List<Dictionary> dictionaries = service.findAllByUser(user);

            assertThat(dictionaries, is(equalTo(ImmutableList.of(dictionary1, dictionary2))));
            verify(dictionaryRepository, only()).findAllByUser(user);
        }
    }

    @Nested
    @DisplayName("Test DictionaryServiceImpl.save(dictionary)")
    class Save {
        @Test
        @DisplayName("Should save given dictionary")
        void save() {
            Dictionary dicToSave = new Dictionary();

            Dictionary persistedDic = Dictionary.builder().id(ID).build();
            when(dictionaryRepository.save(dicToSave)).thenReturn(persistedDic);

            Dictionary savedDic = service.save(dicToSave);

            assertThat(savedDic, is(notNullValue()));
            assertThat(savedDic.getId(), is(ID));
            verify(dictionaryRepository, only()).save(dicToSave);
        }
    }

    @Nested
    @DisplayName("Test DictionaryServiceImpl.existsById(id)")
    class ExistsById {
        @Test
        @DisplayName("Should return false if null is passed in")
        void returnFalseOnNull() {
            boolean exists = service.existsById(null);

            assertThat(exists, is(false));
            verifyNoInteractions(dictionaryRepository);
        }

        @Test
        @DisplayName("Should return false if the dictionary does not exists")
        void dictionaryExists() {
            when(dictionaryRepository.existsById(ID)).thenReturn(false);

            boolean exists = service.existsById(ID);

            assertThat(exists, is(false));
            verify(dictionaryRepository, only()).existsById(ID);
        }

        @Test
        @DisplayName("Should return true if the dictionary exists")
        void dictionaryDoesNotExist() {
            when(dictionaryRepository.existsById(ID)).thenReturn(true);

            boolean exists = service.existsById(ID);

            assertThat(exists, is(true));
            verify(dictionaryRepository, only()).existsById(ID);
        }
    }
}