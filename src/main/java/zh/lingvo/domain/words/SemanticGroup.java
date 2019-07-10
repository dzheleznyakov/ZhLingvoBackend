package zh.lingvo.domain.words;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import zh.lingvo.util.CollectionUtils;

import java.util.List;

public class SemanticGroup {
    private List<SemanticBlock> semanticBlocks;

    public List<SemanticBlock> getSemanticBlocks() {
        return semanticBlocks;
    }

    public void setSemanticBlocks(List<SemanticBlock> semanticBlocks) {
        this.semanticBlocks = CollectionUtils.toImmutableList(semanticBlocks);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("semanticBlocks", semanticBlocks)
                .toString();
    }
}
