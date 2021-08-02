package zh.lingvo.core.en;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.core.LanguageDescriptor;
import zh.lingvo.core.domain.PartOfSpeech;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@DisplayName("Test English language descriptor")
class EnglishDescriptorTest {
    private LanguageDescriptor descriptor;

    @BeforeEach
    void setUp() {
        descriptor = new EnglishDescriptor();
    }

    @Test
    @DisplayName("Should return En language code")
    void testLanguageCode() {
        String code = descriptor.getLanguageCode();

        assertThat(code, is(equalTo("En")));
    }

    @Test
    @DisplayName("Should return English language parts of speech")
    void testPoS() {
        List<PartOfSpeech> posList = descriptor.getPartsOfSpeech();

        List<PartOfSpeech> expectedPosList = ImmutableList.of(
                PartOfSpeech.NOUN, PartOfSpeech.VERB, PartOfSpeech.ADJECTIVE,
                PartOfSpeech.ADVERB, PartOfSpeech.PRONOUN, PartOfSpeech.NUMERAL,
                PartOfSpeech.PREPOSITION, PartOfSpeech.CONJUNCTION, PartOfSpeech.INTERJECTION,
                PartOfSpeech.DETERMINER
        );

        assertThat(posList, is(equalTo(expectedPosList)));
    }
}