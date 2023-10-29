package zh.lingvo.data.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zh.lingvo.data.model.Meaning;
import zh.lingvo.data.model.SemanticBlock;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.Word;
import zh.lingvo.data.repositories.MeaningRepository;
import zh.lingvo.data.services.MeaningService;
import zh.lingvo.data.services.WordService;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static zh.hamcrest.ZhMatchers.empty;
import static zh.hamcrest.ZhMatchers.hasPropertySatisfying;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test implemented Meaning service")
class MeaningServiceImplTest {
    private static final Long MEANING_ID = 42L;
    private static final Long WORD_ID = 100L;
    private static final User USER = User.builder().id(13L).build();

    @Mock
    private MeaningRepository meaningRepository;

    @Mock
    private WordService wordService;

    private MeaningService service;

    @BeforeEach
    void setUp() {
        service = new MeaningServiceImpl(meaningRepository, wordService);
    }

    @Nested
    @DisplayName("Test MeaningServiceImpl.findById(id, user)")
    class FindById {
        @Test
        @DisplayName("Should return nothing if the user is not eligible to see the meaning")
        void userIsNotEligibleToSeeMeaning_ReturnNothing() {
            Word word = Word.builder()
                    .id(WORD_ID)
                    .build();
            SemanticBlock sb = SemanticBlock.builder()
                    .word(word)
                    .build();
            Meaning meaning = Meaning.builder()
                    .id(MEANING_ID)
                    .semBlock(sb)
                    .build();
            when(meaningRepository.findById(MEANING_ID)).thenReturn(Optional.of(meaning));
            when(wordService.userIsAuthorised(word, USER)).thenReturn(false);

            Optional<Meaning> optionalMeaning = service.findById(MEANING_ID, USER);

            assertThat(optionalMeaning, is(empty()));

            verify(meaningRepository, only()).findById(MEANING_ID);
            verify(wordService, only()).userIsAuthorised(word, USER);
        }

        @Test
        @DisplayName("Should return nothing if the meaning is not found")
        void meaningNotFound_ReturnNothing() {
            when(meaningRepository.findById(MEANING_ID)).thenReturn(Optional.empty());

            Optional<Meaning> optionalMeaning = service.findById(MEANING_ID, USER);
            assertThat(optionalMeaning, is(empty()));

            verify(meaningRepository, only()).findById(MEANING_ID);
            verifyNoInteractions(wordService);
        }

        @Test
        @DisplayName("Should return the meaning if it is found and the user is eligible to see it")
        void meaningIsFoundAndUserIsEligible_ReturnMeaning() {
            Word word = Word.builder()
                    .id(WORD_ID)
                    .build();
            SemanticBlock sb = SemanticBlock.builder()
                    .word(word)
                    .build();
            Meaning meaning = Meaning.builder()
                    .id(MEANING_ID)
                    .semBlock(sb)
                    .build();
            when(meaningRepository.findById(MEANING_ID)).thenReturn(Optional.of(meaning));
            when(wordService.userIsAuthorised(word, USER)).thenReturn(true);

            Optional<Meaning> optionalMeaning = service.findById(MEANING_ID, USER);

            assertThat(optionalMeaning, is(not(empty())));
            assertThat(optionalMeaning, hasPropertySatisfying(Meaning::getId, is(MEANING_ID)));

            verify(meaningRepository, only()).findById(MEANING_ID);
            verify(wordService, only()).userIsAuthorised(word, USER);
        }
    }
}