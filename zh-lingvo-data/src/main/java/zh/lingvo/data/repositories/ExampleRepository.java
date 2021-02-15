package zh.lingvo.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zh.lingvo.data.model.Example;

public interface ExampleRepository extends CrudRepository<Example, Long> {
}
