package zh.lingvo.data.fixtures;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;

import static zh.lingvo.data.constants.Profiles.IGNORE;

@Profile(IGNORE)
public interface SubWordRepository<T, ID> extends CrudRepository<T, ID> {
    Class<T> getSubWordClass();
}
