package zh.lingvo.core;

import zh.lingvo.core.domain.PartOfSpeech;

import java.util.List;

public interface LanguageDescriptor {
    String getLanguageCode();

    List<PartOfSpeech> getPartsOfSpeech();
}
