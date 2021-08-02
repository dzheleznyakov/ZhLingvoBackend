package zh.lingvo.core.en;

import com.google.common.collect.ImmutableList;
import zh.lingvo.core.LanguageDescriptor;
import zh.lingvo.core.domain.PartOfSpeech;

import java.util.List;

public class EnglishDescriptor implements LanguageDescriptor {
    @Override
    public String getLanguageCode() {
        return "En";
    }

    @Override
    public List<PartOfSpeech> getPartsOfSpeech() {
        return ImmutableList.of(
                PartOfSpeech.NOUN, PartOfSpeech.VERB, PartOfSpeech.ADJECTIVE,
                PartOfSpeech.ADVERB, PartOfSpeech.PRONOUN, PartOfSpeech.NUMERAL,
                PartOfSpeech.PREPOSITION, PartOfSpeech.CONJUNCTION, PartOfSpeech.INTERJECTION,
                PartOfSpeech.DETERMINER
        );
    }
}
