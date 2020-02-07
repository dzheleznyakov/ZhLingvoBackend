package zh.lingvo.domain.words;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.util.CollectionUtils;

import java.util.List;

public class PosBlock implements WordEntity {
    private final PartOfSpeech pos;
    private List<Meaning> meanings;

    public PosBlock(PartOfSpeech pos) {
        this.pos = pos;
    }

    public PartOfSpeech getPos() {
        return pos;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<Meaning> meanings) {
        this.meanings = CollectionUtils.toImmutableList(meanings);
    }

    @Override
    public boolean isVoid() {
        return pos == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PosBlock posBlock = (PosBlock) o;
        return pos == posBlock.pos &&
                Objects.equal(meanings, posBlock.meanings);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pos, meanings);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("partOfSpeech", pos)
                .add("#OfMeanings", meanings == null ? "null" : meanings.size())
                .toString();
    }
}
