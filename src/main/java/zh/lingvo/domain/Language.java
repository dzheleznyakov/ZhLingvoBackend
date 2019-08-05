package zh.lingvo.domain;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import zh.lingvo.util.ConfigReader;

import java.util.Map;

public class Language {
    private final String code;
    private final String name;
    private Map<String, String> partsOfSpeechEncoding;

    public Language(ConfigReader config) {
        this.code = config.getString("code");
        this.name = config.getString("name");
        partsOfSpeechEncoding = config.getList("partsOfSpeeches", c -> new String[]{ c.getString("type"), c.getString("name") })
                .stream()
                .collect(ImmutableMap.toImmutableMap(a -> a[0], a -> a[1]));
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getPartsOfSpeechName(PartOfSpeech partOfSpeech) {
        return partsOfSpeechEncoding.getOrDefault(partOfSpeech.name(), "");
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("code", code)
                .add("name", name)
                .toString();
    }
}
