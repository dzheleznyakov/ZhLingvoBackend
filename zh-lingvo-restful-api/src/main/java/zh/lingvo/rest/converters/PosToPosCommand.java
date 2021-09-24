package zh.lingvo.rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import zh.lingvo.core.LanguageDescriptorManager;
import zh.lingvo.core.domain.PartOfSpeech;
import zh.lingvo.rest.commands.PosCommand;
import zh.lingvo.rest.converters.requests.PosConversionRequest;

import javax.annotation.Nullable;

@Service
public class PosToPosCommand implements Converter<PosConversionRequest, PosCommand> {
    private final LanguageDescriptorManager languageDescriptorManager;

    public PosToPosCommand(LanguageDescriptorManager languageDescriptorManager) {
        this.languageDescriptorManager = languageDescriptorManager;
    }

    @Override
    public PosCommand convert(@Nullable PosConversionRequest source) {
        return source == null ? null : PosCommand.builder()
                .name(source.getPos().name())
                .defaultShortName(source.getPos().getShortName())
                .nativeName(getPosNativeName(source))
                .nativeShortName(getPosNativeShortName(source))
                .build();
    }

    private String getPosNativeName(PosConversionRequest source) {
        String langCode = source.getLangCode();
        PartOfSpeech pos = source.getPos();
        return languageDescriptorManager
                .get(langCode)
                .getPartOfSpeechNativeName(pos);
    }

    private String getPosNativeShortName(PosConversionRequest source) {
        String langCode = source.getLangCode();
        PartOfSpeech pos = source.getPos();
        return languageDescriptorManager
                .get(langCode)
                .getPartOfSpeechNativeShortName(pos);
    }
}
