package zh.lingvo.core;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.core.domain.PartOfSpeech;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Test Language Descriptors Manager (IT)")
class LanguageDescriptorManagerIT {
    private LanguageDescriptorManager manager;

    @BeforeEach
    void setUp() throws IOException {
         manager = new LanguageDescriptorManager();
    }

    @Test
    @DisplayName("Should support languages: En, Es, Ru")
    public void testGetLanguageCodes() throws IOException {
        List<String> actualCodes = manager.getLanguageCodes();

        Set<String> expectedCodes = ImmutableSet.of("En", "Es", "Ru");

        assertThat(actualCodes, is(notNullValue()));
        assertThat(ImmutableSet.copyOf(actualCodes), is(equalTo(expectedCodes)));
    }

    @Test
    @DisplayName("Should return a language descriptor by code")
    void testGetLanguageDescriptorByCode() {
        LanguageDescriptor descriptor = manager.get("Es");

        assertThat(descriptor.getLanguageCode(), is("Es"));
        assertThat(descriptor.getLanguageName(), is("Spanish"));
        assertThat(descriptor.getLanguageNativeName(), is("Español"));
    }

    @Test
    @DisplayName("Should return NULL language descriptor if the code is not supported")
    void testGetLanguageDescriptorByCode_NoLanguage() {
        LanguageDescriptor descriptor = manager.get("Fo");

        assertThat(descriptor.getLanguageCode(), is(""));
        assertThat(descriptor.getLanguageName(), is(""));
        assertThat(descriptor.getLanguageNativeName(), is(""));
        assertThat(descriptor.getPartsOfSpeech(), is(empty()));
        assertThat(descriptor.getPartOfSpeechNativeName(PartOfSpeech.NOUN), is(""));
        assertThat(descriptor.getPartOfSpeechNativeShortName(PartOfSpeech.NOUN), is(""));
    }
}