package zh.lingvo.domain.words;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.util.CollectionUtils;

import java.util.List;

public class PartOfSpeechBlock {
    private final PartOfSpeech partOfSpeech;
    private List<Meaning> meanings;

    public PartOfSpeechBlock(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<Meaning> meanings) {
        this.meanings = CollectionUtils.toImmutableList(meanings);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("partOfSpeech", partOfSpeech)
                .add("#OfMeanings", meanings == null ? "null" : meanings.size())
                .toString();
    }
}
