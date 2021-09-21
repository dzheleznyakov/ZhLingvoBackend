package zh.lingvo.core;

import zh.lingvo.core.domain.PartOfSpeech;

import java.util.List;

public interface LanguageDescriptor {
    String getLanguageName();

    String getLanguageNativeName();

    String getLanguageCode();

    List<PartOfSpeech> getPartsOfSpeech();

    String getPartOfSpeechNativeShortName(PartOfSpeech pos);

    String getPartOfSpeechNativeName(PartOfSpeech pos);
}
