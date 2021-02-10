package zh.lingvo.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zh.lingvo.data.domain.SemanticBlock;

public interface SemanticBlockRepository extends CrudRepository<SemanticBlock, Long> {
}
