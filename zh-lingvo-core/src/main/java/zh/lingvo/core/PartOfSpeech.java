package zh.lingvo.core;

import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum PartOfSpeech {
    NOUN("n"),
    VERB("v"),
    ADJECTIVE("adj"),
    ADVERB("adv"),
    PRONOUN("pron"),
    NUMERAL("num"),
    PREPOSITION("prep"),
    CONJUNCTION("conj"),
    INTERJECTION("interj"),
    DETERMINER("det");

    private final String code;

    PartOfSpeech(String code) {
        this.code = code;
    }

    private static final Map<String, PartOfSpeech> POS_BY_CODE = Arrays.stream(PartOfSpeech.values())
            .collect(ImmutableMap.toImmutableMap(PartOfSpeech::getCode, Function.identity()));

    public String getCode() {
        return code;
    }

    public static PartOfSpeech fromCode(String code) {
        return POS_BY_CODE.get(code);
    }
}
