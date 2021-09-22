package zh.lingvo.core;

import com.google.common.collect.ImmutableList;
import zh.lingvo.core.domain.PartOfSpeech;

import java.util.List;

public interface LanguageDescriptor {
    LanguageDescriptor NULL = new LanguageDescriptor() {
        @Override
        public String getLanguageName() {
            return "";
        }

        @Override
        public String getLanguageNativeName() {
            return "";
        }

        @Override
        public String getLanguageCode() {
            return "";
        }

        @Override
        public List<PartOfSpeech> getPartsOfSpeech() {
            return ImmutableList.of();
        }

        @Override
        public String getPartOfSpeechNativeShortName(PartOfSpeech pos) {
            return "";
        }

        @Override
        public String getPartOfSpeechNativeName(PartOfSpeech pos) {
            return "";
        }
    };

    String getLanguageName();

    String getLanguageNativeName();

    String getLanguageCode();

    List<PartOfSpeech> getPartsOfSpeech();

    String getPartOfSpeechNativeShortName(PartOfSpeech pos);

    String getPartOfSpeechNativeName(PartOfSpeech pos);
}
