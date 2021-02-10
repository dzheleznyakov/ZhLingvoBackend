package zh.lingvo.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zh.lingvo.data.domain.Example;

public interface ExampleRepository extends CrudRepository<Example, Long> {
}
