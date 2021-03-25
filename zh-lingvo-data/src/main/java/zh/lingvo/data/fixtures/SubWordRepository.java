package zh.lingvo.data.fixtures;

import org.springframework.data.repository.CrudRepository;

public interface SubWordRepository<T, ID> extends CrudRepository<T, ID> {
    Class<T> getSubWordClass();
}
