package zh.lingvo.rest.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zh.lingvo.core.LanguageDescriptor;
import zh.lingvo.core.LanguageDescriptorManager;
import zh.lingvo.core.domain.PartOfSpeech;
import zh.lingvo.rest.commands.PosCommand;
import zh.lingvo.rest.converters.requests.PosConversionRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test PosConversionRequest to PosCommand converter")
class PosToPosCommandTest {
    @Mock
    private LanguageDescriptorManager languageDescriptorManager;

    @Mock
    private LanguageDescriptor fooLanguageDescriptor;

    private PosToPosCommand converter;

    @BeforeEach
    void setUp() {
        converter = new PosToPosCommand(languageDescriptorManager);
    }

    @Test
    @DisplayName("Should convert null to null")
    void convertNull() {
        PosCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert PosConversionRequest into PosCommand")
    void convertPosConversionRequest() {
        String langCode = "Fo";
        PartOfSpeech pos = PartOfSpeech.NOUN;
        String nativeName = "FooNoun";
        String nativeShortName = "fooN";
        PosConversionRequest request = PosConversionRequest.builder()
                .pos(pos)
                .langCode(langCode)
                .build();
        when(languageDescriptorManager.get(langCode)).thenReturn(fooLanguageDescriptor);
        when(fooLanguageDescriptor.getPartOfSpeechNativeName(pos)).thenReturn(nativeName);
        when(fooLanguageDescriptor.getPartOfSpeechNativeShortName(pos)).thenReturn(nativeShortName);

        PosCommand command = converter.convert(request);

        assertThat(command, is(notNullValue()));
        assertThat(command.getName(), is("NOUN"));
        assertThat(command.getDefaultShortName(), is("n"));
        assertThat(command.getNativeName(), is(nativeName));
        assertThat(command.getNativeShortName(), is(nativeShortName));

        verify(languageDescriptorManager, atMost(2)).get(langCode);
        verify(fooLanguageDescriptor, times(1)).getPartOfSpeechNativeName(pos);
        verify(fooLanguageDescriptor, times(1)).getPartOfSpeechNativeShortName(pos);
    }
}