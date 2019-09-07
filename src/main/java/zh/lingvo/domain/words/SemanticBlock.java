package zh.lingvo.domain.words;

import com.google.common.base.MoreObjects;
import zh.lingvo.util.CollectionUtils;

import java.util.List;

public class SemanticBlock {
    private List<PartOfSpeechBlock> partOfSpeechBlocks;

    public List<PartOfSpeechBlock> getPartOfSpeechBlocks() {
        return partOfSpeechBlocks;
    }

    public void setPartOfSpeechBlocks(List<PartOfSpeechBlock> partOfSpeechBlocks) {
        this.partOfSpeechBlocks = CollectionUtils.toImmutableList(partOfSpeechBlocks);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("semanticBlocks", partOfSpeechBlocks)
                .toString();
    }
}
