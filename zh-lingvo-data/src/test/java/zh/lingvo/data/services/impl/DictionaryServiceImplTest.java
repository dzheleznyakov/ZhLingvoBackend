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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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

    private final User user = User.builder().id(1L).build();

    @BeforeEach
    void setUp() {
        service = new DictionaryServiceImpl(dictionaryRepository);
    }

    @Nested
    @DisplayName("Test DictionaryServiceImpl.findById(id)")
    class FindById {
        @Test
        @DisplayName("Should return nothing if no dictionary is found by the id for the user")
        void foundNothing_ReturnNothing() {
            when(dictionaryRepository.findByIdAndUser(ID, user)).thenReturn(Optional.empty());

            Optional<Dictionary> dictionaryOptional = service.findById(ID, user);

            assertThat(dictionaryOptional, is(empty()));
            verify(dictionaryRepository, only()).findByIdAndUser(ID, user);
        }

        @Test
        @DisplayName("Should return a dictionary if it exists")
        void returnFoundDictionary() {
            Dictionary dictionary = Dictionary.builder().id(ID).build();
            when(dictionaryRepository.findByIdAndUser(ID, user)).thenReturn(Optional.of(dictionary));

            Optional<Dictionary> dictionaryOptional = service.findById(ID, user);

            assertThat(dictionaryOptional, is(not(empty())));
            assertThat(dictionaryOptional, hasPropertySatisfying(Dictionary::getId, ID::equals));
            verify(dictionaryRepository, only()).findByIdAndUser(ID, user);
        }
    }

    @Nested
    @DisplayName("Test DictionaryServiceImpl.findAll(user)")
    class FindAll {
        @Test
        @DisplayName("Should return empty list if the user does not have dictionaries")
        void userHasNoDictionaries_ReturnEmptyList() {
            when(dictionaryRepository.findAllByUser(user)).thenReturn(ImmutableList.of());

            List<Dictionary> dictionaries = service.findAll(user);

            assertThat(dictionaries, is(Matchers.empty()));
            verify(dictionaryRepository, only()).findAllByUser(user);
        }

        @Test
        @DisplayName("Should return all user's dictionaries")
        void userHasDictionaries_ReturnThem() {
            Dictionary dictionary1 = Dictionary.builder().id(1L).build();
            Dictionary dictionary2 = Dictionary.builder().id(2L).build();
            when(dictionaryRepository.findAllByUser(user)).thenReturn(ImmutableList.of(dictionary1, dictionary2));

            List<Dictionary> dictionaries = service.findAll(user);

            assertThat(dictionaries, is(equalTo(ImmutableList.of(dictionary1, dictionary2))));
            verify(dictionaryRepository, only()).findAllByUser(user);
        }
    }

    @Nested
    @DisplayName("Test DictionaryServiceImpl.save(dictionary)")
    class Save {
        @Test
        @DisplayName("Existing dictionary: should save given dictionary if it belongs to the user")
        void save_Success() {
            String name = "test";
            Dictionary dicToSave = Dictionary.builder().id(ID).name(name).build();

            Dictionary persistedDic = Dictionary.builder().id(ID).name(name).build();
            when(dictionaryRepository.existsByIdAndUser(ID, user)).thenReturn(true);
            when(dictionaryRepository.save(dicToSave)).thenReturn(persistedDic);

            Optional<Dictionary> savedDicOptional = service.save(dicToSave, user);

            assertThat(savedDicOptional, is(not(empty())));
            Dictionary savedDic = savedDicOptional.get();
            assertThat(savedDic.getId(), is(ID));
            assertThat(savedDic.getName(), is(name));
            verify(dictionaryRepository, times(1)).existsByIdAndUser(ID, user);
            verify(dictionaryRepository, times(1)).save(dicToSave);
            verifyNoMoreInteractions(dictionaryRepository);
        }

        @Test
        @DisplayName("Existing dictionary: should not save if it belongs to a different user")
        void save_DifferentUser() {
            String name = "test";
            Dictionary dicToSave = Dictionary.builder().id(ID).name(name).build();

            when(dictionaryRepository.existsByIdAndUser(ID, user)).thenReturn(false);

            Optional<Dictionary> savedDicOptional = service.save(dicToSave, user);

            assertThat(savedDicOptional, is(empty()));
            verify(dictionaryRepository, only()).existsByIdAndUser(ID, user);
        }

        @Test
        @DisplayName("New dictionary: should save it under this user")
        void save_NewDictionary() {
            String name = "test";
            Dictionary dicToSave = Dictionary.builder().name(name).build();

            Dictionary persistedDic = Dictionary.builder().id(ID).name(name).user(user).build();
            when(dictionaryRepository.save(dicToSave)).thenAnswer(invocation -> {
                Dictionary passedDic = invocation.getArgument(0);
                assertThat(passedDic.getUser(), is(user));
                return persistedDic;
            });

            Optional<Dictionary> savedDicOptional = service.save(dicToSave, user);

            assertThat(savedDicOptional, is(not(empty())));
            verify(dictionaryRepository, only()).save(dicToSave);
        }
    }

    @Nested
    @DisplayName("Test DictionaryServiceImpl.deleteById(id, user)")
    class DeleteById {
        private final Dictionary dictionary = Dictionary.builder()
                .id(ID)
                .user(user)
                .name("Test: delete by id")
                .build();

        @Test
        @DisplayName("Should return true if the deletion is successful")
        void deletionSuccessful() {
            when(dictionaryRepository.findByIdAndUser(ID, user)).thenReturn(Optional.of(dictionary));

            boolean deleted = service.deleteById(ID, user);

            assertThat(deleted, is(true));
            verify(dictionaryRepository, times(1)).findByIdAndUser(ID, user);
            verify(dictionaryRepository, times(1)).delete(dictionary);
            verifyNoMoreInteractions(dictionaryRepository);
        }

        @Test
        @DisplayName("Should return false if there is an error with the deletion")
        void deletionFails() {
            when(dictionaryRepository.findByIdAndUser(ID, user)).thenReturn(Optional.of(dictionary));
            doThrow(new RuntimeException("Something went terribly wrong"))
                    .when(dictionaryRepository).delete(dictionary);

            boolean deleted = service.deleteById(ID, user);

            assertThat(deleted, is(false));
            verify(dictionaryRepository, times(1)).findByIdAndUser(ID, user);
            verify(dictionaryRepository, times(1)).delete(dictionary);
            verifyNoMoreInteractions(dictionaryRepository);
        }

        @Test
        @DisplayName("Should return true if there the dictionary is not found (it does not exist at all or belongs to a different user)")
        void dictionaryNotFound() {
            when(dictionaryRepository.findByIdAndUser(ID, user)).thenReturn(Optional.empty());

            boolean deleted = service.deleteById(ID, user);

            assertThat(deleted, is(true));
            verify(dictionaryRepository, only()).findByIdAndUser(ID, user);
            verifyNoMoreInteractions(dictionaryRepository);
        }
    }
}