package zh.lingvo.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zh.lingvo.data.model.SemanticBlock;

public interface SemanticBlockRepository extends CrudRepository<SemanticBlock, Long> {
}
