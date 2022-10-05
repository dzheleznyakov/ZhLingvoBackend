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

public class RussianLanguageDescriptor implements zh.lingvo.core.LanguageDescriptor {
    private static final Map<PartOfSpeech, String[]> POS_DATA = ImmutableMap.<PartOfSpeech, String[]>builder()
            .put(PartOfSpeech.NOUN, new String[]{"сущ.", "имя существительное"})
            .put(PartOfSpeech.VERB, new String[]{"гл.", "глагол"})
            .put(PartOfSpeech.ADJECTIVE, new String[]{"прил.", "имя прилагательное"})
            .put(PartOfSpeech.ADVERB, new String[]{"нареч.", "наречие"})
            .put(PartOfSpeech.PRONOUN, new String[]{"мест.", "местоимение"})
            .put(PartOfSpeech.NUMERAL, new String[]{"числит.", "имя числительное"})
            .put(PartOfSpeech.PREPOSITION, new String[]{"предл.", "предлог"})
            .put(PartOfSpeech.CONJUNCTION, new String[]{"союз", "союз"})
            .put(PartOfSpeech.INTERJECTION, new String[]{"межд.", "междометие"})
            .put(PartOfSpeech.EXCLAMATION, new String[]{"воскл.", "восклицание"})
            .build();

    @Override
    public String getLanguageName() {
        return "Russian";
    }

    @Override
    public String getLanguageNativeName() {
        return "Русский";
    }

    @Override
    public String getLanguageCode() {
        return "Ru";
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
