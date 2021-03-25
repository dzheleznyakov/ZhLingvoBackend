package zh.lingvo.data.repositories;

import zh.lingvo.data.fixtures.SubWordRepository;
import zh.lingvo.data.model.Example;

public interface ExampleRepository extends SubWordRepository<Example, Long> {
    @Override
    default Class<Example> getSubWordClass() {
        return Example.class;
    }
}
