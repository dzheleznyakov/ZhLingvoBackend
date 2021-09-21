package zh.lingvo.core;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zh.lingvo.core.domain.PartOfSpeech;

import java.util.List;
import java.util.Map;

public class TempLD implements LanguageDescriptor {
    private static final Map<PartOfSpeech, String[]> POS_DATA = ImmutableMap.<PartOfSpeech, String[]>builder()
            .put(PartOfSpeech.NOUN, new String[]{"n", "nombre sustantivo"})
            .put(PartOfSpeech.VERB, new String[]{"v", "verbo"})
            .build();

    @Override
    public String getLanguageName() {
        return "Spanish";
    }

    @Override
    public String getLanguageNativeName() {
        return "Español";
    }

    @Override
    public String getLanguageCode() {
        return "Es";
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
