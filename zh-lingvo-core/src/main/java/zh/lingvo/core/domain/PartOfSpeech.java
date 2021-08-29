/*
This code is automatically generated.
Do not change it manually.
*/

package zh.lingvo.core.domain;

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

    private final String shortName;

    PartOfSpeech(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return this.shortName;
    }

    private static final Map<String, PartOfSpeech> BY_SHORT_NAME = Arrays.stream(PartOfSpeech.values())
            .collect(ImmutableMap.toImmutableMap(PartOfSpeech::getShortName, Function.identity()));

    public static PartOfSpeech fromShortName(String shortName) {
        return BY_SHORT_NAME.get(shortName);
    }
}
