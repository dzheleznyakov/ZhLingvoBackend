/*
This code is automatically generated.
Do not change it manually.
*/

package zh.lingvo.core.descriptors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zh.lingvo.core.domain.PartOfSpeech;

import java.util.List;
import java.util.Map;

public class EnglishLanguageDescriptor implements zh.lingvo.core.LanguageDescriptor {
    private static final Map<PartOfSpeech, String[]> POS_DATA = ImmutableMap.<PartOfSpeech, String[]>builder()
            .put(PartOfSpeech.NOUN, new String[]{"n", "noun"})
            .put(PartOfSpeech.VERB, new String[]{"v", "verb"})
            .put(PartOfSpeech.ADJECTIVE, new String[]{"adj", "adjective"})
            .put(PartOfSpeech.ADVERB, new String[]{"adv", "adverb"})
            .put(PartOfSpeech.PRONOUN, new String[]{"pron", "pronoun"})
            .put(PartOfSpeech.NUMERAL, new String[]{"num", "numeral"})
            .put(PartOfSpeech.PREPOSITION, new String[]{"prep", "preposition"})
            .put(PartOfSpeech.CONJUNCTION, new String[]{"conj", "conjunction"})
            .put(PartOfSpeech.INTERJECTION, new String[]{"interj", "interjection"})
            .put(PartOfSpeech.DETERMINER, new String[]{"det", "determiner"})
            .build();

    @Override
    public String getLanguageName() {
        return "English";
    }

    @Override
    public String getLanguageNativeName() {
        return "English";
    }

    @Override
    public String getLanguageCode() {
        return "En";
    }

    @Override
    public List<PartOfSpeech> getPartsOfSpeech() {
        return ImmutableList.copyOf(POS_DATA.keySet());
    }

    @Override
    public String getPartOfSpeechNativeShortName(PartOfSpeech pos) {
        return getPosData(pos, 0);
    }

    @Override
    public String getPartOfSpeechNativeName(PartOfSpeech pos) {
        return getPosData(pos, 1);
    }

    private String getPosData(PartOfSpeech pos, int index) {
        if (POS_DATA.containsKey(pos)) {
            return POS_DATA.get(pos)[index];
        }
        return "";
    }
}
