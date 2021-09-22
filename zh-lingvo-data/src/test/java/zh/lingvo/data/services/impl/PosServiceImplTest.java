package zh.lingvo.data.services.impl;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.core.domain.PartOfSpeech;
import zh.lingvo.data.services.PosService;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Test PoS service")
class PosServiceImplTest {
    private PosService posService;

    @BeforeEach
    void setUp() {
        posService = new PosServiceImpl();
    }

    @Test
    @DisplayName("Should return all parts of speech")
    void findAll() {
        Set<PartOfSpeech> allPos = posService.findAll();

        Set<PartOfSpeech> expectedAllPos = ImmutableSet.copyOf(new PartOfSpeech[]{
                PartOfSpeech.NOUN, PartOfSpeech.VERB, PartOfSpeech.ADJECTIVE, PartOfSpeech.ADVERB, PartOfSpeech.NUMERAL,
                PartOfSpeech.PRONOUN, PartOfSpeech.PREPOSITION, PartOfSpeech.CONJUNCTION, PartOfSpeech.INTERJECTION,
                PartOfSpeech.DETERMINER
        });

        assertThat(allPos, equalTo(expectedAllPos));
    }
}