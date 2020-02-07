package zh.lingvo.domain.words;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import zh.lingvo.util.CollectionUtils;

import java.util.List;

public class SemanticBlock {
    private List<PosBlock> posBlocks;

    public List<PosBlock> getPosBlocks() {
        return posBlocks;
    }

    public void setPosBlocks(List<PosBlock> posBlocks) {
        this.posBlocks = CollectionUtils.toImmutableList(posBlocks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SemanticBlock that = (SemanticBlock) o;
        return Objects.equal(posBlocks, that.posBlocks);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(posBlocks);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("semanticBlocks", posBlocks)
                .toString();
    }
}
