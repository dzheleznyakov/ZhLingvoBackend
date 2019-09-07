package zh.lingvo.domain;

import com.google.common.collect.ImmutableList;
import zh.lingvo.util.ConfigReader;

import java.util.List;
import java.util.Map;

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

    public static PartOfSpeech fromName(String languageCode, String name) {
        return nameMap.get(languageCode).get(name);
    }

    public static List<String> getPosNames(String languageCode) {
        return ImmutableList.copyOf(nameMap.get(languageCode).keySet());
    }
}
