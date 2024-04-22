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

public class PortugueseLanguageDescriptor implements zh.lingvo.core.LanguageDescriptor {
    private static final Map<PartOfSpeech, String[]> POS_DATA = ImmutableMap.<PartOfSpeech, String[]>builder()
            .put(PartOfSpeech.NOUN, new String[]{"s", "substantivo"})
            .put(PartOfSpeech.VERB, new String[]{"v", "verbo"})
            .put(PartOfSpeech.ADJECTIVE, new String[]{"adj", "adjetivo"})
            .put(PartOfSpeech.ADVERB, new String[]{"adv", "advérbio"})
            .put(PartOfSpeech.PRONOUN, new String[]{"pron", "pronome"})
            .put(PartOfSpeech.NUMERAL, new String[]{"num", "numeral"})
            .put(PartOfSpeech.PREPOSITION, new String[]{"prep", "preposição"})
            .put(PartOfSpeech.CONJUNCTION, new String[]{"conj", "conjunção"})
            .put(PartOfSpeech.INTERJECTION, new String[]{"interj", "interjeição"})
            .put(PartOfSpeech.DETERMINER, new String[]{"art", "artigo"})
            .put(PartOfSpeech.CONTRACTION, new String[]{"contr", "contração"})
            .build();

    @Override
    public String getLanguageName() {
        return "Portuguese";
    }

    @Override
    public String getLanguageNativeName() {
        return "Português";
    }

    @Override
    public String getLanguageCode() {
        return "Pt";
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
