package zh.lingvo.data.services.impl;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zh.lingvo.data.exceptions.FailedToPersist;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.Word;
import zh.lingvo.data.repositories.WordRepository;
import zh.lingvo.data.services.DictionaryService;
import zh.lingvo.data.services.WordService;

import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static zh.hamcrest.ZhMatchers.empty;
import static zh.hamcrest.ZhMatchers.hasPropertySatisfying;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test WordServiceImpl")
class WordServiceImplTest {
    private static final Long WORD_ID = 42L;
    private static final long DICTIONARY_ID = 100L;

    private User user;
    private User anotherUser;
    private Dictionary dictionary;
    private Word word;

    @Mock
    private WordRepository wordRepository;

    @Mock
    private DictionaryService dictionaryService;

    private WordService service;

    @BeforeEach
    void setUpService() {
        service = new WordServiceImpl(wordRepository, dictionaryService, new SubWordServiceImpl(ImmutableSet.of()));
    }

    @BeforeEach
    void setUpEntities() {
        user = User.builder().id(1L).build();
        anotherUser = User.builder().id(2L).build();
        dictionary = Dictionary.builder().id(DICTIONARY_ID).user(user).build();
        word = Word.builder().id(WORD_ID).mainForm("word").dictionary(dictionary).build();
    }

    @Nested
    @DisplayName("Test WordServiceImpl.getById(id)")
    class FindById {
        @Test
        @DisplayName("Should return nothing if the word is in the dictionary of another user")
        void differentUser() {
            dictionary.setUser(anotherUser);
            when(wordRepository.findById(WORD_ID)).thenReturn(Optional.of(word));

            Optional<Word> foundWord = service.findById(WORD_ID, user);

            assertThat(foundWord, is(empty()));
            verify(wordRepository, only()).findById(WORD_ID);
        }

        @Test
        @DisplayName("Should return nothing if the word does not exist")
        void wordDoesNotExist() {
            when(wordRepository.findById(WORD_ID)).thenReturn(Optional.empty());

            Optional<Word> foundWord = service.findById(WORD_ID, user);

            assertThat(foundWord, is(empty()));
            verify(wordRepository, only()).findById(WORD_ID);
        }

        @Test
        @DisplayName("Should return the word if it is in the dictionary of this user")
        void thisUser() {
            when(wordRepository.findById(WORD_ID)).thenReturn(Optional.of(word));

            Optional<Word> foundWord = service.findById(WORD_ID, user);

            assertThat(foundWord, is(not(empty())));
            verify(wordRepository, only()).findById(WORD_ID);
        }
    }

    @Nested
    @DisplayName("Test WordServiceImpl.create(word, dictionary, user)")
    class Create {
        private Word newWord;

        @BeforeEach
        void setUpDictionary() {
            newWord = Word.builder().mainForm(word.getMainForm()).build();
        }

        @Test
        @DisplayName("Should not create the word if it already exists")
        void wordAlreadyExists() {
            Optional<Word> optionalWord = service.create(word, DICTIONARY_ID, user);

            assertThat(optionalWord, is(empty()));
            verifyNoInteractions(wordRepository, dictionaryService);
        }

        @Test
        @DisplayName("Should not create the work if it is to be put to the dictionary of a different user")
        void differentUser() {
            when(dictionaryService.findById(DICTIONARY_ID, user)).thenReturn(Optional.empty());

            Optional<Word> optionalWord = service.create(newWord, DICTIONARY_ID, user);

            assertThat(optionalWord, is(empty()));
            verify(dictionaryService, only()).findById(DICTIONARY_ID, user);
            verifyNoMoreInteractions(dictionaryService);
            verifyNoInteractions(wordRepository);
        }

        @Test
        @DisplayName("Should create the word if it does not exist yet and belongs to this user")
        void happyPath() {
            when(dictionaryService.findById(DICTIONARY_ID, user)).thenReturn(Optional.of(dictionary));
            when(wordRepository.save(newWord)).thenReturn(word);

            Optional<Word> optionalWord = service.create(newWord, DICTIONARY_ID, user);

            assertThat(optionalWord, is(not(empty())));
            assertThat(optionalWord, hasPropertySatisfying(Word::getId, Objects::nonNull));
            assertThat(optionalWord, hasPropertySatisfying(Word::getMainForm, newWord.getMainForm()::equals));
            assertThat(optionalWord, hasPropertySatisfying(Word::getDictionary, dictionary::equals));
            verify(dictionaryService, only()).findById(DICTIONARY_ID, user);
            verify(wordRepository, only()).save(newWord);
            verifyNoMoreInteractions(dictionaryService, wordRepository);
        }
    }

    @Nested
    @DisplayName("Test WordServiceImpl.update(word, user)")
    class Update {
        @Test
        @DisplayName("Should throw FailedToPersist exception if the word does not exist")
        void wordDoesNotExist() {
            Word newWord = Word.builder().mainForm("blah").dictionary(dictionary).build();

            assertThrows(FailedToPersist.class, () -> service.update(newWord, user));
        }

        @Test
        @DisplayName("Should throw FailedToPersist exception if the word exists, but from another user's dictionary")
        void anotherUser() {
            assertThrows(FailedToPersist.class, () -> service.update(word, anotherUser));
        }

        @Test
        @DisplayName("Should throw FailedToPersist exception if the word is missing dictionary")
        void missingDictionary() {
            word.setDictionary(null);
            assertThrows(FailedToPersist.class, () -> service.update(word, user));
        }

        @Test
        @DisplayName("Should update the word")
        void updateWordMainForm() {
            when(wordRepository.save(word)).thenReturn(word);

            service.update(word, user);

            verify(wordRepository, only()).save(word);
        }
    }
}