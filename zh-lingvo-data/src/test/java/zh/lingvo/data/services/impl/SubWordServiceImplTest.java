package zh.lingvo.data.services.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zh.lingvo.data.model.Example;
import zh.lingvo.data.model.Meaning;
import zh.lingvo.data.model.SemanticBlock;
import zh.lingvo.data.model.Translation;
import zh.lingvo.data.repositories.ExampleRepository;
import zh.lingvo.data.repositories.MeaningRepository;
import zh.lingvo.data.repositories.SemanticBlockRepository;
import zh.lingvo.data.repositories.TranslationRepository;
import zh.lingvo.data.services.SubWordService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static com.google.common.base.MoreObjects.firstNonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static zh.hamcrest.ZhMatchers.empty;
import static zh.hamcrest.ZhMatchers.hasPropertySatisfying;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test SubWordServiceImpl")
class SubWordServiceImplTest {
    @Mock
    private SemanticBlockRepository semBlockRepository;

    @Mock
    private MeaningRepository meaningRepository;

    @Mock
    private TranslationRepository translationRepository;

    @Mock
    private ExampleRepository exampleRepository;

    private SubWordService service;

    @BeforeEach
    void setUp() {
        when(semBlockRepository.getSubWordClass()).thenReturn(SemanticBlock.class);
        when(meaningRepository.getSubWordClass()).thenReturn(Meaning.class);
        when(translationRepository.getSubWordClass()).thenReturn(Translation.class);
        when(exampleRepository.getSubWordClass()).thenReturn(Example.class);

        service = new SubWordServiceImpl(ImmutableSet.of(
                semBlockRepository, meaningRepository, translationRepository, exampleRepository));
    }

    @AfterEach
    void tearDown() {
        verify(semBlockRepository, times(1)).getSubWordClass();
        verify(meaningRepository, times(1)).getSubWordClass();
        verify(translationRepository, times(1)).getSubWordClass();
        verify(exampleRepository, times(1)).getSubWordClass();
        verifyNoMoreInteractions(semBlockRepository, meaningRepository, translationRepository, exampleRepository);
    }

    @Nested
    @DisplayName("Test SubWordServiceImpl.save(entity)")
    class Save {
        @Test
        @DisplayName("Should save the example")
        void saveExample() {
            Example exampleToSave = getExample(null);
            Example persistedExample = getExample(1L);
            when(exampleRepository.save(exampleToSave)).thenReturn(persistedExample);

            Optional<Example> exampleOptional = service.save(exampleToSave);

            assertThat(exampleOptional, is(not(empty())));
            assertThat(exampleOptional, hasPropertySatisfying(Function.identity(), persistedExample::equals));
            verify(exampleRepository, times(1)).save(exampleToSave);
        }

        @Test
        @DisplayName("Should save the translation")
        void saveTranslation() {
            Translation translationToSave = getTranslation(null);
            Translation persistedTranslation = getTranslation(1L);
            when(translationRepository.save(translationToSave)).thenReturn(persistedTranslation);

            Optional<Translation> translationOptional = service.save(translationToSave);

            assertThat(translationOptional, is(not(empty())));
            assertThat(translationOptional, hasPropertySatisfying(Function.identity(), persistedTranslation::equals));
            verify(translationRepository, times(1)).save(translationToSave);
        }

        @Test
        @DisplayName("Should save the meaning, example and the translation")
        void saveMeaning_OneTranslation_OneExample() {
            Example exampleToSave = getExample(null);
            Example savedExample = getExample(1L);
            Translation translationToSave = getTranslation(null);
            Translation savedTranslation = getTranslation(1L);
            Meaning meaningToSave = getMeaning(null, ImmutableSet.of(exampleToSave), ImmutableSet.of(translationToSave));
            Meaning savedMeaning = getMeaning(1L, ImmutableSet.of(savedExample), ImmutableSet.of(savedTranslation));

            when(exampleRepository.saveAll(anyList())).thenReturn(ImmutableList.of(savedExample));
            when(translationRepository.saveAll(anyList())).thenReturn(ImmutableList.of(savedTranslation));
            when(meaningRepository.save(meaningToSave)).thenReturn(savedMeaning);

            Optional<Meaning> meaningOptional = service.save(meaningToSave);

            assertThat(meaningOptional, is(not(empty())));
            assertThat(meaningOptional, hasPropertySatisfying(Function.identity(), savedMeaning::equals));
            verify(meaningRepository, times(1)).save(meaningToSave);
            verify(exampleRepository, times(1)).saveAll(anyList());
            verify(translationRepository, times(1)).saveAll(anyList());
        }

        @Test
        @DisplayName("Should save the meaning and both examples")
        void saveMeaning_TwoExamples() {
            Example exampleToSave1 = getExample(null);
            String explanation2 = "test explanation 2";
            Example exampleToSave2 = getExample(null, explanation2, null, null);
            Example savedExample1 = getExample(1L);
            Example savedExample2 = getExample(2L, explanation2, null, null);
            Meaning meaningToSave = getMeaning(null, ImmutableSet.of(exampleToSave1, exampleToSave2), null);
            Meaning savedMeaning = getMeaning(null, ImmutableSet.of(savedExample1, savedExample2), null);

            when(exampleRepository.saveAll(anyList())).thenReturn(ImmutableList.of(savedExample1, savedExample2));
            when(meaningRepository.save(meaningToSave)).thenReturn(savedMeaning);

            Optional<Meaning> meaningOptional = service.save(meaningToSave);

            assertThat(meaningOptional, is(not(empty())));
            assertThat(meaningOptional, hasPropertySatisfying(Function.identity(), savedMeaning::equals));
            verify(meaningRepository, times(1)).save(meaningToSave);
            verify(exampleRepository, times(1)).saveAll(anyList());
            verify(translationRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Should save the semantic block and both meanings")
        void saveSemanticBlock_TwoMeanings() {
            Example exampleToSave = getExample(null);
            Example savedExample = getExample(1L);
            Meaning meaningToSave1 = getMeaning(null, ImmutableSet.of(exampleToSave), null);
            Meaning savedMeaning1 = getMeaning(1L, ImmutableSet.of(savedExample), null);
            Meaning meaningToSave2 = getMeaning(null, null, null);
            Meaning savedMeaning2 = getMeaning(2L, null, null);
            SemanticBlock blockToSave = getSemanticBlock(null, ImmutableList.of(meaningToSave1, meaningToSave2));
            SemanticBlock savedBlock = getSemanticBlock(1L, ImmutableList.of(savedMeaning1, savedMeaning2));

            when(meaningRepository.saveAll(anyList())).thenReturn(ImmutableList.of(savedMeaning1, savedMeaning2));
            when(exampleRepository.saveAll(anyList())).thenReturn(ImmutableList.of(savedExample));
            when(semBlockRepository.save(blockToSave)).thenReturn(savedBlock);

            Optional<SemanticBlock> blockOptional = service.save(blockToSave);

            assertThat(blockOptional, is(not(empty())));
            assertThat(blockOptional, hasPropertySatisfying(Function.identity(), savedBlock::equals));
            verify(semBlockRepository, times(1)).save(blockToSave);
            verify(exampleRepository, times(1)).saveAll(anyList());
            verify(meaningRepository, times(1)).saveAll(anyList());
        }
    }

    private Example getExample(Long id) {
        return getExample(id, "test explanation", "test expression", "test remark");
    }

    private Example getExample(Long id, String explanation, String expression, String remark) {
        return Example.builder()
                .id(id)
                .explanation(explanation)
                .expression(expression)
                .remark(remark)
                .build();
    }

    private Translation getTranslation(Long id) {
        return Translation.builder()
                .id(id)
                .elaboration("test elaboration")
                .value("test translation")
                .build();
    }

    private Meaning getMeaning(Long id, Set<Example> examples, Set<Translation> translations) {
        return Meaning.builder()
                .id(id)
                .remark("test mRemark")
                .examples(firstNonNull(examples, ImmutableSet.of()))
                .translations(firstNonNull(translations, ImmutableSet.of()))
                .build();
    }

    private SemanticBlock getSemanticBlock(Long id, List<Meaning> meanings) {
        return SemanticBlock.builder()
                .id(id)
                .meanings(firstNonNull(meanings, ImmutableList.of()))
                .build();
    }
}