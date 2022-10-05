package zh.lingvo.data.services.impl;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.core.LanguageDescriptorManager;
import zh.lingvo.core.domain.PartOfSpeech;
import zh.lingvo.data.services.PosService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@DisplayName("Test PoS service")
class PosServiceImplTest {
    private PosService posService;

    @BeforeEach
    void setUp() throws IOException {
        posService = new PosServiceImpl(new LanguageDescriptorManager());
    }

    @Test
    @DisplayName("Should return all parts of speech for a language")
    void findAll() {
        List<PartOfSpeech> allPos = posService.findAll("En");

        Set<PartOfSpeech> expectedAllPos = ImmutableSet.copyOf(new PartOfSpeech[]{
                PartOfSpeech.NOUN, PartOfSpeech.VERB, PartOfSpeech.ADJECTIVE, PartOfSpeech.ADVERB, PartOfSpeech.NUMERAL,
                PartOfSpeech.PRONOUN, PartOfSpeech.PREPOSITION, PartOfSpeech.CONJUNCTION, PartOfSpeech.INTERJECTION,
                PartOfSpeech.DETERMINER, PartOfSpeech.EXCLAMATION
        });

        assertThat(ImmutableSet.copyOf(allPos), equalTo(expectedAllPos));
    }

    @Test
    @DisplayName("Should return an empty list if a language is not supported")
    void allPos_NothingForLanguage() {
        List<PartOfSpeech> allPos = posService.findAll("No");

        assertThat(allPos, is(empty()));
    }

    @Test
    @DisplayName("Should return a short name of a part of speech")
    void getShortName() {
        String actual = posService.getShortName(PartOfSpeech.NOUN);

        assertThat(actual, is("n"));
    }

    @Test
    @DisplayName("Should return the short name of a part of speech in a given language")
    void getShortNativeName() {
        String actual = posService.getShortNativeName(PartOfSpeech.NOUN, "Ru");

        assertThat(actual, is("сущ."));
    }

    @Test
    @DisplayName("Should return the native name of a part of speech in a given language")
    void getNativeName() {
        String actual = posService.getNativeName(PartOfSpeech.NOUN, "Ru");

        assertThat(actual, is("имя существительное"));
    }
}