package zh.lingvo.domain;

import zh.lingvo.domain.languages.Language;
import zh.lingvo.util.ConfigReader;

import java.util.Map;
import java.util.Objects;

public enum PartOfSpeech {
    NOUN,
    VERB,
    ADJECTIVE;

    private static final Map<String, Map<String, PartOfSpeech>> nameMap =  ConfigReader.get()
            .getMap("languages",
                    langConfig -> langConfig.getString("code"),
                    langConfig -> langConfig.getMap("partsOfSpeeches",
                            posConfig -> posConfig.getString("name"),
                            posConfig -> PartOfSpeech.valueOf(posConfig.getString("type"))
                    )
            );

    public static PartOfSpeech fromName(Language language, String name) {
        return language.getPartsOfSpeech().stream()
                .filter(pos -> Objects.equals(language.getPartsOfSpeechName(pos), name))
                .findAny()
                .get();
    }
}
