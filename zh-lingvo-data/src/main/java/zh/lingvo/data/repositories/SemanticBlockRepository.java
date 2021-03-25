package zh.lingvo.data.repositories;

import zh.lingvo.data.fixtures.SubWordRepository;
import zh.lingvo.data.model.SemanticBlock;

public interface SemanticBlockRepository extends SubWordRepository<SemanticBlock, Long> {
    @Override
    default Class<SemanticBlock> getSubWordClass() {
        return SemanticBlock.class;
    }
}
